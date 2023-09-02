package com.alan.movie.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  
  private static final String SECRET_KEY = "6yQD5RwY8Eqme7CBUmyh7nALLd95DbpllLHLmGWE2wWHmnbKaHJk0dptFprYLXjSlIs9c+gn4BNwNNKGLxXiBXHEqxYcJ366XqjKNbQxHCZTPiCTm58WESOjVQXUcByt5I/B0LF/xcmAzYxm7GXuLOzlVr/x8ay1qkge4J7gnnzT0dtZxn4P/BbAqwaTnheJnuQDXOhZYLr3YdBo4/tMEtK8JF7kfrNK5WcTPNR4sdL4NKZO8OY/oB1x9A/tbBQADZh8QgatdxMwsDuqc9GKkwz18UHmm0sJ4jnXJcSDBqgDJGpEMlxEzACsAbvbYZYqiSy8WmcXihgan01IzIue+A==";
  
  public String generateToken(UserDetails userDetails) {
      return generateToken(new HashMap<>(), userDetails);
  }
  
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
      return Jwts.builder()
          .setClaims(extraClaims)
          .setSubject(userDetails.getUsername())
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis() + 3600000L * 24 * 365))
          .signWith(getSignInKey(), SignatureAlgorithm.HS256)
          .compact();
  }
  
  public boolean isValidToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
  
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }
  
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
  
  
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }
  
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }
  
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build().parseClaimsJws(token)
        .getBody();
  }
  
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
