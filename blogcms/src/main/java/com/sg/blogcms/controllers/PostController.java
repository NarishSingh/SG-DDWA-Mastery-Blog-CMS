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
import javax.validation.Validation;
import javax.validation.Validator;
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

    /*MAIN - ADMIN/CREATOR*/
    /**
     * GET - load the creation main page
     *
     * @param model {Model} load related lists and errors
     * @return {String} load page
     */
    @GetMapping("/createPost")
    public String displayCreatePage(Model model) {
        model.addAttribute("categories", cDao.readAllCategories());
        model.addAttribute("errors", violations);

        return "createPost";
    }

    /*CREATE - ADMIN/CREATOR*/
    /**
     * POST - create a new blog post
     *
     * @param request  {HttpServletRequest} pull in form data
     * @param file     {MultipartFile} pull in cover photo
     * @param postOn   {LocalDateTime} data from form for when to post blog
     * @param expireOn {LocalDateTime} data from form for when to remove blog
     * @param model    {Model} hold lists and errors on fail to post
     * @return {String} redirect to blog scroll, reload page w errors if fail
     */
    @PostMapping("addPost")
    public String addPost(HttpServletRequest request, @RequestParam("file") MultipartFile file,
            @RequestParam("postOn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime postOn,
            @RequestParam("expireOn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expireOn,
            Model model) {
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

        List<Category> categories = new ArrayList<>();
        String[] categoryIds = request.getParameterValues("categoryId");
        for (String id : categoryIds) {
            categories.add(cDao.readCategoryById(Integer.parseInt(id)));
        }
        post.setCategories(categories);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(post);

        if (violations.isEmpty()) {
            pDao.createPost(post);
        } else {
            model.addAttribute("categories", cDao.readAllCategories());
            model.addAttribute("errors", violations);

            return "createPost";
        }

        return "redirect:/createPost"; //TODO should load blog scroll, change later
    }

    /**
     * Create a new category to hashtag a post
     *
     * @param request {HttpServletRequest} pulls in form data
     * @param model   {Model} holds errors if category is invalid
     * @return {String} redirect to post creation page if created, reload page
     *         with errors if fail
     */
    @PostMapping("createCategory")
    public String addCategory(HttpServletRequest request, Model model) {
        Category c = new Category();
        String categoryString = request.getParameter("category");

        if (!categoryString.isBlank()) {
            c.setCategory(categoryString);

            cDao.createCategory(c);
        } else {
            model.addAttribute("errors", violations);
            return "createCategory";
        }

        return "redirect:/createPost";
    }

    /*EDIT - ADMIN ONLY*/
    /*DELETE - ADMIN ONLY*/
}
