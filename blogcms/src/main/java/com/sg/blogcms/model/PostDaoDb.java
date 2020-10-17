package com.sg.blogcms.model;

import com.sg.blogcms.entity.Post;
import com.sg.blogcms.entity.User;
import com.sg.blogcms.model.UserDaoDb.UserMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PostDaoDb implements PostDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    @Transactional
    public Post createPost(Post post) {
        //insert
        String insertQuery = "INSERT INTO post(title, body, isApproved, isStaticPage, "
                + "createdOn, postOn, expireOn, userId) "
                + "VALUES(?,?,?,?,?,?,?,?);";
        jdbc.update(insertQuery, 
                post.getTitle(),
                post.getBody(),
                post.isApproved(),
                post.isStaticPage(),
                post.getCreatedOn(),
                post.getPostOn(),
                post.getExpireOn(),
                post.getUser().getId());
        
        //set id from db
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        post.setId(newId);
        
        //FIXME category stuff
        
        return post;
    }

    @Override
    public Post readPostById(int id) {
        try {
            String readQuery = "SELECT * FROM post "
                    + "WHERE postId = ?;";
            Post post = jdbc.queryForObject(readQuery, new PostMapper(), id);
            
            post.setUser(readUserForPost(post.getId()));
            //FIXME CATEGORY
            
            return post;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Post> readAllPosts() {
        String readAllQuery = "SELECT * FROM post;";
        List<Post> posts = jdbc.query(readAllQuery, new PostMapper());
        
        for (Post p : posts) {
            p.setUser(readUserForPost(p.getId()));
            //FIXME CATEGORY
        }
        
        return posts;
    }

    @Override
    public List<Post> readPostsByDate() {
        String readActivePosts = "SELECT * FROM post "
                + "WHERE postOn <= NOW() "
                + "AND expireOn >= NOW();";
        List<Post> activePosts = jdbc.query(readActivePosts, new PostMapper());
        
        for (Post p : activePosts) {
            p.setUser(readUserForPost(p.getId()));
            //FIXME CATEGORY
        }
        
        return activePosts;
    }

    @Override
    public List<Post> readPostsByApproval() {
        String readApprovedPosts = "SELECT * FROM post "
                + "WHERE isApproved != 0;";
        List<Post> approvedPosts = jdbc.query(readApprovedPosts, new PostMapper());
        
        for (Post p : approvedPosts) {
            p.setUser(readUserForPost(p.getId()));
            //FIXME CATEGORY
        }
        
        return approvedPosts;
    }

    @Override
    public List<Post> readAllForPublication() {
        String readPublish = "SELECT * FROM post "
                + "WHERE postOn <= NOW() "
                + "AND expireOn >= NOW() "
                + "AND isApproved != 0";
        List<Post> publishPosts = jdbc.query(readPublish, new PostMapper());
        
        for (Post p : publishPosts) {
            p.setUser(readUserForPost(p.getId()));
            //FIXME CATEGORY
        }
        
        return publishPosts;
    }

    @Override
    @Transactional
    public Post updatePost(Post post) {
        //FIXME CATEGORY
        String updateQuery = "UPDATE post "
                + "SET "
                + "title = ?, "
                + "body = ?, "
                + "isApproved = ?, "
                + "isStaticPage = ?, "
                + "createdOn = ?, "
                + "postOn = ?, "
                + "expireOn = ?, "
                + "userId = ? "
                + "WHERE postId = ?;";
        int updated = jdbc.update(updateQuery, 
                post.getTitle(),
                post.getBody(),
                post.isApproved(),
                post.isStaticPage(),
                post.getCreatedOn(),
                post.getPostOn(),
                post.getExpireOn(),
                post.getUser().getId(),
                post.getId());
        
        if (updated == 1) {
            //delete from bridge
            
            //reinsert to bridge
            
            return post;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deletePostById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*helpers*/
    /**
     * Retrieve the User for a Post
     * @param id {int} a valid id
     * @return {User} obj for a Post from db
     */
    private User readUserForPost(int id) throws DataAccessException {
        String readQuery = "SELECT u.* FROM user u "
                + "JOIN post p ON p.userId = u.userId "
                + "WHERE p.postId = ?;";
        return jdbc.queryForObject(readQuery, new UserMapper(), id);
    }
    
    /**
     * RowMapper impl
     */
    public static final class PostMapper implements RowMapper<Post>{

        @Override
        public Post mapRow(ResultSet rs, int i) throws SQLException {
            Post p = new Post();
            p.setTitle(rs.getString("title"));
            p.setBody(rs.getString("body"));
            p.setApproved(rs.getBoolean("isApproved"));
            p.setStaticPage(rs.getBoolean("isStaticPage"));
            p.setCreatedOn(rs.getTimestamp("createdOn").toLocalDateTime());
            p.setPostOn(rs.getTimestamp("postOn").toLocalDateTime());
            p.setExpireOn(rs.getTimestamp("expireOn").toLocalDateTime());
            //user and cetegory will be associated in methods
            
            return p;
        }
    }
    
}
