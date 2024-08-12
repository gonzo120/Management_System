package com.inn.cafe.cominncafe.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
    
    private String secret = "Gonzo120!"; // Define una clave secreta utilizada para firmar y validar los JWT.
    
    // Extrae el nombre de usuario del token JWT.
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject); // Llama a extractClaims y obtiene el "subject" del token (generalmente el nombre de usuario).
    }

    // Extrae la fecha de expiración del token JWT.
    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration); // Llama a extractClaims y obtiene la fecha de expiración del token.
    }

    // Método genérico para extraer una claim específica del token JWT.
    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token); // Extrae todos los claims del token.
        return claimsResolver.apply(claims); // Aplica la función claimsResolver para obtener una claim específica.
    }

    // Extrae todas las claims del token JWT.
    public Claims extractAllClaims(String token){
        // Usa Jwts para parsear el token y obtener las claims, utilizando la clave secreta para la verificación.
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // Verifica si el token JWT ha expirado.
    private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date()); // Compara la fecha de expiración del token con la fecha actual.
	}

    // Crea un nuevo token JWT con los claims proporcionados y el nombre de usuario como subject.
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims) // Establece los claims en el token.
				.setSubject(subject) // Establece el "subject" del token.
				.setIssuedAt(new Date(System.currentTimeMillis())) // Establece la fecha de emisión del token.
				.setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Establece la fecha de expiración (1 hora desde la emisión).
				.signWith(SignatureAlgorithm.HS256, secret).compact(); // Firma el token con el algoritmo HS256 y la clave secreta.
	}

    // Genera un token JWT para un usuario específico con un rol asociado.
	public String generateToken(String userName, String role) {
		Map<String, Object> claims = new HashMap<>(); // Crea un mapa para los claims.
		claims.put("role", role); // Añade el rol del usuario a los claims.
		return createToken(claims, userName); // Llama a createToken para generar el token.
	}

    // Valida el token JWT comparando el nombre de usuario y verificando si el token ha expirado.
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUsername(token); // Extrae el nombre de usuario del token.
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Verifica que el nombre de usuario coincida y que el token no haya expirado.
	}
}