package com.sg.blogcms.controllers;

import com.sg.blogcms.model.RoleDao;
import com.sg.blogcms.model.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
    
    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    PasswordEncoder encoder;
    
    /**
     * GET - load subdomain with all users
     * @param model {Model} holds all users, regardless of role
     * @return {String} load subdomain
     */
    @GetMapping("/admin")
    public String displayAdminPage(Model model){
        model.addAttribute("roles", rDao.readAllRoles());
        model.addAttribute("users", uDao.readAllUsers());
        
        return "admin";
    }
    
}
