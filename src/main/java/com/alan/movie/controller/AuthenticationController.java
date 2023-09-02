package com.alan.movie.controller;


import com.alan.movie.dto.AuthenticationResponse;
import com.alan.movie.dto.LoginRequest;
import com.alan.movie.dto.RegisterRequest;
import com.alan.movie.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
      HttpServletRequest request
  ) throws GeneralSecurityException, IOException {
    return ResponseEntity.ok(authService.loginGoogle(request));
  }
  
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(
      @RequestBody LoginRequest request
  ) {
    return ResponseEntity.ok(authService.login(request));
  }
}
