package travellin.travelblog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.BlogPostDto;
import travellin.travelblog.dto.TagDto;
import travellin.travelblog.services.BlogPostService;
import travellin.travelblog.services.TagService;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final BlogPostService blogPostService;

    @Autowired
    public TagController(TagService tagService, BlogPostService blogPostService) {
        this.tagService = tagService;
        this.blogPostService = blogPostService;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<TagDto>> getAllTags() {
        try {
        List<TagDto> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        try {
            TagDto createdTag = tagService.createTag(tagDto);
            return ResponseEntity.ok(createdTag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        try {
            TagDto tagDto = tagService.getTagById(id);
            return ResponseEntity.ok(tagDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<TagDto> updateTagById(
            @PathVariable Long id,
            @RequestBody TagDto tagDto
    ) throws Exception {
        try {
        TagDto updatedTag = tagService.updateTag(id, tagDto);
        return ResponseEntity.ok(updatedTag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        try {
            for (BlogPostDto post : tagService.getPostsById(id)) {
                blogPostService.removeTagFromBlogPost(post.getId(), id);
            }
            tagService.deleteTag(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
