package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.DestinationDto;
import travellin.travelblog.services.BlogPostService;
import travellin.travelblog.services.DestinationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;
    private final BlogPostService blogPostService;

    @Autowired
    public DestinationController(DestinationService destinationService, BlogPostService blogPostService) {
        this.destinationService = destinationService;
        this.blogPostService = blogPostService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getAllDestinations() {
        try {
        List<DestinationDto> destinationDtos = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinationDtos);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getDestinationById(@PathVariable Long id) throws Exception {
        try {
            DestinationDto destinationDto = destinationService.getDestinationById(id);
            return ResponseEntity.ok(destinationDto);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);        }
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DestinationDto> createDestination(@RequestBody DestinationDto destinationDto) {
        try {
            DestinationDto createdDestination = destinationService.createDestination(destinationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDestination);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> updateDestination(@PathVariable Long id, @RequestBody DestinationDto destinationDto) throws Exception {
        try {
            DestinationDto updatedDestination = destinationService.updateDestination(id, destinationDto);
            return ResponseEntity.ok(updatedDestination);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteDestination(@PathVariable Long id) throws Exception {
        try {
            blogPostService.removeDestinationfromBlogPosts(id);
            destinationService.deleteDestination(id);
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", "Destination " + id + " deleted successfully.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorMap);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("Message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);        }
    }

}
