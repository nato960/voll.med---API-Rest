package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Para que o Spring carrega uma classe automaticamente, a classe deve ter uma anotação. Nesse caso, a anotação é "@Service", pois a classe está encarregada de realizar um servico. Aqui é o serviço de autenticação.
//"UserDetailsService" por meio dessa interface, o Spring identifica que essa classe é responsável pela autenticação.

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }
}
