package travellin.travelblog.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import travellin.travelblog.dto.BlogPostDto;
import travellin.travelblog.security.config.JwtService;
import travellin.travelblog.services.BlogPostService;

@RestController
@RequestMapping("/blogposts")
public class BlogPostController {

    private final BlogPostService blogPostService;
    private final JwtService jwtService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService, JwtService jwtService) {
        this.blogPostService = blogPostService;
        this.jwtService = jwtService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<BlogPostDto> createBlogPost(@RequestBody BlogPostDto blogPostDto, @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));
        BlogPostDto createdBlogPost = blogPostService.createBlogPost(blogPostDto, userId);
        return ResponseEntity.ok(createdBlogPost);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> deleteBlogPost(@PathVariable Long id) throws Exception {
        try {
            blogPostService.deleteBlogPost(id);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", "Post " + id + " deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorMap);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMap);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlogPost(@PathVariable Long id, @RequestBody BlogPostDto blogPostDto) throws Exception {
        try {
            BlogPostDto updatedBlogPost = blogPostService.updateBlogPost(id, blogPostDto);
            return ResponseEntity.ok(updatedBlogPost);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getBlogPostById(@PathVariable Long id) throws Exception {
        try {
            BlogPostDto blogPost = blogPostService.getBlogPostById(id);
            return ResponseEntity.ok(blogPost);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);        }
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<BlogPostDto>> getAllBlogPosts() throws Exception {
        try {
            List<BlogPostDto> blogPosts = blogPostService.getAllBlogPosts();
            return ResponseEntity.ok(blogPosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/user={id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<BlogPostDto>> getAllBlogPostsByUserId(@PathVariable Long id) throws Exception {
        try {
            List<BlogPostDto> blogPosts = blogPostService.getAllBlogPostsByUserId(id);
            return ResponseEntity.ok(blogPosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{blogPostId}/addtags/{tagId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> addTagToBlogPost(@PathVariable Long blogPostId, @PathVariable Long tagId) throws Exception {
        try {
            blogPostService.addTagToBlogPost(blogPostId, tagId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);        }
    }

    @PostMapping("/{blogPostId}/removetags/{tagId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> removeTagFromBlogPost(@PathVariable Long blogPostId, @PathVariable Long tagId) throws Exception {
        try {
            blogPostService.removeTagFromBlogPost(blogPostId, tagId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);        }
    }
}