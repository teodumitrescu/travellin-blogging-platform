package travellin.travelblog.security.auth;

import lombok.RequiredArgsConstructor;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.UserRepository;
import travellin.travelblog.security.Role;
import travellin.travelblog.security.config.JwtService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var existingUser = repository.findByUsernameOrEmail(request.getUsername(), request.getEmail());
    if (existingUser != null) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or email already exists.");
    }
    
    var user = User.builder()
		.username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    repository.save(user);

    Map<String, Object> claims = new HashMap<>();
    claims.put("authorities", user.getAuthorities());
    claims.put("userId", user.getId());

    var jwtToken = jwtService.generateToken(claims, user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse registerAdmin(RegisterRequest request) {
    var existingUser = repository.findByUsernameOrEmail(request.getUsername(), request.getEmail());
    if (existingUser != null) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or email already exists.");
    }

    var user = User.builder()
		.username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.ADMIN)
        .build();
    repository.save(user);

    Map<String, Object> claims = new HashMap<>();
    claims.put("authorities", user.getAuthorities()
    .stream()
    .map(GrantedAuthority::getAuthority)
    .collect(Collectors.toList()));
    claims.put("userId", user.getId());

    var jwtToken = jwtService.generateToken(claims, user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials. Try again."));

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        claims.put("userId", user.getId());

        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    } catch (AuthenticationException e) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials. Try again.");
    }
}

}
