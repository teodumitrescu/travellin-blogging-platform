package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.entities.Image;
import travellin.travelblog.services.ImageService;
import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) throws Exception {
        Image image = imageService.getImageById(id);
        return ResponseEntity.ok(image);
    }

    @PostMapping
    public ResponseEntity<Image> createImage(@RequestBody Image image) throws Exception {
        Image createdImage = imageService.createImage(
            image.getFilename(),
            image.getUrl(),
            image.getCaption(),
            image.getAltText(),
            image.getPost().getId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Image> updateImageById(
        @PathVariable Long id,
        @RequestBody Image image
    ) throws Exception {
        Image updatedImage = imageService.updateImage(
            id,
            image.getFilename(),
            image.getUrl(),
            image.getCaption(),
            image.getAltText(),
            image.getPost().getId()
        );
        return ResponseEntity.ok(updatedImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImageById(@PathVariable Long id) throws Exception {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
