# Sistema de Controle de Vacinação

Este projeto é uma aplicação web desenvolvida em **Spring Boot** com **Thymeleaf**, **TailwindCSS** e **HTMX**, destinada ao gerenciamento do processo de vacinação. Ele implementa funcionalidades de cadastro, listagem, filtros e segurança de acesso, seguindo boas práticas de desenvolvimento e arquitetura.

## Tecnologias Utilizadas
- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA (Hibernate)**
- **Thymeleaf** (templates dinâmicos)
- **HTMX** (atualizações parciais de página)
- **TailwindCSS** (estilização responsiva)
- **PostgreSQL** (banco de dados relacional)
- **SweetAlert2** (notificações amigáveis)

## 📂 Estrutura do Projeto
- `controller/` → Controladores para Pessoa, Usuário e Vacina.  
- `model/` → Entidades JPA (Pessoa, Vacina, Usuário, Papel).  
- `repository/` → Repositórios JPA e consultas customizadas.  
- `service/` → Regras de negócio (cadastro, validações, autenticação).  
- `validation/` → Validações customizadas de atributos.  
- `formatter/` → Conversores e formatadores (datas, números, big decimals).  
- `config/` → Configuração de segurança e logging.  

## 🔑 Funcionalidades
- Cadastro, edição e exclusão de **Pessoas** e **Vacinas**.  
- Autenticação e autorização com **usuários e papéis**.  
- Filtros e paginação para listagem de registros.  
- Tratamento global de exceções e mensagens amigáveis.  
- Layout moderno e responsivo com **Tailwind**.  
- Notificações com **SweetAlert2** integradas ao HTMX.  
