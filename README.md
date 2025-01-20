# Voll Med - Sistema de Gerenciamento de Clínica Médica

Bem-vindo ao projeto **Voll Med**, uma aplicação desenvolvida para atender as necessidades de uma clínica médica fictícia. O objetivo principal é facilitar o monitoramento de cadastros de médicos e pacientes, além de gerenciar o agendamento e cancelamento de consultas

---

## :clipboard: Funcionalidades

- :man_health_worker: **Médicos:** Permite criar, visualizar, atualizar e excluir informações de médicos.
- :woman_health_worker: **Pacientes:** Permite criar, visualizar, atualizar e excluir informações de pacientes.
- :calendar: **Gerenciamento de Consultas:** Possibilita o agendamento e cancelamento de consultas médicas.

---

## :rocket: Tecnologias Utilizadas

- ``Spring Boot 3``: Framework para criação de aplicações web com Java.
- ``Java 17``: Linguagem de programação utilizada no desenvolvimento.
- ``Lombok``: Biblioteca para reduzir a verbosidade no código.
- ``MySQL``: Banco de dados relacional utilizado para armazenar as informações.
- ``JPA/Hibernate``: Frameworks para mapeamento objeto-relacional (ORM).
- ``Spring MVC``: Estrutura para construção de aplicações seguindo o padrão Model-View-Controller.
- ``Spring Security``: Solução para implementação de autenticação e autorização na aplicação.

---

## :wrench: Ferramentas Utilizadas

- ``Flyway``: Ferramenta para versionamento de esquema de banco de dados.
- ``Maven``: Gerenciador de dependências e build.
- ``Insomnia``: Ferramenta para testar APIs RESTful.
- ``Spring Doc/Swagger UI``: Geração automática de documentação interativa para a API.

---

## :arrow_forward: Como Executar o Projeto

Siga os passos abaixo para configurar e executar o projeto em seu ambiente local:

1. **Clone o repositório:**
   ```bash
   git clone <URL_DO_REPOSITORIO>
   ```

2. **Acesse o diretório do projeto:**
   ```bash
   cd voll-med
   ```

3. **Configure o banco de dados:**
   Atualize o arquivo <code>application.properties</code> ou <code>application.yml</code> com as credenciais do seu banco de dados MySQL.

4. **Execute as migrações do Flyway:**
   ```bash
   mvn flyway:migrate
   ```

5. **Inicie a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

6. **Teste a API:**
   Utilize o Insomnia ou outra ferramenta de sua preferência para interagir com os endpoints da aplicação. A documentação interativa estará disponível no Swagger UI em <code>http://localhost:8080/swagger-ui.html</code>.

---

## :file_folder: Estrutura do Projeto

A aplicação segue uma arquitetura em camadas:

- **Controller:** Responsável por gerenciar as requisições HTTP.
- **Domain:** Contém as entidades do domínio, a lógica de negócios e interfaces para interação com o banco de dados.
- **Infra:** Configurações de segurança.


---

## :handshake: Contribuindo

Contribuições são bem-vindas! Para contribuir, abra uma <i>issue</i> ou envie um <i>pull request</i> com melhorias ou correções.

---

