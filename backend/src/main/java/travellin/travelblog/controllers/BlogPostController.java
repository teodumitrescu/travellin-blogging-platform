package travellin.travelblog.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BlogPostDto> createBlogPost(@RequestBody BlogPostDto blogPostDto, @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));
        BlogPostDto createdBlogPost = blogPostService.createBlogPost(blogPostDto, userId);
        return ResponseEntity.ok(createdBlogPost);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<BlogPostDto> updateBlogPost(@PathVariable Long id, @RequestBody BlogPostDto blogPostDto) throws Exception {
    //     LocalDateTime now = LocalDateTime.now();
    //     BlogPost updatedBlogPost = blogPostService.updateBlogPost(
    //             id,
    //             blogPostDto.getTitle(),
    //             blogPostDto.getContent(),
    //             now,
    //             blogPostDto.getImages(),
    //             blogPostDto.getDestination() != null ? new Destination(blogPostDto.getDestination().getId(), blogPostDto.getDestination().getName()) : null,
    //             blogPostDto.getTags() != null ? blogPostDto.getTags().stream().map(tagDto -> new Tag(tagDto.getId(), tagDto.getName())).collect(Collectors.toList()) : null
    //     );
    //     return ResponseEntity.ok(mapToDto(updatedBlogPost));
    // }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDto> getBlogPostById(@PathVariable Long id) throws Exception {
        try {
            BlogPostDto blogPost = blogPostService.getBlogPostById(id);
            return ResponseEntity.ok(blogPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<BlogPostDto>> getAllBlogPosts() throws Exception {
        try {
            List<BlogPostDto> blogPosts = blogPostService.getAllBlogPosts();
            return ResponseEntity.ok(blogPosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{blogPostId}/tags/{tagId}")
    public ResponseEntity<Void> addTagToBlogPost(@PathVariable Long blogPostId, @PathVariable Long tagId) throws Exception {
        try {
            blogPostService.addTagToBlogPost(blogPostId, tagId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{blogPostId}/tags/{tagId}")
    public ResponseEntity<Void> removeTagFromBlogPost(@PathVariable Long blogPostId, @PathVariable Long tagId) throws Exception {
        try {
            blogPostService.removeTagFromBlogPost(blogPostId, tagId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // @GetMapping("/tags/{tagId}")
    // public ResponseEntity<List<BlogPostDto>> getAllBlogPostsByTag(@PathVariable Long tagId) throws Exception {
    //     Tag tag = tagService.getTagById(tagId)
    //         .orElseThrow(() -> new Exception("Tag not found with id " + tagId));
    //     List<BlogPostDto> blogPostDtos = blogPostService.getAllBlogPostsByTag(tag).stream()
    //         .map(BlogPostDto::new)
    //         .collect(Collectors.toList());
    //     return ResponseEntity.ok(blogPostDtos);
    // }

    // @GetMapping("/destinations/{destinationId}")
    // public ResponseEntity<List<BlogPostDto>> getAllBlogPostsByDestination(@PathVariable Long destinationId) throws Exception {
    //     Destination destination = destinationService.getDestinationById(destinationId);
    //     if (destination == null) {
    //         throw new Exception("Destination not found with id " + destinationId);
    //     }
    //     List<BlogPostDto> blogPostDtos = blogPostService.getAllBlogPostsByDestination(destination).stream()
    //         .map(BlogPostDto::new)
    //         .collect(Collectors.toList());
    //     return ResponseEntity.ok(blogPostDtos);
    // }
}