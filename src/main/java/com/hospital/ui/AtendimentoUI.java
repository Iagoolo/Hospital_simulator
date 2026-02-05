package com.hospital.ui;

import java.util.Scanner;

import com.hospital.model.Atendimento;
import com.hospital.model.Consulta;
import com.hospital.model.HistoricoMedico;
import com.hospital.service.AtendimentoService;
import com.hospital.service.ConsultaService;
import com.hospital.service.HistoricoMedicoService;
import com.hospital.service.MedicoService;
import com.hospital.service.PacienteService;
import com.hospital.utils.ConsoleUtil;

public class AtendimentoUI extends BaseUI {
    
    private AtendimentoService atendimentoService;
    private PacienteService pacienteService;
    private ConsultaService consultaService;
    private HistoricoMedicoService historicoMedicoService;
    private MedicoService medicoService;

    private TriagemUI triagemUI;
    private ConsultaUI consultaUI;

    public AtendimentoUI(Scanner scanner, 
                         AtendimentoService as, 
                         PacienteService ps, 
                         ConsultaService cs,
                         HistoricoMedicoService hs,
                         MedicoService ms,
                         TriagemUI tUI, 
                         ConsultaUI cUI) {
        
        super(scanner); 
        this.atendimentoService = as;
        this.pacienteService = ps;
        this.consultaService = cs;
        this.historicoMedicoService = hs;
        this.medicoService = ms;
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
        System.out.println("5. Finalizar Atendimento");
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
            case 5:
                finalizarAtendimento();
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
            if (pacienteService.buscarPacienteCpf(cpf) == null) {
                throw new Exception("Paciente não cadastrado. Cadastre-o primeiro."); 
            }

            Atendimento atendimento = new Atendimento();
            atendimento.setCpfPaciente(cpf);
            atendimento.setStatus("Aguardando Triagem");
            atendimento.setHoraAtendimento(java.sql.Time.valueOf(java.time.LocalTime.now()));
            atendimento.setSenha("P-" + (int)(Math.random() * 1000));

            atendimentoService.realizarAtendimento(atendimento);

        }, "Ficha de atendimento aberta com sucesso!", "Erro ao registrar chegada");
    }

    /**
     * Finaliza um atendimento e atualiza o histórico médico do paciente.
     * 
     * <p>Este método realiza as seguintes operações:</p>
     * <ul>
     *   <li>Solicita ao usuário o ID do atendimento a ser finalizado</li>
     *   <li>Valida se o atendimento existe e seu status atual</li>
     *   <li>Verifica se existe uma consulta associada ao atendimento</li>
     *   <li>Confirma se a consulta possui diagnóstico registrado</li>
     *   <li>Busca ou cria um novo histórico médico para o paciente</li>
     *   <li>Registra os dados da consulta no histórico (médico, diagnóstico, observações e data)</li>
     *   <li>Atualiza o status do atendimento para "Finalizado"</li>
     *   <li>Persiste as alterações no histórico médico no banco de dados</li>
     * </ul>
     * 
     * <p><strong>Validações realizadas:</strong></p>
     * <ul>
     *   <li>O atendimento deve existir no sistema</li>
     *   <li>O atendimento não pode já estar finalizado</li>
     *   <li>O atendimento deve possuir uma consulta associada</li>
     *   <li>A consulta deve ter um diagnóstico registrado</li>
     * </ul>
     * 
     * <p><strong>Comportamento:</strong></p>
     * <p>Se o paciente não possui histórico anterior, um novo será criado.
     * Caso contrário, o histórico existente será atualizado com os novos dados da consulta.
     * Todas as operações são envolvidas por tratamento de exceções que exibe mensagens
     * apropriadas em caso de erro.</p>
     * 
     * @see Atendimento
     * @see Consulta
     * @see HistoricoMedico
     * @see AtendimentoService
     * @see ConsultaService
     * @see HistoricoMedicoService
     * @see MedicoService
     */
    private void finalizarAtendimento() {
        System.out.println("\n--- 5. Finalizar Atendimento e Atualizar Histórico ---");
        
        System.out.print("Digite o ID do Atendimento que deseja finalizar: ");
        int idAtendimento = ConsoleUtil.lerInt(scanner);

        executarAcao(() -> {
            Atendimento atendimento = atendimentoService.buscarAtendimentoPorId(idAtendimento);

            if (atendimento == null) {
                 throw new Exception("Atendimento não encontrado.");
            }
            if ("Finalizado".equals(atendimento.getStatus())) {
                throw new Exception("Este atendimento já foi finalizado.");
            }
            
            if (atendimento.getIdConsulta() == null || atendimento.getIdConsulta() == 0) {
                throw new Exception("Este atendimento não pode ser finalizado pois não possui uma consulta registrada.");
            }

            Consulta consulta = consultaService.procurarConsultaId(atendimento.getIdConsulta());
            if (consulta == null) {
                throw new Exception("Erro: Consulta associada ao atendimento não encontrada.");
            }
            if (consulta.getDiagnostico() == null || consulta.getDiagnostico().isEmpty()) {
                throw new Exception("A consulta ainda não possui diagnóstico. Registre o pós-consulta primeiro.");
            }

            HistoricoMedico historico = historicoMedicoService.buscarHistorico(consulta.getCpfPaciente());
            boolean historicoNovo = false;
            if (historico == null) {
                historico = new HistoricoMedico();
                historico.setCpfPaciente(consulta.getCpfPaciente());
                historico.setStatusHistorico("Ativo");
                historico.setObservacoes("");
                historicoNovo = true;
            }

            String novoRegistroHistorico = String.format(
                "\n--- REGISTRO DE CONSULTA %s ---" +
                "\nMédico: %s (CPF: %s)" +
                "\nDiagnóstico: %s" +
                "\nObservações: %s" +
                "\n-------------------------------------",
                consulta.getDataConsulta(),
                medicoService.buscarMedicoCpf(consulta.getCpfMedico()).getNome(),
                consulta.getCpfMedico(),
                consulta.getDiagnostico(),
                consulta.getObservacao()
            );

            historico.setObservacoes(historico.getObservacoes() + novoRegistroHistorico);

            atendimentoService.finalizarAtendimento(idAtendimento);
            
            if (historicoNovo) {
                historicoMedicoService.cadastrarHistorico(historico);
            } else {
                historicoMedicoService.atualizarHistorico(historico.getIdHistorico(), historico.getObservacoes());
            }

        }, "Atendimento finalizado e histórico atualizado com sucesso!", "Erro ao finalizar atendimento");
    }
}