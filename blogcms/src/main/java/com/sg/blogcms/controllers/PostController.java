package com.sg.blogcms.controllers;

import com.sg.blogcms.entity.Post;
import com.sg.blogcms.model.CategoryDao;
import com.sg.blogcms.model.ImageDao;
import com.sg.blogcms.model.PostDao;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {
    
    @Autowired
    CategoryDao cDao;
    @Autowired
    PostDao pDao;
    @Autowired
    ImageDao iDao;
    private final String coverUploadDir = "Posts";
    Set<ConstraintViolation<Post>> violations = new HashSet<>();
    
    /*MAIN*/
    @GetMapping("/createPost")
    public String displayCreatePage(Model model){
        model.addAttribute("categories", cDao.readAllCategories());
        model.addAttribute("errors", violations);
        
        return "createPost";
    }
    
    /*CREATE*/
    
    /*EDIT*/
    
    /*DELETE*/
}
