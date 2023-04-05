package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Destination;
import travellin.travelblog.entities.Image;
import travellin.travelblog.entities.Tag;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.BlogPostRepository;
import travellin.travelblog.repositories.TagRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
	private final TagRepository tagRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, TagRepository tagRepository) {
        this.blogPostRepository = blogPostRepository;
		this.tagRepository = tagRepository;
    }

    public BlogPost createBlogPost(String title, String content, User author, LocalDateTime createdAt, LocalDateTime updatedAt, List<Image> images, Destination destination, List<Tag> tags) {
        BlogPost blogPost = new BlogPost(title, content, createdAt, updatedAt, author, destination);
        blogPost.setImages(images);
        blogPost.setTags(tags);
        return blogPostRepository.save(blogPost);
    }

    public BlogPost updateBlogPost(Long id, String title, String content, LocalDateTime updatedAt, List<Image> images, Destination destination, List<Tag> tags) throws Exception {
		BlogPost blogPost = blogPostRepository.findById(id)
		.orElse(null);

	if (blogPost == null) {
		throw new Exception("BlogPost not found: " + id);
}        blogPost.setTitle(title);
        blogPost.setContent(content);
        blogPost.setUpdatedAt(updatedAt);
        blogPost.setImages(images);
        blogPost.setTags(tags);
        blogPost.setDestination(destination);
        return blogPostRepository.save(blogPost);
    }

    public List<BlogPost> getAllBlogPostsByUser(User author) {
        return blogPostRepository.findByAuthor(author);
    }

	public BlogPost getBlogPostById(Long id) throws Exception {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new Exception("BlogPost not found: " + id));
    }

	public void addTagToBlogPost(Long blogPostId, Long tagId) throws Exception {
		BlogPost blogPost = blogPostRepository.findById(blogPostId)
				.orElseThrow(() -> new Exception("Blog post not found: " + blogPostId));
	
		Tag tag = tagRepository.findById(tagId)
				.orElseThrow(() -> new Exception("Tag not found: " + tagId));
	
		blogPost.addTag(tag);
		blogPostRepository.save(blogPost);
	}
	
	public void removeTagFromBlogPost(Long blogPostId, Long tagId) throws Exception {
		BlogPost blogPost = blogPostRepository.findById(blogPostId)
				.orElseThrow(() -> new Exception("Blog post not found with id " + blogPostId));
	
		Tag tag = tagRepository.findById(tagId)
				.orElseThrow(() -> new Exception("Tag not found with id " + tagId));
	
		blogPost.removeTag(tag);
		blogPostRepository.save(blogPost);
	}
	
	public List<BlogPost> getAllBlogPostsByTag(Tag tag) {
		return blogPostRepository.findByTags(tag);
	}
	
	public List<BlogPost> getAllBlogPostsByDestination(Destination destination) {
		return blogPostRepository.findByDestination(destination);
	}
	
}
