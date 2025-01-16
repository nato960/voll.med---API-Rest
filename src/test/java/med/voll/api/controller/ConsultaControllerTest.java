package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {
//"@AutoConfigureMockMvc" é necessário para possibilitar a injeção do MockMvc.

    //A classe abaixo simula uma requisição no padrão MVC.
    @Autowired
    private MockMvc mvc;

    //Para simular o JSON de entrada no método (Cenário 2). Além disso, o objeto converte o objeto (nesse caso, DadosAgendamentoConsulta) em um JSON.
    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

    //Para simular o JSON de saída no método (Cenário 2). Além disso, o objeto converte o objeto (nesse caso, DadosDetalhamentoConsulta) em um JSON.
    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @MockitoBean
    private AgendaDeConsultas agendaDeConsultas;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    //"@WithMockUser" simula um usuário logado. Pois o método precisa de autenticação, mas não quer passar usuário ou não quer testar segurança.
    void agendar_cenario1() throws Exception {
        var response = mvc.perform(post("/consultas"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        //O bloco de código acima é o método utilizado para testar o código 400: primeiro, é disparada uma requisição para o endereço "/consultas" via método POST,
        // sem levar nenhum corpo; na sequência, jogamos o resultado (.andReturn()) e o getResponse() em uma variável e verificamos se o status do response é 400,
        // pois nesse cenário, esse é o erro que deve acontecer.

    }

    //Cenário abaixo tem como resposta ódigo 400, porém sem sucesso de correção...
    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas")
    @WithMockUser
    void agendar_cenario2() throws Exception {
        //"@WithMockUser" simula um usuário logado. Pois o método precisa de autenticação, mas não quer passar usuário ou não quer testar segurança.

        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.ORTOPEDIA;

        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 1l, 1l, data);
        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);

        var response = mvc
                .perform(
                        post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dadosAgendamentoConsultaJson.write(
                                        new DadosAgendamentoConsulta(1l, 1l,data, especialidade)
                                ).getJson())
                )
                .andReturn().getResponse();
        //Em "new DadosAgendamentoConsulta(1l, 1l,data, especialidade)" os parâmentros idMedico e idPaciente precisão de um l após os algarismos para serem reconhecidos como do tipo Long.

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                dadosDetalhamento
                                ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
        //Espera-se um json com id de consulta = null, porém não vem null....
    }



}