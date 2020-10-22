package com.sg.blogcms.controllers;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.model.CategoryDao;
import com.sg.blogcms.model.ImageDao;
import com.sg.blogcms.model.PostDao;
import com.sg.blogcms.model.UserDao;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostController {
    
    @Autowired
    UserDao uDao;
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
    @PostMapping("addPost")
    public String addPost(HttpServletRequest request, @RequestParam("file") MultipartFile file, 
            @RequestParam("postOn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime postOn,
            @RequestParam("expireOn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expireOn){
        String filePath = iDao.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), coverUploadDir);
        
        Post post = new Post();
        post.setTitle(request.getParameter("title"));
        post.setBody(request.getParameter("body"));
        post.setApproved(false); //admin must approve
        //FIXME static page?
//        post.setStaticPage(Boolean.parseBoolean(request.getParameter("")));
        post.setCreatedOn(LocalDateTime.now().withNano(0));
        post.setPostOn(postOn);
        post.setExpireOn(expireOn);
        //FIXME figure out how to grab the user
//        post.setUser(uDao.readUserById());
        post.setPhotoFilename(filePath);
        
        //FIXME this is supposed to be a multiselect, get a list of id's, for-e to read into list, set
        List<Category> categories = new ArrayList<>();
//        categories.add(cDao.readCategoryById(Integer.parseInt(request.getParameter("categoryId"))));
        
        
    }
    
    @PostMapping("addCategory")
    public String addCategory(HttpServletRequest request){
        
        return "createPost";
    }
    
    /*EDIT*/
    
    /*DELETE*/
}
