package travellin.travelblog.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Tag;
import travellin.travelblog.services.TagService;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping("")
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        Optional<Tag> tagOptional = tagService.getTagById(id);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            return ResponseEntity.ok(tag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        try {
            tagService.deleteTag(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/posts")
    public List<BlogPost> getAllBlogPostsByTag(@PathVariable Long id) {
        Optional<Tag> tagOptional = tagService.getTagById(id);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            return tag.getPosts();
        } else {
            return Collections.emptyList();
        }
    }
}

