package com.hospital.ui;

import java.sql.SQLException;
import java.util.Scanner;

import com.hospital.model.Atendimento;
import com.hospital.service.AtendimentoService;
import com.hospital.service.PacienteService;
import com.hospital.service.ServiceContainer;
import com.hospital.utils.ConsoleUtil;

public class AtendimentoUI extends BaseUI {
    
    private ServiceContainer services;

    private TriagemUI triagemUI;
    private ConsultaUI consultaUI;

    public AtendimentoUI(Scanner scanner, 
                         ServiceContainer services,
                         TriagemUI tUI, 
                         ConsultaUI cUI) {
        
        super(scanner); 
        this.services = services;
        this.triagemUI = tUI;
        this.consultaUI = cUI;
    }

    @Override
    protected String obterTituloMenu(){
        return "Fluxo de Atendimento";
    }

    @Override
    protected void imprimirOpcoes(){
        System.out.println("1. Registrar chegada (Recepção)");
        System.out.println("2. Realizar Triagem");
        System.out.println("3. Iniciar Consulta");
        System.out.println("4. Registrar Pós-Consulta");
        System.out.println("0. Voltar ao Menu Principal");
    }

    @Override
    protected boolean processarOpcao(int opcao){
        switch (opcao) {
            case 1:
                registrarChegada();
                break;
            case 2:
                triagemUI.realizarTriagemWizard();
                break;
            case 3:
                consultaUI.iniciarConsulta();
                break;
            case 4:
                consultaUI.registrarPosConsulta();
                break;
            case 0:
                return false;
            default:
                System.out.println("Opção inválida.");
        }
        return true;
    }

    /**
     * Registra a chegada de um paciente no sistema de atendimento hospitalar.
     * 
     * Este método solicita ao usuário o CPF do paciente e verifica se o mesmo está cadastrado
     * no sistema. Caso o paciente não seja encontrado, uma exceção é lançada com mensagem
     * informativa. Se o paciente existir, um novo registro de atendimento é criado com as
     * seguintes informações:
     * <ul>
     *   <li>CPF do paciente fornecido</li>
     *   <li>Status inicial: "Aguardando Triagem"</li>
     *   <li>Hora do atendimento: hora atual do sistema</li>
     *   <li>Senha de atendimento: gerada aleatoriamente com prefixo "P-"</li>
     * </ul>
     * 
     * O método utiliza a estratégia de execução de ação com tratamento de exceções para
     * garantir que erros sejam capturados e mensagens apropriadas sejam exibidas ao usuário.
     * 
     * @see Atendimento
     * @see AtendimentoService#realizarAtendimento(Atendimento)
     * @see PacienteService#buscarPacienteCpf(String)
     * 
     * @throws Exception se o paciente com o CPF fornecido não estiver cadastrado no sistema
     * @throws SQLException se ocorrer erro ao acessar a base de dados durante o registro
     */
    private void registrarChegada() {
        System.out.println("\n--- 1. Registrar Chegada ---");
        System.out.print("Digite o CPF do paciente: ");
        String cpf = ConsoleUtil.lerString(scanner);

        executarAcao(() -> { 
            if (services.pacienteService.buscarPacienteCpf(cpf) == null) {
                throw new Exception("Paciente não cadastrado. Cadastre-o primeiro."); 
            }

            Atendimento atendimento = new Atendimento();
            atendimento.setCpfPaciente(cpf);
            atendimento.setStatus("Aguardando Triagem");
            atendimento.setHoraAtendimento(java.sql.Time.valueOf(java.time.LocalTime.now()));
            atendimento.setSenha("P-" + (int)(Math.random() * 1000));

            services.atendimentoService.realizarAtendimento(atendimento);

        }, "Ficha de atendimento aberta com sucesso!", "Erro ao registrar chegada");
    }
}