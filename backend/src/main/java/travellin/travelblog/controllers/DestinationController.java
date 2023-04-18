package travellin.travelblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import travellin.travelblog.dto.DestinationDto;
import travellin.travelblog.services.BlogPostService;
import travellin.travelblog.services.DestinationService;

import java.util.List;

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
    public ResponseEntity<List<DestinationDto>> getAllDestinations() {
        try {
        List<DestinationDto> destinationDtos = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinationDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<DestinationDto> getDestinationById(@PathVariable Long id) throws Exception {
        try {
            DestinationDto destinationDto = destinationService.getDestinationById(id);
            return ResponseEntity.ok(destinationDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
    public ResponseEntity<DestinationDto> updateDestination(@PathVariable Long id, @RequestBody DestinationDto destinationDto) throws Exception {
        try {
            DestinationDto updatedDestination = destinationService.updateDestination(id, destinationDto);
            return ResponseEntity.ok(updatedDestination);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) throws Exception {
        try {
            blogPostService.removeDestinationfromBlogPosts(id);
            destinationService.deleteDestination(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
