package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import travellin.travelblog.dto.ImageDto;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Image;
import travellin.travelblog.repositories.BlogPostRepository;
import travellin.travelblog.repositories.ImageRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImageService {

    private ImageRepository imageRepository;
    private BlogPostRepository blogPostRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, BlogPostRepository blogPostRepository) {
        this.imageRepository = imageRepository;
        this.blogPostRepository = blogPostRepository;
    }


    public List<ImageDto> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(ImageDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ImageDto getImageById(Long id) throws Exception {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            return ImageDto.fromEntity(image);
        } else {
            throw new Exception("Image not found: " + id);
        }
    }

    public ImageDto createImage(Long blogPostId, ImageDto imageDto) throws Exception {
        Optional<BlogPost> postOptional = blogPostRepository.findById(blogPostId);
        if (postOptional.isPresent()) {
            Image image = new Image(
                imageDto.getFilename(),
                imageDto.getUrl(),
                imageDto.getCaption(),
                imageDto.getAltText(),
                postOptional.get());
        Image createdImage = imageRepository.save(image);
        return ImageDto.fromEntity(createdImage);
        } else {
            throw new Exception("Post to create image for not found:  " + blogPostId);
        }
    }

    public ImageDto updateImage(Long id, ImageDto imageDto) throws Exception {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();

            if (imageDto.getPostId() != null) {
                BlogPost post = blogPostRepository.findById(imageDto.getPostId()).get();
                image.setPost(post);
            }

            if (imageDto.getFilename() != null) {
                image.setFilename(imageDto.getFilename());
            }
            if (imageDto.getUrl() != null) {
                image.setUrl(imageDto.getUrl());
            }
            if (imageDto.getCaption() != null) {
                image.setCaption(imageDto.getCaption());
            }
            if (imageDto.getAltText() != null) {
                image.setAltText(imageDto.getAltText());
            }

            Image updatedImage = imageRepository.save(image);
            return ImageDto.fromEntity(updatedImage);
        } else {
            throw new Exception("Image not found:" + id);
        }
    }

    public void deleteImage(Long id) throws Exception {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            imageRepository.delete(image);
        } else {
            throw new Exception("Image not found:" + id);
        }
    }

}
