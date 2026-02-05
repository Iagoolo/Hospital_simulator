# 🏥 Simulador de Sistema Hospitalar

> Um sistema de gerenciamento hospitalar via console, desenvolvido em Java com persistência em PostgreSQL. Focado em boas práticas de arquitetura e fluxo de atendimento completo.

## 📖 Sobre o Projeto

Este projeto simula o fluxo real de um pronto-atendimento hospitalar. Diferente de CRUDs simples, o sistema gerencia o **ciclo de vida do paciente** dentro do hospital, garantindo a integridade dos dados e a ordem correta dos processos (Recepção → Triagem → Consulta → Pós-Consulta → Alta).

O projeto foi desenvolvido com foco em **Arquitetura em Camadas** e **Injeção de Dependências**, garantindo um código modular e fácil de manter.

## 🚀 Funcionalidades Principais

### 🔄 Fluxo de Atendimento (Core)

1. **Recepção:** Abertura de ficha de atendimento para pacientes cadastrados.
2. **Triagem (Enfermaria):** Registro de sinais vitais (peso, temperatura) e definição de prioridade.
3. **Consulta Médica:** Alocação de médico e sala disponível.
4. **Pós-Consulta:**
    * Registro de diagnóstico.
    * **Prescrição Eletrônica:** Seleção de medicamentos do estoque.
    * **Solicitação de Exames:** Integração automática com histórico.
5. **Finalização:** Encerramento do atendimento e atualização automática do **Histórico Médico** do paciente.

### ⚙️ Gerenciamento (Backoffice)

* **Gestão de Pessoas:** CRUD completo de Pacientes, Médicos e Enfermeiros.
* **Gestão de Recursos:** Cadastro de Salas com validação de uso (impede deletar sala ocupada).
* **Farmácia:** Controle de estoque de medicamentos (Fórmula, Forma, Via de Administração).
* **Histórico Médico:** Log perpétuo de todas as consultas e procedimentos do paciente.

## 🛠 Tecnologias Utilizadas

* **Linguagem:** Java 17+
* **Banco de Dados:** PostgreSQL
* **Conexão:** JDBC (Java Database Connectivity)
* **Arquitetura:**
  * **DAO Pattern:** Para abstração do acesso a dados.
  * **Service Layer:** Para regras de negócio e validações.
  * **UI Composition:** Interface de terminal modularizada.

## 🏗 Estrutura do Banco de Dados

O sistema utiliza um banco relacional robusto. As principais tabelas são:

* `Paciente`, `Medico`, `Enfermeiro` (Atores)
* `Atendimento` (Controla o fluxo e o Status)
* `Triagem`, `Consulta` (Etapas do processo)
* `Medicamento`, `Prescricao`, `Item_Prescricao` (Farmácia)
* `Sala`, `Historico_Medico`

> **Nota:** O sistema utiliza transações (`commit`/`rollback`) para garantir que operações críticas (como deletar uma sala ou finalizar atendimento) sejam atômicas.

## 🏁 Como Executar

### Pré-requisitos

* Java JDK instalado.
* PostgreSQL instalado e rodando.
* Driver JDBC do PostgreSQL no classpath.

### Passo 1: Configurar o Banco

Crie um banco de dados chamado `hospital_db` e execute o script SQL de criação das tabelas (disponível na pasta `/SQL`).
