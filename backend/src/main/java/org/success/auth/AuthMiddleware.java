package org.success.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.success.user.entities.User;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthMiddleware extends OncePerRequestFilter {

    @Autowired
    private JwtEssentials jwtEssentials;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtEssentials.getTokenFromServelet(request);
        if(token!=null && !token.isBlank()){
            if(!jwtEssentials.validateToken(token)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Optional<User> userOpt = jwtEssentials.getUserFromJWT(token);
            if(userOpt.isEmpty()){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            User user = userOpt.get();
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
