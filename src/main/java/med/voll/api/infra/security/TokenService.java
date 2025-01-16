package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Essa classe faz a validação e geração dos tokens, qualquer coisa relacionada a tokens.
@Service
public class TokenService {

    private static final String ISSUER = "API Voll.med";

    //@Value do Spring!
    @Value("${api.security.token.secret}")
    private String secret;

    //método para criação do Token.
    public String gerarToken(Usuario usuario){
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar o token jwt", exception);
        }
    }

    //Faz a validação do Token e entrega o subject (usuário).
    public String getSubject(String tokenJWT){
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token inválido ou expirado! " +tokenJWT);
        }
    }

    //O método abaixo é privado, pois só deve ser chamado por essa classe.
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
//"HMAC256" é um algoritmo para gerar a assinatura do Token, e lega uma senha como parâmetro para o método.
//".withIssuer(" ")" identica a ferramente "dona" do token.
//O método "withClaim()" recebe dois parâmetros, sendo o primeiro uma String que identifica o nome do claim (propriedade armazenada no token), e o segundo a informação que se deseja armazenar.