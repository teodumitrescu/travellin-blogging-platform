package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.ImageDto;
import travellin.travelblog.services.BlogPostService;
import travellin.travelblog.services.ImageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;
    private final BlogPostService blogPostService;

    @Autowired
    public ImageController(ImageService imageService, BlogPostService blogPostService) {
        this.imageService = imageService;
        this.blogPostService = blogPostService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ImageDto>> getAllImages() {
        try {
            List<ImageDto> imageDtos = imageService.getAllImages();
            return ResponseEntity.ok(imageDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        try {
            ImageDto imageDto = imageService.getImageById(id);
            return ResponseEntity.ok(imageDto);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }
    }

    @PostMapping("/post={postId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> createImage(@PathVariable Long postId, @RequestBody ImageDto imageDto) {
        try {
            ImageDto createdImage = imageService.createImage(postId, imageDto);
            blogPostService.addImageToBlogPost(postId, createdImage.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdImage);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> updateImageById(
            @PathVariable Long id,
            @RequestBody ImageDto imageDto
    ) throws Exception {
        try {
        ImageDto updatedImage = imageService.updateImage(id, imageDto);
        return ResponseEntity.ok(updatedImage);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> deleteImageById(@PathVariable Long id) throws Exception {
        try {
            Long postId = imageService.getImageById(id).getPostId();
            blogPostService.removeImageFromBlogPost(postId, id);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", "Image " + id + " deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorMap);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);        }
    }
}
