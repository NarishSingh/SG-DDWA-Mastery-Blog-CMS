package com.sg.blogcms.model;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryDaoTest {

    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    CategoryDao cDao;
    @Autowired
    PostDao pDao;

    static Category c1;
    static Category c2;
    static Category c3;

    public CategoryDaoTest() {
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

        /*Create categories*/
        c1 = new Category();
        c1.setCategory("Upcoming");

        c2 = new Category();
        c2.setCategory("Release");

        c3 = new Category();
        c3.setCategory("Update");
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createCategory and readCategoryById method, of class CategoryDao.
     */
    @Test
    public void testCreateReadCategoryByIdCategory() {
        Category cat1 = cDao.createCategory(c1);

        Category fromDao = cDao.readCategoryById(cat1.getId());

        assertNotNull(cat1);
        assertNotNull(fromDao);
        assertEquals(cat1, fromDao);
    }

    /**
     * Test of readCategoryByName method, of class CategoryDao.
     */
    @Test
    public void testReadCategoryByName() {
        Category cat1 = cDao.createCategory(c1);

        Category fromDao = cDao.readCategoryByName(cat1.getCategory());

        assertNotNull(cat1);
        assertNotNull(fromDao);
        assertEquals(cat1, fromDao);
    }

    /**
     * Test of readAllCategories method, of class CategoryDao.
     */
    @Test
    public void testReadAllCategories() {
        Category cat1 = cDao.createCategory(c1);
        Category cat2 = cDao.createCategory(c2);
        Category cat3 = cDao.createCategory(c3);

        List<Category> categories = cDao.readAllCategories();

        assertNotNull(categories);
        assertEquals(3, categories.size());
        assertTrue(categories.contains(cat1));
        assertTrue(categories.contains(cat2));
        assertTrue(categories.contains(cat3));
    }

    /**
     * Test of updateCategory method, of class CategoryDao.
     */
    @Test
    public void testUpdateCategory() {
        Category cat1 = cDao.createCategory(c1);
        Category original = cDao.readCategoryById(cat1.getId());

        cat1.setCategory("testEdit");
        Category cat1update = cDao.updateCategory(cat1);
        Category edit = cDao.readCategoryById(cat1.getId());

        assertNotNull(edit);
        assertEquals(cat1update, edit);
        assertNotEquals(original, edit);
    }

    /**
     * Test of deleteCategoryById method, of class CategoryDao.
     */
    @Test
    public void testDeleteCategoryById() {
        Category cat1 = cDao.createCategory(c1);
        Category cat2 = cDao.createCategory(c2);
        Category cat3 = cDao.createCategory(c3);
        List<Category> original = cDao.readAllCategories();

        boolean deleted = cDao.deleteCategoryById(cat2.getId());

        List<Category> afterDel = cDao.readAllCategories();

        assertNotNull(original);
        assertEquals(3, original.size());
        assertNotNull(afterDel);
        assertEquals(2, afterDel.size());
        assertNotEquals(original, afterDel);
        assertTrue(deleted);
        assertTrue(afterDel.contains(cat1));
        assertFalse(afterDel.contains(cat2));
        assertTrue(afterDel.contains(cat3));
    }

    //FIXME move to post dao testing
    /**
     * Test of readPostsByCategory method, of class CategoryDao.
     */
    /*
    @Test
    public void testReadPostsByCategory() {
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
        Post p1 = new Post();
        p1.setTitle("title1");
        p1.setBody("body1");
        p1.setApproved(true);
        p1.setStaticPage(false);
        p1.setCreatedOn(LocalDateTime.now());
        p1.setPostOn(LocalDateTime.now());
        p1.setExpireOn(LocalDateTime.now().plusMonths(1));
        p1.setUser(user);
        p1.setCategories(post1cat);
        Post post1 = pDao.createPost(p1);

        Post p2 = new Post();
        p2.setTitle("title2");
        p2.setBody("body2");
        p2.setApproved(true);
        p2.setStaticPage(true);
        p2.setCreatedOn(LocalDateTime.now());
        p2.setPostOn(LocalDateTime.now());
        p2.setExpireOn(null);
        p2.setUser(user);
        p2.setCategories(post2cat);
        Post post2 = pDao.createPost(p2);

        List<Post> cat1posts = cDao.readPublishedPostsByCategory(cat1.getId());
        List<Post> cat2posts = cDao.readPublishedPostsByCategory(cat2.getId());
        List<Post> cat3posts = cDao.readPublishedPostsByCategory(cat3.getId());

        assertNotNull(cat1posts);
        assertEquals(2, cat1posts.size());
        assertTrue(cat1posts.contains(post1));
        assertTrue(cat1posts.contains(post2));
        
        assertNotNull(cat2posts);
        assertEquals(1, cat2posts.size());
        assertTrue(cat2posts.contains(post1));
        
        assertNotNull(cat3posts);
        assertEquals(0, cat3posts.size());
    }
     */
}
