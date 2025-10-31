package com.hospital.ui;

import java.util.Scanner;

import com.hospital.utils.ConsoleUtil;
import com.hospital.model.Atendimento;
import com.hospital.model.Consulta;
import com.hospital.model.Triagem;
import com.hospital.service.AtendimentoService;
import com.hospital.service.ConsultaService;
import com.hospital.service.EnfermeiroService;
import com.hospital.service.MedicoService;
import com.hospital.service.PacienteService;
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

    public AtendimentoUI (Scanner scanner, PacienteService paciente, TriagemService triagem, SalaService sala, ConsultaService consulta, MedicoService medico, EnfermeiroService enfermeiro, AtendimentoService atend){
        super(scanner);
        this.pacienteService = paciente;
        this.triagemService = triagem;
        this.salaService = sala;
        this.consultaService = consulta;
        this.medicoService = medico;
        this.enfermeiroService = enfermeiro;
        this.atendimentoService = atend;
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

    public void registrarPosConsulta(){
        System.out.println("Digite o id da consulta");
    
        executarAcao(() -> {
            int idConsulta = ConsoleUtil.lerInt(scanner);

            if (consultaService.procurarConsultaId(idConsulta) == null){
                throw new Exception("Consulta não encontrada");
            }

            
           

        }, "Paciente em consulta", "Erro ao realizar consulta");
    }

    public void finalizarAtendimento(){}
}
