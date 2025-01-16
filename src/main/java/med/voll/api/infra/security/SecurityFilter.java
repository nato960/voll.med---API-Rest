package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            //abaixo é a chamada para obter o usuário do respectivo token.
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repository.findByLogin(subject);
            //"cria o DTO que representa o usuário"
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            //"força a autorização"
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Logado na requisição");
        }

        filterChain.doFilter(request, response);

    }

    //Recupera o Token no cabeçalho da request.
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null){
                return authorizationHeader.replace("Bearer ", "").trim();
            }
            return null;

    }
}
//"OncePerRequestFilter" é utilizado para filtrar requisições HTTP antes de chegarem aos controladores da aplicação. Esse método garante que cada requisição será filtrada apenas uma vez durante o seu ciclo de vida.