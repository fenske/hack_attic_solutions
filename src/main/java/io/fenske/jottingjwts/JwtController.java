package io.fenske.jottingjwts;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class JwtController {

    private final AtomicReference<String> SECRET_KEY = new AtomicReference<>();
    private final CopyOnWriteArrayList<String> container = new CopyOnWriteArrayList<>();

    @PostMapping("/")
    public Object index(@RequestBody String jwt) {
        Claims claims = Jwts.parser()
                            .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY.get()))
                            .parseClaimsJws(jwt)
                            .getBody();
        final Object append = claims.get("append");
        if (append == null) {
            final Map<String, String> response = new HashMap<>();
            response.put("solution", String.join("", container));
            return response;
        }
        container.add(append.toString());
        return "Appended " + append;
    }

    @PostMapping("/configure")
    public String configure(@RequestBody String secret) {
        SECRET_KEY.set(secret);
        return "Updated secret";
    }
}