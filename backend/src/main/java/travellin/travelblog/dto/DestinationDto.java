package travellin.travelblog.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import travellin.travelblog.entities.Destination;

@Data
@Builder
@AllArgsConstructor
public class DestinationDto {

    private Long id;
    private String name;
    //private String description;
    private String country;
    //private String region;

    public DestinationDto() {}

	public static DestinationDto fromEntity(Destination destination) {
		return new DestinationDto(
			destination.getId(),
			destination.getName(),
			//destination.getDescription(),
			destination.getCountry()
			//destination.getRegion()
		);
	}
	
	public static List<DestinationDto> fromEntityList(List<Destination> destinations) {
		List<DestinationDto> dtos = new ArrayList<>();
        for (Destination destination : destinations) {
            dtos.add(fromEntity(destination));
        }
        return dtos;
	}
}
