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

@Repository
public class PostDaoDb implements PostDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
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
        
        //FIXME should I do category stuff here?
        
        return post;
    }

    @Override
    public Post readPostById(int id) {
        try {
            String readQuery = "SELECT * FROM post "
                    + "WHERE postId = ?;";
            Post post = jdbc.queryForObject(readQuery, new PostMapper(), id);
            
            post.setUser(readUserForPost(post.getId()));
            
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
        }
        
        return posts;
    }

    @Override
    public List<Post> readPostsByDate() {
        String readActivePosts = "SELECT * FROM post "
                + "WHERE postOn"
    }

    @Override
    public List<Post> readPostsByApproval() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Post> readAllForPublication() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Post updatePost(Post post) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
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
            //user will be associated in methods
            
            return p;
        }
    }
    
}
