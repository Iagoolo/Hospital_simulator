package com.hospital.ui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import com.hospital.model.*;
import com.hospital.service.*;
import com.hospital.utils.ConsoleUtil;

public class ConsultaUI extends BaseUI {

    private ConsultaService consultaService;
    private AtendimentoService atendimentoService;
    private MedicoService medicoService;
    private SalaService salaService;
    private MedicamentosService medicamentosService;
    private PrescricaoService prescricaoService;
    private HistoricoMedicoService historicoMedicoService;
    private ExamesService examesService;

    public ConsultaUI(Scanner scanner, 
                      ConsultaService cs, 
                      AtendimentoService as, 
                      MedicoService ms, 
                      SalaService ss,
                      PrescricaoService ps,      
                      ExamesService es,            
                      MedicamentosService meds,    
                      HistoricoMedicoService hs) { 
        super(scanner);
        this.consultaService = cs;
        this.atendimentoService = as;
        this.medicoService = ms;
        this.salaService = ss;
        this.prescricaoService = ps;     
        this.examesService = es;        
        this.medicamentosService = meds; 
        this.historicoMedicoService = hs; 
    }

    /**
     * Inicia uma nova consulta para o próximo paciente aguardando atendimento.
     * Este método busca o próximo atendimento na fila, verifica se o médico e a sala
     * informados existem, e então cria uma nova consulta associando o paciente, médico,
     * e sala. Atualiza o status do atendimento para "Em Consulta" e registra a consulta
     * no sistema.
     *
     * @throws Exception se não houver pacientes aguardando consulta, se o médico não for encontrado,
     *                   ou se a sala não for encontrada.
     */
    public void iniciarConsulta() {
        System.out.println("\n--- Iniciar Próxima Consulta ---");
        
        executarAcao(() -> {
            Atendimento atendimento = atendimentoService.buscarProximoParaConsulta();
            if (atendimento == null) {
                throw new Exception("Nenhum paciente aguardando consulta");
            }
            System.out.println("Chamando paciente (CPF): " + atendimento.getCpfPaciente());

            System.out.print("CPF do Médico: ");
            String cpfMedico = ConsoleUtil.lerString(scanner);
            if (medicoService.buscarMedicoCpf(cpfMedico) == null){
                throw new Exception("Médico com CPF " + cpfMedico + " não encontrado.");
            }

            System.out.print("ID da Sala: ");
            int idSala = ConsoleUtil.lerInt(scanner);
            if (salaService.buscarSala(idSala) == null){
               throw new Exception("Sala com ID " + idSala + " não encontrada.");
            }

            Consulta consulta = new Consulta();
            consulta.setCpfPaciente(atendimento.getCpfPaciente());
            consulta.setCpfMedico(cpfMedico);
            consulta.setSala(idSala);
            consulta.setIdTriagem(atendimento.getIdTriagem());
            consulta.setDataConsulta(LocalDate.now());
            consulta.setHoraConsulta(LocalTime.now());

            Consulta consultaSalva = consultaService.criarConsulta(consulta);

            atendimento.setStatus("Em Consulta");
            atendimento.setIdConsulta(consultaSalva.getIdConsulta());
            atendimento.setIdSala(idSala);

            atendimentoService.atualizarAtendimento(atendimento);

        }, "Consulta iniciada! Paciente encaminhado para a sala.", "Erro ao iniciar consulta");
    }

    /**
     * Registra os detalhes da pós-consulta, incluindo diagnóstico, observações adicionais,
     * prescrições e exames para uma consulta específica.
     * 
     * Este método exibe uma lista de consultas em andamento sem diagnóstico, permitindo que o usuário
     * selecione uma pelo seu ID. Ele solicita o diagnóstico e observações adicionais, atualiza
     * o registro da consulta e fornece opções para adicionar prescrições ou solicitar exames.
     * 
     * Se não houver consultas pendentes, uma mensagem de aviso é exibida. O método trata
     * exceções SQL que podem ocorrer durante operações no banco de dados.
     * 
     * O usuário pode escolher:
     * 1. Adicionar uma prescrição de medicamento.
     * 2. Solicitar um exame.
     * 0. Concluir o processo de pós-consulta.
     * 
     * @throws SQLException se houver um erro ao acessar o banco de dados ao recuperar ou atualizar dados da consulta.
     */
    public void registrarPosConsulta() {
        System.out.println("\n--- 4. Registrar Pós-Consulta (Diagnóstico/Exames/Prescrições) ---");
        
        try {

            System.out.println("Consultas em Andamento (Sem Diagnóstico):");
            List<Consulta> pendentes = consultaService.listarConsultasPendentes();
            
            if (pendentes.isEmpty()) {
                System.out.println("[Aviso] Não há consultas pendentes no momento.");
            } else {
                System.out.printf("%-5s | %-15s | %-15s%n", "ID", "Paciente (CPF)", "Médico (CPF)");
                System.out.println("-".repeat(45));
                for (Consulta c : pendentes) {
                    System.out.printf("%-5d | %-15s | %-15s%n", 
                        c.getIdConsulta(), c.getCpfPaciente(), c.getCpfMedico());
                }
                System.out.println("-".repeat(45));
            }

            System.out.print("Digite o ID da Consulta para registrar o Pós-Consulta: ");
            int idConsulta = ConsoleUtil.lerInt(scanner);

            Consulta consulta = consultaService.procurarConsultaId(idConsulta);
            if (consulta == null) {
                System.out.println("Consulta não encontrada.");
                return;
            }

            System.out.println("\nPaciente: " + consulta.getCpfPaciente());
            System.out.print("Diagnóstico: ");
            String diagnostico = ConsoleUtil.lerString(scanner);
            System.out.print("Observações Adicionais: ");
            String observacoes = scanner.nextLine(); // Permite observações em branco

            consulta.setDiagnostico(diagnostico);
            consulta.setObservacao(observacoes);

            executarAcao(() -> {
                consultaService.atualizarConsulta(consulta);
            }, "Diagnóstico salvo com sucesso.", "Erro ao salvar diagnóstico");

            boolean concluido = false;
            while (!concluido) {
                System.out.println("\n--- Pós-Consulta para ID: " + idConsulta + " ---");
                System.out.println("1. Adicionar Prescrição de Medicamento");
                System.out.println("2. Solicitar Exame");
                System.out.println("0. Concluir Pós-Consulta");
                System.out.print("Escolha uma opção: ");
                int opcao = ConsoleUtil.lerInt(scanner);

                switch (opcao) {
                    case 1:
                        adicionarPrescricao(consulta);
                        break;
                    case 2:
                        adicionarExame(consulta);
                        break;
                    case 0:
                        concluido = true;
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        break;
                }
            }

        } catch (SQLException e) {
            System.err.println("\n[ERRO BD] Erro ao buscar dados da consulta: " + e.getMessage());
        }
    }

    /**
     * Adiciona uma nova prescrição a uma consulta.
     * 
     * Este método solicita ao usuário informações sobre os medicamentos a serem 
     * adicionados à prescrição, incluindo o ID do medicamento, dosagem, 
     * frequência e duração. O usuário pode adicionar múltiplos medicamentos 
     * até que decida parar digitando 0. Se nenhum item for adicionado, a 
     * prescrição não será salva.
     * 
     * @param consulta A consulta à qual a prescrição será associada.
     */
    private void adicionarPrescricao(Consulta consulta) {
        System.out.println("\n--- Adicionar Prescrição ---");

        executarAcao(() -> {
            Prescricao novaPrescricao = new Prescricao();
            novaPrescricao.setIdConsulta(consulta.getIdConsulta());

            while (true) {
                System.out.print("Digite o ID do Medicamento (ou 0 para parar): ");
                int idMedicamento = ConsoleUtil.lerInt(scanner);
                if (idMedicamento == 0) {
                    break;
                }
                
                Medicamento med = medicamentosService.buscarMedicamento(idMedicamento);
                if (med == null) {
                    System.out.println("Medicamento com ID " + idMedicamento + " não encontrado.");
                    continue;
                }
                
                System.out.print("Dosagem (ex: 500mg): ");
                String dosagem = ConsoleUtil.lerString(scanner);
                System.out.print("Frequência (ex: 8 em 8 horas): ");
                String frequencia = ConsoleUtil.lerString(scanner);
                System.out.print("Duração (ex: 7 dias): ");
                String duracao = ConsoleUtil.lerString(scanner);
                
                ItemPrescricao item = new ItemPrescricao(0, 0, idMedicamento, dosagem, frequencia, duracao, "");
                novaPrescricao.addItem(item);
                System.out.println(med.getNome() + " adicionado à prescrição.");
            }

            if (novaPrescricao.getItens().isEmpty()) {
                System.out.println("Nenhum item adicionado. A prescrição não foi salva.");
                return;
            }

            prescricaoService.criarPrescricao(novaPrescricao);

        }, "Prescrição salva com sucesso!", "Erro ao salvar prescrição");
    }

    /**
     * Adiciona um exame ao histórico médico do paciente associado à consulta.
     * 
     * Este método verifica se já existe um histórico médico para o paciente. 
     * Se não existir, um novo histórico é criado e cadastrado. Em seguida, 
     * solicita ao usuário o tipo do exame a ser realizado e cria um objeto 
     * Exames com as informações necessárias, incluindo o ID da consulta, 
     * o ID do histórico, o tipo do exame, a data de solicitação e o status 
     * do exame. Por fim, o exame é cadastrado no sistema.
     * 
     * @param consulta A consulta associada ao paciente para o qual o exame está sendo solicitado.
     */
    private void adicionarExame(Consulta consulta) {
        System.out.println("\n--- Solicitar Exame ---");

        executarAcao(() -> {

            HistoricoMedico historico = historicoMedicoService.buscarHistorico(consulta.getCpfPaciente());
            if (historico == null) {
                System.out.println("Criando novo histórico médico para o paciente...");
                HistoricoMedico novoHistorico = new HistoricoMedico();
                novoHistorico.setCpfPaciente(consulta.getCpfPaciente());
                novoHistorico.setStatusHistorico("Ativo");
                novoHistorico.setObservacoes("Histórico criado em " + LocalDate.now());
                historico = historicoMedicoService.cadastrarHistorico(novoHistorico);
            }

            System.out.print("Tipo do Exame (ex: Raio-X de Tórax, Hemograma Completo): ");
            String tipoExame = ConsoleUtil.lerString(scanner);

            Exames novoExame = new Exames();
            novoExame.setIdConsulta(consulta.getIdConsulta());
            novoExame.setIdHistorico(historico.getIdHistorico());
            novoExame.setTipo(tipoExame);
            novoExame.setSolicitadoEm(java.sql.Date.valueOf(LocalDate.now()));
            novoExame.setStatus("Pendente");

            examesService.cadastrarExame(novoExame);

        }, "Exame solicitado com sucesso!", "Erro ao solicitar exame");
    }

    @Override protected String obterTituloMenu() { return "Módulo de Consultas"; }
    @Override protected void imprimirOpcoes() { }
    @Override protected boolean processarOpcao(int opcao) { return false; }
}