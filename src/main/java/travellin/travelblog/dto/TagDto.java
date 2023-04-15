package travellin.travelblog.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import travellin.travelblog.entities.Tag;

@Data
@Builder
@AllArgsConstructor
public class TagDto {
    private Long id;
    private String name;

    public TagDto() {}

	public static TagDto fromEntity(Tag tag) {
		return new TagDto(tag.getId(), tag.getName());
	}

	public static List<TagDto> fromEntityList(List<Tag> tags) {
        List<TagDto> dtos = new ArrayList<>();
        for (Tag tag : tags) {
            dtos.add(fromEntity(tag));
        }
        return dtos;
    }
}
