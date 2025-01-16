package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

//O segundo parâmentro da interface JpaRepository é o tipo do atributo da chave primária, nesse caso é o Long do "id".
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    @Query("""
            select m
            from Medico m
            where
            m.ativo = true
            and
            m.especialidade = :especialidade
            and
            m.id not in(
                select c.medico.id from Consulta c
                where
                c.data = :data
                and
                c.motivoCancelamento is null
            )
            order by rand()
            limit 1
            """)
    Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);
    //A Query acima (em JPQL) consulta no DB um médico ATIVO de uma especialidade especificada, que esteja livre na data especificada (para isso é feita uma outra consulta, agora na tabela de consultas), de forma aleatória.


    @Query("""
            select m.ativo
            from Medico m
            where
            m.id = :id
            """)
    Boolean findAtivoById(Long id);
    //A Query acima faz uma consulta no DB, onde é carregado apenas o atributo "ativo".

}
