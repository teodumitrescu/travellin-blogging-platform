package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.ImageDto;
import travellin.travelblog.services.ImageService;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<List<ImageDto>> getAllImages() {
        List<ImageDto> imageDtos = imageService.getAllImages();
        return ResponseEntity.ok(imageDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImageById(@PathVariable Long id) throws Exception {
        ImageDto imageDto = imageService.getImageById(id);
        return ResponseEntity.ok(imageDto);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<ImageDto> createImage(@PathVariable Long postId, @RequestBody ImageDto imageDto) throws Exception {
        ImageDto createdImage = imageService.createImage(postId, imageDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdImage);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<ImageDto> updateImageById(
    //         @PathVariable Long id,
    //         @RequestBody ImageDto imageDto
    // ) throws Exception {
    //     Image updatedImage = imageService.updateImage(
    //             id,
    //             imageDto.getFilename(),
    //             imageDto.getUrl(),
    //             imageDto.getCaption(),
    //             imageDto.getAltText()
    //     );
    //     ImageDto updatedImageDto = ImageDto.fromEntity(updatedImage);
    //     return ResponseEntity.ok(updatedImageDto);
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteImageById(@PathVariable Long id) throws Exception {
    //     imageService.deleteImage(id);
    //     return ResponseEntity.noContent().build();
    // }
}
