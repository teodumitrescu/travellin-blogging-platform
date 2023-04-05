package travellin.travelblog.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Destination;
import travellin.travelblog.entities.Tag;
import travellin.travelblog.entities.User;
import travellin.travelblog.services.BlogPostService;
import travellin.travelblog.services.DestinationService;
import travellin.travelblog.services.TagService;

@RestController
@RequestMapping("/blogposts")
public class BlogPostController {

    private final BlogPostService blogPostService;
	private final TagService tagService;
    private final DestinationService destinationService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService, TagService tagService, DestinationService destinationService) {
        this.blogPostService = blogPostService;
		this.tagService = tagService;
        this.destinationService = destinationService;
    }

    @PostMapping
    public ResponseEntity<BlogPost> createBlogPost(@RequestBody BlogPost blogPost) {
        User author = new User(); //to be replaced with authenticated user
        LocalDateTime now = LocalDateTime.now();
        BlogPost createdBlogPost = blogPostService.createBlogPost(
            blogPost.getTitle(), 
            blogPost.getContent(), 
            author, 
            now, 
            now, 
            blogPost.getImages(), 
            blogPost.getDestination(), 
            blogPost.getTags()
        );
        return ResponseEntity.ok(createdBlogPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updateBlogPost(@PathVariable Long id, @RequestBody BlogPost blogPost) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        BlogPost updatedBlogPost = blogPostService.updateBlogPost(
            id, 
            blogPost.getTitle(), 
            blogPost.getContent(), 
            now, 
            blogPost.getImages(), 
            blogPost.getDestination(), 
            blogPost.getTags()
        );
        return ResponseEntity.ok(updatedBlogPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getBlogPostById(@PathVariable Long id) throws Exception {
        BlogPost blogPost = blogPostService.getBlogPostById(id);
        return ResponseEntity.ok(blogPost);
    }

    @PostMapping("/{blogPostId}/tags/{tagId}")
    public ResponseEntity<Void> addTagToBlogPost(@PathVariable Long blogPostId, @PathVariable Long tagId) throws Exception {
        blogPostService.addTagToBlogPost(blogPostId, tagId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{blogPostId}/tags/{tagId}")
    public ResponseEntity<Void> removeTagFromBlogPost(@PathVariable Long blogPostId, @PathVariable Long tagId) throws Exception {
        blogPostService.removeTagFromBlogPost(blogPostId, tagId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<List<BlogPost>> getAllBlogPostsByTag(@PathVariable Long tagId) throws Exception {
        Tag tag = tagService.getTagById(tagId)
                .orElseThrow(() -> new Exception("Tag not found with id " + tagId));
        List<BlogPost> blogPosts = blogPostService.getAllBlogPostsByTag(tag);
        return ResponseEntity.ok(blogPosts);
    }

	@GetMapping("/destinations/{destinationId}")
	public ResponseEntity<List<BlogPost>> getAllBlogPostsByDestination(@PathVariable Long destinationId) throws Exception {
		Destination destination = destinationService.getDestinationById(destinationId);
		if (destination == null) {
			throw new Exception("Destination not found with id " + destinationId);
		}
		List<BlogPost> blogPosts = blogPostService.getAllBlogPostsByDestination(destination);
		return ResponseEntity.ok(blogPosts);
	}
}