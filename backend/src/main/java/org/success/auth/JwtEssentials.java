package org.success.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.success.user.entities.User;
import org.success.user.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class JwtEssentials {


    private String secret = "JI3J0F02R3F9K023KF230GK5I3GIH";

    @Autowired
    private UserRepository userRepository;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(LocalDateTime.now().plusWeeks(1).toInstant(ZoneOffset.of("-03:00")))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String getTokenFromServelet(HttpServletRequest request){
        return request.getHeader("token");
    }

    public Boolean validateToken(String jwt){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException exception){
            return false;
        }
    }

    public Optional<User> getUserFromJWT(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // Use the same secret used for signing
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")  // Certifique-se de que o emissor seja o mesmo usado na criação do token
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            String username = jwt.getSubject();
            if(username == null || username.isBlank()){
                return Optional.empty();
            }else{
                return userRepository.findByUsername(username);
            }
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error while verifying token", exception);
        }
    }

}
