package com.hospital.ui;

import com.hospital.model.Exames;
import com.hospital.model.HistoricoMedico;
import com.hospital.model.ItemPrescricao;
import com.hospital.model.Medicamento;
import com.hospital.model.Prescricao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

import com.hospital.utils.ConsoleUtil;
import com.hospital.model.Atendimento;
import com.hospital.model.Consulta;
import com.hospital.model.Triagem;
import com.hospital.service.AtendimentoService;
import com.hospital.service.ConsultaService;
import com.hospital.service.EnfermeiroService;
import com.hospital.service.ExamesService;
import com.hospital.service.HistoricoMedicoService;
import com.hospital.service.MedicamentosService;
import com.hospital.service.MedicoService;
import com.hospital.service.PacienteService;
import com.hospital.service.PrescricaoService;
import com.hospital.service.SalaService;
import com.hospital.service.TriagemService;

public class AtendimentoUI extends BaseUI {
    
    private PacienteService pacienteService;
    private TriagemService triagemService;
    private SalaService salaService;
    private ConsultaService consultaService;
    private MedicoService medicoService;
    private EnfermeiroService enfermeiroService;
    private AtendimentoService atendimentoService;
    private ExamesService examesService;
    private HistoricoMedicoService historicoMedicoService;
    private MedicamentosService medicamentosService;
    private PrescricaoService prescricaoService;

    public AtendimentoUI(Scanner scanner, 
                         PacienteService pacienteService, 
                         EnfermeiroService enfermeiroService, 
                         MedicoService medicoService, 
                         TriagemService triagemService, 
                         ConsultaService consultaService, 
                         AtendimentoService atendimentoService, 
                         PrescricaoService prescricaoService, 
                         MedicamentosService medicamentosService, 
                         ExamesService examesService, 
                         HistoricoMedicoService historicoMedicoService, 
                         SalaService salaService) {
        
        super(scanner); 
        
        this.pacienteService = pacienteService;
        this.enfermeiroService = enfermeiroService;
        this.medicoService = medicoService;
        this.triagemService = triagemService;
        this.consultaService = consultaService;
        this.atendimentoService = atendimentoService;
        this.prescricaoService = prescricaoService;
        this.medicamentosService = medicamentosService;
        this.examesService = examesService;
        this.historicoMedicoService = historicoMedicoService;
        this.salaService = salaService;
    }

    @Override
    protected String obterTituloMenu(){
        return "Fluxo de Atendimento";
    }

    @Override
    protected void imprimirOpcoes(){
        System.out.println("1. Registrar chegada");
        System.out.println("2. Realizar Triagem");
        System.out.println("3. Iniciar Consulta");
        System.out.println("4. Registrar Pós-Consulta");
        System.out.println("5. Finalizar Atendimento");
        System.out.println("0. Voltar ao Menu Principal");
    }

    @Override
    protected boolean processarOpcao(int opcao){
        switch (opcao) {
            case 0:
                return false;

            case 1:
                registrarChegada();
                return true;
                
            case 2:
                realizarProximaTriagem();
                return true;
                
            case 3:
                iniciarConsulta();
                return true;
                
            case 4:
                registrarPosConsulta();
                return true;
                
            case 5:
                finalizarAtendimento();
                return true;

            default:
                return false;
        }
    }

    public void registrarChegada() {
        System.out.println("Digite o CPF do paciente");
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

    public void realizarProximaTriagem() {
        System.out.println("\n--- 2. Realizar Próxima Triagem ---");

        executarAcao(() -> {
            Atendimento atendimento = atendimentoService.buscarProximoPaciente();
            System.out.println("Chamando paciente: " + atendimento.getCpfPaciente());

            System.out.print("Digite o CPF do enfermeiro: ");
            String cpfEnfermeiro = ConsoleUtil.lerString(scanner);
        
            if(enfermeiroService.buscarEnfermeiroCpf(cpfEnfermeiro) == null) {
                 throw new Exception("Enfermeiro não encontrado.");
            }

            System.out.print("Digite o peso do paciente (ex: 70.5): ");
            double peso = Double.parseDouble(ConsoleUtil.lerString(scanner));

            System.out.print("Digite a temperatura do paciente (ex: 36.5): ");
            double temperatura = Double.parseDouble(ConsoleUtil.lerString(scanner));

            System.out.print("Digite a prioridade (Baixa, Média, Alta): ");
            String prioridade = ConsoleUtil.lerString(scanner);

            Triagem triagem = new Triagem();
            triagem.setCpfPaciente(atendimento.getCpfPaciente());
            triagem.setCpfEnfermeiro(cpfEnfermeiro);
            triagem.setPeso(peso);
            triagem.setTemperatura(temperatura);
            triagem.setPrioridade(prioridade);
            triagem.setDataTriagem(java.time.LocalDate.now());
            triagem.setHoraTriagem(java.time.LocalTime.now());

            Triagem triagemSalva = triagemService.realizarTriagem(triagem);

            atendimento.setStatus("Aguardando Consulta");
            atendimento.setIdTriagem(triagemSalva.getIdTriagem()); 
            
            atendimentoService.atualizarAtendimento(atendimento);

        }, "Triagem realizada com sucesso. Paciente aguardando consulta.", "Erro ao realizar triagem");
    }

    public void iniciarConsulta(){
        System.out.println("\n--- 3. Iniciar Consulta ---");

        executarAcao(() -> {
            Atendimento atendimento = atendimentoService.buscarProximoParaConsulta();
            System.out.println("Chamando paciente: " + atendimento.getCpfPaciente());

            System.out.print("Digite o CPF do médico: ");
            String cpfMedico = ConsoleUtil.lerString(scanner);
        
            if(medicoService.buscarMedicoCpf(cpfMedico) == null) {
                 throw new Exception("Medico não encontrado.");
            }

            System.out.print("Digite o número da sala: ");
            int idSala = ConsoleUtil.lerInt(scanner);
        
            if(salaService.buscarSala(idSala) == null) {
                 throw new Exception("Sala não encontrada.");
            }
            
            Consulta consulta = new Consulta();
            consulta.setCpfMedico(cpfMedico);
            consulta.setCpfPaciente(atendimento.getCpfPaciente());
            consulta.setIdTriagem(atendimento.getIdTriagem());
            consulta.setDataConsulta(java.time.LocalDate.now());
            consulta.setHoraConsulta(java.time.LocalTime.now());
            consulta.setSala(idSala);

            Consulta consultaSalva = consultaService.criarConsulta(consulta);;

            atendimento.setStatus("Em Consulta");
            atendimento.setIdConsulta(consultaSalva.getIdConsulta());
            
            atendimentoService.atualizarAtendimento(atendimento);

        }, "Paciente em consulta", "Erro ao realizar consulta");
    }

    private void registrarPosConsulta() {
        System.out.println("\n--- 4. Registrar Pós-Consulta (Diagnóstico/Exames/Prescrições) ---");
        
        try {
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

    private void finalizarAtendimento() {
        System.out.println("\n--- 5. Finalizar Atendimento e Atualizar Histórico ---");
        
        System.out.print("Digite o ID do Atendimento que deseja finalizar: ");
        int idAtendimento = ConsoleUtil.lerInt(scanner);

        executarAcao(() -> {
            Atendimento atendimento = atendimentoService.buscarAtendimentoPorId(idAtendimento);

            if (atendimento.getStatus().equals("Finalizado")) {
                throw new Exception("Este atendimento já foi finalizado.");
            }
            if (atendimento.getIdConsulta() == 0) {
                throw new Exception("Este atendimento não pode ser finalizado pois não possui uma consulta registrada.");
            }

            Consulta consulta = consultaService.procurarConsultaId(atendimento.getIdConsulta());
            if (consulta == null) {
                throw new Exception("Erro: Consulta associada ao atendimento não encontrada.");
            }
            if (consulta.getDiagnostico() == null || consulta.getDiagnostico().isEmpty()) {
                throw new Exception("Consulta com ID " + consulta.getIdConsulta() + " ainda não possui um diagnóstico. Registre-o primeiro.");
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
