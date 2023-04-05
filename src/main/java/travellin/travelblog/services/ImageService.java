package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Image;
import travellin.travelblog.repositories.ImageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private BlogPostService blogPostService;

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Image getImageById(Long id) throws Exception {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            return imageOptional.get();
        } else {
            throw new Exception("Image not found:" + id);
        }
    }

    public Image createImage(String filename, String url, String caption, String altText, Long postId) throws Exception {
        BlogPost post = blogPostService.getBlogPostById(postId);
        Image image = new Image(filename, url, caption, altText, post);
        return imageRepository.save(image);
    }

    public Image updateImage(Long id, String filename, String url, String caption, String altText, Long postId) throws Exception {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();

            BlogPost post = blogPostService.getBlogPostById(postId);

            if (filename != null) {
                image.setFilename(filename);
            }
            if (url != null) {
                image.setUrl(url);
            }
            if (caption != null) {
                image.setCaption(caption);
            }
            if (altText != null) {
                image.setAltText(altText);
            }
            if (post != null) {
                image.setPost(post);
            }

            return imageRepository.save(image);
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
