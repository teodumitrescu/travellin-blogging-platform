package travellin.travelblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travellin.travelblog.dto.DestinationDto;
import travellin.travelblog.entities.BlogPost;
import travellin.travelblog.entities.Destination;
import travellin.travelblog.repositories.DestinationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }
    
    public DestinationDto createDestination(DestinationDto destinationDto) {
        Optional<Destination> destinationOptional = destinationRepository.findByNameAndCountry(destinationDto.getName(), destinationDto.getCountry());
        if (destinationOptional.isPresent()) {
            return DestinationDto.fromEntity(destinationOptional.get());
        } else {
            Destination destination = new Destination(
                    destinationDto.getName(),
                    "",
                    destinationDto.getCountry(),
                    ""
            );
            List<BlogPost> blogPostList = new ArrayList<>();
            destination.setPosts(blogPostList);
            Destination createdDestination = destinationRepository.save(destination);
            return DestinationDto.fromEntity(createdDestination);
        }
    }
    
    public DestinationDto updateDestination(Long id, DestinationDto destinationDto) throws Exception {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new Exception("Destination not found with id " + id));
        
        if (destinationDto.getName() != null) {
            destination.setName(destinationDto.getName());
        }
        if (destinationDto.getCountry() != null) {
            destination.setCountry(destinationDto.getCountry());
        }
    
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

    public List<DestinationDto> getDestinationByCountryContaining(String name) throws Exception {
        List<Destination> destinations = destinationRepository.findAllByCountryContainingIgnoreCase(name);
        return DestinationDto.fromEntityList(destinations);
    }
    
    public void deleteDestination(Long id) throws Exception {
        try {
            Destination destination = destinationRepository.findById(id)
                    .orElseThrow(() -> new Exception("Destination not found with id " + id));
            destinationRepository.delete(destination);
        } catch (Exception e) {
            throw new Exception("Error deleting destination with id " + id, e);
        }
    }
}