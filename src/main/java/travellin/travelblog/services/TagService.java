package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Tag;
import travellin.travelblog.repositories.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    public void deleteTag(Long id) throws Exception {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            tagRepository.delete(tag);
        } else {
            throw new Exception("Tag not found: " + id);
        }
    }

    public List<BlogPost> getAllBlogPostsByTag(Tag tag) {
        return tag.getPosts();
    }
}
