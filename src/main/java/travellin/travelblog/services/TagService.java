package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travellin.travelblog.dto.TagDto;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Tag;
import travellin.travelblog.repositories.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagDto> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(TagDto::fromEntity)
                .collect(Collectors.toList());
    }

    public TagDto createTag(TagDto tagDto) {
        List<BlogPost> emptyBlogPostList = new ArrayList<>();

        Tag tag = new Tag(tagDto.getName());
        tag.setPosts(emptyBlogPostList);

        tag = tagRepository.save(tag);
        return TagDto.fromEntity(tag);
    }

    public Optional<TagDto> getTagById(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        return tagOptional.map(TagDto::fromEntity);
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
}
