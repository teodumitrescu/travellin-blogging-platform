package travellin.travelblog.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import travellin.travelblog.entities.Image;

@Data
@Builder
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String filename;
    private String url;
    private String caption;
    private String altText;
	private Long postId;

    public ImageDto() {}

	public static ImageDto fromEntity(Image image) {
		return new ImageDto(
				image.getId(),
				image.getFilename(),
				image.getUrl(),
				image.getCaption(),
				image.getAltText(),
				image.getPost().getId()
		);
	}
	
	public static List<ImageDto> fromEntityList(List<Image> images) {
		List<ImageDto> dtos = new ArrayList<>();
        for (Image image : images) {
            dtos.add(fromEntity(image));
        }
        return dtos;
	}
}
