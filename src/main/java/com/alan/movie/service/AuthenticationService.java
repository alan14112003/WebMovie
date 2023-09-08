package com.alan.movie.service;

import com.alan.movie.Model.Role;
import com.alan.movie.Model.User;
import com.alan.movie.config.JwtService;
import com.alan.movie.dto.AuthenticationResponse;
import com.alan.movie.dto.LoginRequest;
import com.alan.movie.dto.RegisterRequest;
import com.alan.movie.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  
  public AuthenticationResponse register(RegisterRequest request) {
    Optional<User> userEmailOptional = repository.findByEmail(request.getEmail());
    if (userEmailOptional.isPresent()) {
      throw new RuntimeException("User exits");
    }
    
    var user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse
        .builder()
        .token(jwtToken)
        .build();
  }
  
  public AuthenticationResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    
    var user = repository.findByEmail(request.getEmail()).orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken).build();
  }
  public AuthenticationResponse loginGoogle(HttpServletRequest request) throws GeneralSecurityException, IOException {
    NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
    // Khởi tạo jsonFactory
    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(Arrays.asList("173570859950-ld6n4nukbdhhgv8mfrof0bfr59fe0670.apps.googleusercontent.com"))
        .build();
    String authorization = request.getHeader("Authorization");
    
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new RuntimeException("Not Authorization in header");
    }
    
    String token = authorization.substring(7);
    
    GoogleIdToken idToken = verifier.verify(token);
    if (idToken == null) {
      throw new RuntimeException("Invalid ID token.");
    }
    Payload payload = idToken.getPayload();
    
    User user = repository.findByEmail(payload.getEmail()).orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken).build();
  }
}
