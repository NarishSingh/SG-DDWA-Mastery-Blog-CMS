package com.sg.blogcms.controllers;

import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import com.sg.blogcms.model.ImageDao;
import com.sg.blogcms.model.RoleDao;
import com.sg.blogcms.model.UserDao;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {

    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    ImageDao iDao;
    @Autowired
    PasswordEncoder encoder;
    private final String userUploadDir = "Users";
    Set<ConstraintViolation<User>> violations = new HashSet<>();

    /*MAIN*/
    /**
     * GET - load subdomain with all users
     *
     * @param model {Model} holds all users, regardless of role
     * @return {String} load subdomain
     */
    @GetMapping("/admin")
    public String displayAdminPage(Model model) {
        model.addAttribute("roles", rDao.readAllRoles());
        model.addAttribute("users", uDao.readAllUsers());
        model.addAttribute("errors", violations);

        return "admin";
    }

    /*CREATE*/
    /**
     * POST - create a new user account from form data
     *
     * @param request {HttpServletRequest} marshals form data for obj
     *                construction and validation
     * @return {String} reload admin subdomain
     */
    @PostMapping("/addUser")
    public String addUser(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String filePath = iDao.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), userUploadDir);
        
        User user = new User();
        String usernameString = request.getParameter("username");
        String passwordString = request.getParameter("password");

        if (usernameString.isBlank() || passwordString.isBlank()) {
            return "admin";
        } else {
            user.setUsername(usernameString);
            user.setPassword(encoder.encode(passwordString));
            user.setEnabled(true); //admin creates accounts, assume true
            user.setFirstName(request.getParameter("firstName"));
            user.setLastName(request.getParameter("lastName"));
            user.setEmail(request.getParameter("email"));
            user.setPassword(filePath);

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(rDao.readRoleById(Integer.parseInt(request.getParameter("roleId"))));
            user.setRoles(userRoles);
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);

        if (violations.isEmpty()) {
            uDao.createUser(user);
        }

        return "redirect:/admin";
    }
    
    //TODO edit user with the edit user template

    /*DELETE*/
    /**
     * POST - delete a user account
     *
     * @param request {HttpServletRequest} will retrieve id from data table
     * @return {String} reload the admin page
     */
    @PostMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        uDao.deleteUserById(id);

        return "redirect:/admin";
    }
}
