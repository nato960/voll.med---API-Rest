package med.voll.api.domain.consulta;


import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorDeCancelamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    //Os repositório de Paciente e Médico precisaram ser injetados aqui em função da criação do objeto do tipo Consulta, que precisa do idMedico e idPaciente pelos respectivos repositórios.
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadoresAgendamento;
    //Na linha acima, o Spring detecta que uma lista está sendo injetada e que o generics é uma interface.
    // Logo, ele busca todas as classse que implemntam essa interface, cria uma lista essas classes e injeta a lista com cada uma delas.

    @Autowired
    private List<ValidadorDeCancelamentoDeConsulta> validadoresCancelamento;


    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados){
    if (!pacienteRepository.existsById(dados.idPaciente())){
        throw new ValidacaoException("Id do paciente informado não existe");
    }
        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Id do médico informado não existe");
        }

        validadoresAgendamento.forEach(v -> v.validar(dados));

        //A linha abaixo carrega o "paciente" no DB.
        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        if (medico == null) {
            throw new ValidacaoException("Não existe médico disponível nessa data!");
        }

        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }


    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }
        if (dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando o médico não for escolhido!");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());

    }

    public void cancelar(DadosCancelamentoConsulta dados){
        if(!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }
}


//"@Service" são para o reconheço de classe relacionadas à regras de negócio.
//Assim, aqui serão realizadas todas as regras de negócio.
//Em "var medico = medicoRepository.findById(dados.idMedico()).get();" foi necessário a introdução do".get()", pois estava dando erro de compilamento, pois o "findById()" retorna um optional.
//"getReferenceById()" é melhor usado quando se precisa apenas de uma referência, diferentemente do "findById()" que carrega o objeto por completo, gerando cosulta desnecessãria.
