package com.alan.movie.controller;


import com.alan.movie.dto.AuthenticationResponse;
import com.alan.movie.dto.LoginGoogleRequest;
import com.alan.movie.dto.LoginRequest;
import com.alan.movie.dto.RegisterRequest;
import com.alan.movie.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  
  private final AuthenticationService authService;
  
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
      return ResponseEntity.ok(authService.register(request));
  }
  
  @PostMapping("/login-google")
  public ResponseEntity<AuthenticationResponse> loginGoogle(
      @RequestBody LoginGoogleRequest loginGoogleRequest
      ) throws GeneralSecurityException, IOException {
    return ResponseEntity.ok(authService.loginGoogle(loginGoogleRequest));
  }
  
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(
      @RequestBody LoginRequest request
  ) {
    return ResponseEntity.ok(authService.login(request));
  }
}
