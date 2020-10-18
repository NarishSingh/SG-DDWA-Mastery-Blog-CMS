package com.sg.blogcms.model;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostDaoTest {

    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    CategoryDao cDao;
    @Autowired
    PostDao pDao;

    static Role role1;
    static Role role2;
    static Role role3;

    static User user1;
    static User user2disabled;
    static User creator;
    static User admin;

    static Category category1;
    static Category category2;
    static Category category3;

    static Post p1;
    static Post p2;
    static Post p3;
    static Post p4;

    public PostDaoTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        /*clean db*/
        for (Role r : rDao.readAllRoles()) {
            rDao.deleteRoleById(r.getId());
        }

        for (User u : uDao.readAllUsers()) {
            uDao.deleteUserById(u.getId());
        }

        for (Category c : cDao.readAllCategories()) {
            cDao.deleteCategoryById(c.getId());
        }

        for (Post p : pDao.readAllPosts()) {
            pDao.deletePostById(p.getId());
        }

        /*Create roles*/
        Role r1 = new Role();
        r1.setRole("User");
        role1 = rDao.createRole(r1);

        Role r2 = new Role();
        r2.setRole("Content Creator");
        role2 = rDao.createRole(r2);

        Role r3 = new Role();
        r3.setRole("Admin");
        role3 = rDao.createRole(r3);

        /*Create users*/
        //just a regular user
        User u1 = new User();
        u1.setUsername("test1");
        u1.setPassword("password001");
        u1.setEnabled(true);
        u1.setFirstName("First");
        u1.setLastName("Tester");
        u1.setEmail("test1@mail.com");
        Set<Role> u1r = new HashSet<>();
        u1r.add(role1);
        u1.setRoles(u1r);
        user1 = uDao.createUser(u1);

        //disabled account, some nulls
        User u2disabled = new User();
        u2disabled.setUsername("test2disabled");
        u2disabled.setPassword("password002");
        u2disabled.setEnabled(false);
        u2disabled.setFirstName("Second");
        u2disabled.setLastName(null);
        u2disabled.setEmail(null);
        Set<Role> u2disabledr = new HashSet<>();
        u2disabledr.add(role1);
        u2disabled.setRoles(u2disabledr);
        user2disabled = uDao.createUser(u2disabled);

        //content creator
        User cc = new User();
        cc.setUsername("creator");
        cc.setPassword("password003");
        cc.setEnabled(true);
        cc.setFirstName("First");
        cc.setLastName("Creator");
        cc.setEmail("test3@mail.com");
        Set<Role> ccr = new HashSet<>();
        ccr.add(role1);
        ccr.add(role2);
        cc.setRoles(ccr);
        creator = uDao.createUser(cc);

        //admin
        User adm = new User();
        adm.setUsername("admin");
        adm.setPassword("password003");
        adm.setEnabled(true);
        adm.setFirstName("The");
        adm.setLastName("Admin");
        adm.setEmail("admin@mail.com");
        Set<Role> ar = new HashSet<>();
        ar.add(role1);
        ar.add(role2);
        ar.add(role3);
        adm.setRoles(ar);
        admin = uDao.createUser(adm);

        /*Create categories*/
        Category c1 = new Category();
        c1.setCategory("Upcoming");
        category1 = cDao.createCategory(c1);

        Category c2 = new Category();
        c2.setCategory("Release");
        category2 = cDao.createCategory(c2);

        Category c3 = new Category();
        c3.setCategory("Update");
        category3 = cDao.createCategory(c3);

        /*Create posts*/
        //by admin, approved, static, post now -> no expiration
        p1 = new Post();
        p1.setTitle("title1");
        p1.setBody("body1");
        p1.setApproved(true);
        p1.setStaticPage(true);
        p1.setCreatedOn(LocalDateTime.now());
        p1.setPostOn(LocalDateTime.now());
        p1.setExpireOn(null);
        p1.setUser(admin);
        List<Category> post1cat = new ArrayList<>();
        post1cat.add(category1);
        post1cat.add(category2);
        post1cat.add(category3);
        p1.setCategories(post1cat); //3 #'s

        //by creator, not approved, not static, post now -> expires in one month
        p2 = new Post();
        p2.setTitle("title2");
        p2.setBody("body2");
        p2.setApproved(false);
        p2.setStaticPage(false);
        p2.setCreatedOn(LocalDateTime.now());
        p2.setPostOn(LocalDateTime.now());
        p2.setExpireOn(LocalDateTime.now().plusMonths(1));
        p2.setUser(creator);
        List<Category> post2cat = new ArrayList<>();
        post2cat.add(category1);
        p2.setCategories(post2cat); //1 #

        //by admin, not approved, not static, post in one month -> no expire
        p3 = new Post();
        p3.setTitle("title3");
        p3.setBody("body3");
        p3.setApproved(false);
        p3.setStaticPage(false);
        p3.setCreatedOn(LocalDateTime.now());
        p3.setPostOn(LocalDateTime.now().plusMonths(1));
        p3.setExpireOn(null);
        p3.setUser(admin);
        List<Category> post3cat = new ArrayList<>();
        post3cat.add(category1);
        post3cat.add(category2);
        p3.setCategories(post3cat); //2#'s

        //by creator, approved, static, post in one month -> expires month after
        p4 = new Post();
        p4.setTitle("title4");
        p4.setBody("body4");
        p4.setApproved(true);
        p4.setStaticPage(true);
        p4.setCreatedOn(LocalDateTime.now());
        p4.setPostOn(LocalDateTime.now().plusMonths(1));
        p4.setExpireOn(LocalDateTime.now().plusMonths(2));
        p4.setUser(creator);
        List<Category> post4cat = new ArrayList<>();
        post4cat.add(category1);
        post4cat.add(category2);
        post4cat.add(category3);
        p4.setCategories(post4cat); //3 #'s
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createPost and readPostById method, of class PostDao.
     */
    @Test
    public void testCreateReadPostByIdPost() {
    }

    /**
     * Test of readAllPosts method, of class PostDao.
     */
    @Test
    public void testReadAllPosts() {
    }

    /**
     * Test of readPostsByDate method, of class PostDao.
     */
    @Test
    public void testReadPostsByDate() {
    }

    /**
     * Test of readPostsByApproval method, of class PostDao.
     */
    @Test
    public void testReadPostsByApproval() {
    }

    /**
     * Test of readAllForPublication method, of class PostDao.
     */
    @Test
    public void testReadAllForPublication() {
    }

    /**
     * Test of readPostsByCategory method, of class PostDao.
     */
    @Test
    public void testReadPostsByCategory() {
        /*
        Category cat1 = cDao.createCategory(c1);
        Category cat2 = cDao.createCategory(c2);
        Category cat3 = cDao.createCategory(c3);

        List<Category> post1cat = new ArrayList<>();
        post1cat.add(cat1);

        List<Category> post2cat = new ArrayList<>();
        post2cat.add(cat1);
        post2cat.add(cat2);

        //role
        Role cc = new Role();
        cc.setRole("Content Creator");
        Role creator = rDao.createRole(cc);

        Set<Role> roles = new HashSet<>();
        roles.add(creator);

        //user
        User u1 = new User();
        u1.setUsername("testman");
        u1.setPassword("password");
        u1.setEnabled(true);
        u1.setFirstName("test");
        u1.setLastName("man");
        u1.setEmail("testman@mail.com");
        u1.setRoles(roles);
        User user = uDao.createUser(u1);

        //post
        Post p2 = new Post();
        p2.setTitle("title1");
        p2.setBody("body1");
        p2.setApproved(true);
        p2.setStaticPage(false);
        p2.setCreatedOn(LocalDateTime.now());
        p2.setPostOn(LocalDateTime.now());
        p2.setExpireOn(LocalDateTime.now().plusMonths(1));
        p2.setUser(user);
        p2.setCategories(post1cat);
        Post post1 = pDao.createPost(p2);

        Post p1 = new Post();
        p1.setTitle("title2");
        p1.setBody("body2");
        p1.setApproved(true);
        p1.setStaticPage(true);
        p1.setCreatedOn(LocalDateTime.now());
        p1.setPostOn(LocalDateTime.now());
        p1.setExpireOn(null);
        p1.setUser(user);
        p1.setCategories(post2cat);
        Post post2 = pDao.createPost(p1);

        List<Post> cat1posts = pDao.readPublishedPostsByCategory(cat1.getId());
        List<Post> cat2posts = pDao.readPublishedPostsByCategory(cat2.getId());
        List<Post> cat3posts = pDao.readPublishedPostsByCategory(cat3.getId());

        assertNotNull(cat1posts);
        assertEquals(2, cat1posts.size());
        assertTrue(cat1posts.contains(post1));
        assertTrue(cat1posts.contains(post2));
        
        assertNotNull(cat2posts);
        assertEquals(1, cat2posts.size());
        assertTrue(cat2posts.contains(post1));
        
        assertNotNull(cat3posts);
        assertEquals(0, cat3posts.size());
         */
    }

    /**
     * Test of readPublishedPostsByCategory method, of class PostDao.
     */
    @Test
    public void testReadPublishedPostsByCategory() {
    }

    /**
     * Test of updatePost method, of class PostDao.
     */
    @Test
    public void testUpdatePost() {
    }

    /**
     * Test of deletePostById method, of class PostDao.
     */
    @Test
    public void testDeletePostById() {
    }

}
