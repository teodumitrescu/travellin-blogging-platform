package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import travellin.travelblog.dto.DestinationDto;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Destination;
import travellin.travelblog.repositories.DestinationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }
    
    public DestinationDto createDestination(DestinationDto destinationDTO) {
        Destination destination = new Destination(
                destinationDTO.getName(),
                destinationDTO.getDescription(),
                destinationDTO.getCountry(),
                destinationDTO.getRegion()
        );
        List<BlogPost> emptyBlogPostList = new ArrayList<>();
        destination.setPosts(emptyBlogPostList);
        Destination createdDestination = destinationRepository.save(destination);
        return DestinationDto.fromEntity(createdDestination);
    }
    
    public DestinationDto updateDestination(Long id, DestinationDto destinationDTO) throws Exception {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new Exception("Destination not found with id " + id));
    
        destination.setName(destinationDTO.getName());
        destination.setDescription(destinationDTO.getDescription());
        destination.setCountry(destinationDTO.getCountry());
        destination.setRegion(destinationDTO.getRegion());
    
        Destination savedDestination = destinationRepository.save(destination);
        return DestinationDto.fromEntity(savedDestination);
    }
    
    public List<DestinationDto> getAllDestinations() {
        List<Destination> destinations = destinationRepository.findAll();
        return DestinationDto.fromEntityList(destinations);
    }
    
    public DestinationDto getDestinationById(Long id) throws Exception {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new Exception("Destination not found with id " + id));
        return DestinationDto.fromEntity(destination);
    }
    
    // public List<BlogPost> getBlogPostsForDestination(Long destinationId) throws Exception {
    //     Optional<Destination> destination = destinationRepository.findById(destinationId);
    //     if (destination.isPresent()) {
    //         return destination.get().getPosts();
    //     } else {
    //         throw new Exception("Destination not found with id " + destinationId);
    //     }
    // }
    
    public List<DestinationDto> getDestinationsByCountry(String country) {
        List<Destination> destinations = destinationRepository.findByCountry(country);
        return DestinationDto.fromEntityList(destinations);
    }
    
    public List<DestinationDto> getDestinationsByRegion(String region) {
        List<Destination> destinations = destinationRepository.findByRegion(region);
        return DestinationDto.fromEntityList(destinations);
    }
    
    public List<DestinationDto> searchDestinations(String keyword) {
        List<Destination> destinations = destinationRepository.findByNameContainingIgnoreCase(keyword);
        return DestinationDto.fromEntityList(destinations);
    }
    
    public void deleteDestination(Long id) throws Exception {
        try {
            Destination destination = destinationRepository.findById(id)
                    .orElseThrow(() -> new Exception("Destination not found with id " + id));
    
            destinationRepository.delete(destination);
        } catch (DataAccessException e) {
            throw new Exception("Error deleting destination with id " + id, e);
        }
    }
}