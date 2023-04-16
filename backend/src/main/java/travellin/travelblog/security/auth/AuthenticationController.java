package travellin.travelblog.security.auth;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
        AuthenticationResponse response = service.register(request);
        return ResponseEntity.ok(response);
    } catch (ResponseStatusException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getReason());
        return ResponseEntity.status(e.getStatusCode()).body(errorMap);
    }
  }

  @PostMapping("/register/admin")
  public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
    try {
      AuthenticationResponse response = service.registerAdmin(request);
      return ResponseEntity.ok(response);
    } catch (ResponseStatusException e) {
      Map<String, String> errorMap = new HashMap<>();
      errorMap.put("error", e.getReason());
      return ResponseEntity.status(e.getStatusCode()).body(errorMap);
    }
  }

  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
    try {
      AuthenticationResponse response = service.authenticate(request);
      return ResponseEntity.ok(response);
    } catch (ResponseStatusException e) {
      Map<String, String> errorMap = new HashMap<>();
      errorMap.put("error", e.getReason());
      return ResponseEntity.status(e.getStatusCode()).body(errorMap);
    }
  }

}
