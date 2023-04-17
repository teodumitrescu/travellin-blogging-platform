package travellin.travelblog.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.TagDto;
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
    public List<TagDto> getAllTags() {
        List<TagDto> tags = tagService.getAllTags();
        return tags;
    }

    @PostMapping("")
    public TagDto createTag(@RequestBody TagDto tagDto) {
        TagDto createdTag = tagService.createTag(tagDto);
        return createdTag;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        Optional<TagDto> tagOptional = tagService.getTagById(id);
        if (tagOptional.isPresent()) {
            TagDto tagDto = tagOptional.get();
            return ResponseEntity.ok(tagDto);
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

    // @GetMapping("/{id}/posts")
    // public List<BlogPostDto> getAllBlogPostsByTag(@PathVariable Long id) {
    //     Optional<Tag> tagOptional = tagService.getTagById(id);
    //     if (tagOptional.isPresent()) {
    //         Tag tag = tagOptional.get();
    //         List<BlogPostDto> blogPostDtos = BlogPostDto.fromEntityList(tag.getPosts());
    //         return blogPostDtos;
    //     } else {
    //         return Collections.emptyList();
    //     }
    // }
}
