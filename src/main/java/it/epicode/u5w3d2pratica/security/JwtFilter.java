package it.epicode.u5w3d2pratica.security;


import it.epicode.u5w3d2pratica.exception.NotFoundException;
import it.epicode.u5w3d2pratica.exception.UnauthorizedException;
import it.epicode.u5w3d2pratica.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException
                    ("Token non presente, non sei autorizzato ad usare il servizio richiesto");
        } else {
            String token = authorization.substring(7);

            jwtTool.validateToken(token);

            try {
                User user = jwtTool.getUserFromToken(token);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(user,
                                null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (NotFoundException e) {
                throw new UnauthorizedException("Utente collegato al token non trovato");
            }

            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
    }

