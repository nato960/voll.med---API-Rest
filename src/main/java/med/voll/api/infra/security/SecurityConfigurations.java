package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//O "@EnableWebSecurity" indica ao Spring que as configurações de segurança serão personalizadas.
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    //Objeto e utilizado para configurar coisas relacionadas ao processo de autenticação.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/login").permitAll();
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
                    req.requestMatchers(HttpMethod.DELETE, "/medicos").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/pacientes").hasRole("ADMIN");
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
        //"csrf(csrf -> csrf.disable())" desativa o sistema de proteção contra Cross-Site Request Security (CSRF), pois o Token já proteje contra esse tipo de ataque. (seria reduntante deixa-lo ativado.)
        //".sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))" configura a autenticação de maneira STATELESS.
        //"@Bean" para expor o retorno do método, de modo que permita ao Spring a injetar o método em um controlelr ou uma classe service.
        //o bloco abaixo permite permite requests do tipo POST pelo URL "/login" sem a necessiade de autenticação.


        //"req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();" Esta linha permite acesso sem login para esses enderecos para possibilitar acesso à documentação gerada automaticamente pela dependência SpringDOC
        //Pois a documentação deve ser pública.
    }


    //O "@Bean" serve para exportar uma classe para o Spring, fazendo com que ele consiga carregá-la e realiza a sua injeção de dependência em outras classes.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

