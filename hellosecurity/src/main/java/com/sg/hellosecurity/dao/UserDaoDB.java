package com.sg.hellosecurity.dao;

import com.sg.hellosecurity.dao.RoleDaoDB.RoleMapper;
import com.sg.hellosecurity.dto.Role;
import com.sg.hellosecurity.dto.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDaoDB implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public User getUserById(int id) {
        try {
            final String SELECT_USER_BY_ID = "SELECT * FROM user "
                    + "WHERE id = ?;";
            User user = jdbc.queryForObject(SELECT_USER_BY_ID, new UserMapper(), id);
            user.setRoles(getRolesForUser(user.getId()));
            
            return user;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            final String SELECT_USER_BY_USERNAME = "SELECT * FROM user "
                    + "WHERE username = ?;";
            User user = jdbc.queryForObject(SELECT_USER_BY_USERNAME, new UserMapper(), username);
            user.setRoles(getRolesForUser(user.getId()));
            
            return user;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        final String SELECT_ALL_USERS = "SELECT * FROM user;";
        List<User> users = jdbc.query(SELECT_ALL_USERS, new UserMapper());
        for (User user : users) {
            user.setRoles(getRolesForUser(user.getId()));
        }
        
        return users;
    }

    private Set<Role> getRolesForUser(int id) throws DataAccessException {
        final String SELECT_ROLES_FOR_USER = "SELECT r.* FROM user_role ur "
                + "JOIN role r ON ur.role_id = r.id "
                + "WHERE ur.user_id = ?";
        Set<Role> roles = new HashSet(jdbc.query(SELECT_ROLES_FOR_USER, new RoleMapper(), id));
        return roles;
    }

    @Override
    public void updateUser(User user) {
        final String UPDATE_USER = "UPDATE user "
                + "SET "
                + "username = ?, "
                + "password = ?, "
                + "enabled = ? "
                + "WHERE id = ?;";
        jdbc.update(UPDATE_USER, user.getUsername(), user.getPassword(), user.isEnabled(), user.getId());

        //delete from bridge table
        final String DELETE_USER_ROLE = "DELETE FROM user_role "
                + "WHERE user_id = ?;";
        jdbc.update(DELETE_USER_ROLE, user.getId());
        
        //reinsert the updated obj to bridge
        for (Role role : user.getRoles()) {
            final String INSERT_USER_ROLE = "INSERT INTO user_role(user_id, role_id) "
                    + "VALUES(?,?);";
            jdbc.update(INSERT_USER_ROLE, user.getId(), role.getId());
        }
    }

    @Override
    public void deleteUser(int id) {
        final String DELETE_USER_ROLE = "DELETE FROM user_role "
                + "WHERE user_id = ?;";
        final String DELETE_USER = "DELETE FROM user "
                + "WHERE id = ?;";
        jdbc.update(DELETE_USER_ROLE, id);
        jdbc.update(DELETE_USER, id);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        final String INSERT_USER = "INSERT INTO user(username, password, enabled) "
                + "VALUES(?,?,?);";
        jdbc.update(INSERT_USER, user.getUsername(), user.getPassword(), user.isEnabled());
        int newId = jdbc.queryForObject("select LAST_INSERT_ID()", Integer.class);
        user.setId(newId);

        for (Role role : user.getRoles()) {
            final String INSERT_USER_ROLE = "INSERT INTO user_role(user_id, role_id) VALUES(?,?)";
            jdbc.update(INSERT_USER_ROLE, user.getId(), role.getId());
        }
        
        return user;
    }

    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEnabled(rs.getBoolean("enabled"));
            
            return user;
        }
    }
}