package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    //"@Transactional" Ela simplifica o gerenciamento de transações, permitindo que os desenvolvedores configurem regras de transação diretamente no código, em métodos ou classes.
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder){
        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    //O método de cadastrar possui diversos detalhes, porque precisamos devolver o código 201, o cabeçalho location com a URI e no corpo da resposta é necessário ter uma representação do recurso recém criado.

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        //a linha abaixo recebe o id do medico o acessa no DB.
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        //A atualização com os novos os novos dados no DB se dá de maneira automática pela JPA, devido à anotação @Transactional. Ela carrega a entidade e detecta a mudança de atributos, logo carrega automaticamente  modificação.
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.excluir();
        //repository.deleteById(id);
        return ResponseEntity.noContent().build();
        //o código 204 se refere à requisição processada e sem conteúdo.
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

}

//Pageable é utilizado quando se quer fazer o controle da quantidade de objetos por request, caso contrário, o repositorio pode fornecer informações sobre todas as entidade, o que poderia sobre carregar...
//Pageable é uma uma classe do Spring que cuida da paginação. **Cuidado, pois há outro objeto Pageable do Java.
//A classe Page já integra um map() e já é retornado um toList().
//"?size=1&page=1" são os parâmetros utilizados para controlar a paginação. (quantidade de elementos por pagina e o número da página. Por exemplo: "http://localhost:8080/medicos?size=1&page=1"
//Para ordenação usa-se o parâmetro "?sort=crm". Para fazer em ordem descrescente: ",desc" após o parâmentro. "?sort=crm,desc"
// O padrão do Spring é: 20 elemtnos por paginação, seguindo a ordem que está contida no DB.

