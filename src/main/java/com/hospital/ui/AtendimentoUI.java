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