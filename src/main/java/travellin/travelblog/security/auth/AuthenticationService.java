package travellin.travelblog.security.auth;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import travellin.travelblog.entities.User;
import travellin.travelblog.repositories.UserRepository;
import travellin.travelblog.security.Role;
import travellin.travelblog.security.config.JwtService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

// import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
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
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
			request.getUsername(),
            request.getPassword()
        )
    );
    var user = repository.findByUsername(request.getUsername())
        .orElseThrow();

    Map<String, Object> claims = new HashMap<>();
    claims.put("authorities", user.getAuthorities()
    .stream()
    .map(GrantedAuthority::getAuthority)
    .collect(Collectors.toList()));
    claims.put("userId", user.getId());

    var jwtToken = jwtService.generateToken(claims, user);
    //var refreshToken = jwtService.generateRefreshToken(user);
    //revokeAllUserTokens(user);
    //saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

//   private void saveUserToken(User user, String jwtToken) {
//     var token = Token.builder()
//         .user(user)
//         .token(jwtToken)
//         .tokenType(TokenType.BEARER)
//         .expired(false)
//         .revoked(false)
//         .build();
//     tokenRepository.save(token);
//   }

//   private void revokeAllUserTokens(User user) {
//     var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//     if (validUserTokens.isEmpty())
//       return;
//     validUserTokens.forEach(token -> {
//       token.setExpired(true);
//       token.setRevoked(true);
//     });
//     tokenRepository.saveAll(validUserTokens);
//   }

//   public void refreshToken(
//           HttpServletRequest request,
//           HttpServletResponse response
//   ) throws IOException {
//     final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//     final String refreshToken;
//     final String userEmail;
//     if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//       return;
//     }
//     refreshToken = authHeader.substring(7);
//     userEmail = jwtService.extractUsername(refreshToken);
//     if (userEmail != null) {
//       var user = this.repository.findByEmail(userEmail)
//               .orElseThrow();
//       if (jwtService.isTokenValid(refreshToken, user)) {
//         var accessToken = jwtService.generateToken(user);
//         revokeAllUserTokens(user);
//         saveUserToken(user, accessToken);
//         var authResponse = AuthenticationResponse.builder()
//                 .accessToken(accessToken)
//                 .refreshToken(refreshToken)
//                 .build();
//         new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//       }
//     }
//   }
}
