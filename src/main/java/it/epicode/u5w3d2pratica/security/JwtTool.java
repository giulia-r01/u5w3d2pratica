package it.epicode.u5w3d2pratica.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.epicode.u5w3d2pratica.exception.NotFoundException;
import it.epicode.u5w3d2pratica.model.User;
import it.epicode.u5w3d2pratica.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {

    @Value("${jwt.duration}")
    private Long durata;
    @Value("${jwt.secret}")
    private String chiaveSegreta;

    @Autowired
    private UserService userService;

    public String createToken(User user){

        return Jwts.builder().issuedAt(new Date()).
                expiration(new Date(System.currentTimeMillis()+durata))
                .subject(user.getId()+"").signWith(Keys.
                        hmacShaKeyFor(chiaveSegreta.getBytes()))
                .compact();
    }

    public void validateToken(String token){
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes()))
                .build().parse(token);
    }

    public User getUserFromToken(String token) throws NotFoundException {
        int id = Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes()))
                .build().parseSignedClaims(token).getPayload().getSubject());

        return userService.getUser(id);
    }
}
