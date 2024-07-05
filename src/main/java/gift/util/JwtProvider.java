package gift.util;

import gift.dto.MemberDTO;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private final long accessTokenExpirationTime;

    public JwtProvider(@Value("${jwt.access_token_expiration_time}") long accessTokenExpirationTime) {
        this.accessTokenExpirationTime = accessTokenExpirationTime;
    }

    public String createAccessToken(MemberDTO memberDTO) {
        Date now = new Date();
        return Jwts.builder()
            .claim("email", memberDTO.email())
            .expiration(new Date(now.getTime() + accessTokenExpirationTime))
            .signWith(secretKey)
            .compact();
    }
}