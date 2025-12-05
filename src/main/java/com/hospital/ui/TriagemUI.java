package com.hospital.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import com.hospital.model.Atendimento;
import com.hospital.model.Triagem;
import com.hospital.service.AtendimentoService;
import com.hospital.service.EnfermeiroService;
import com.hospital.service.TriagemService;
import com.hospital.utils.ConsoleUtil;

public class TriagemUI extends BaseUI {

    private TriagemService triagemService;
    private AtendimentoService atendimentoService;
    private EnfermeiroService enfermeiroService;

    public TriagemUI(Scanner scanner, TriagemService ts, AtendimentoService as, EnfermeiroService es) {
        super(scanner);
        this.triagemService = ts;
        this.atendimentoService = as;
        this.enfermeiroService = es;
    }

    public void realizarTriagemWizard() {
        System.out.println("\n--- Realizar Próxima Triagem ---");

        executarAcao(() -> {
    
            Atendimento atendimento = atendimentoService.buscarProximoPaciente();
            
            if (atendimento == null) {
                throw new Exception("Não há pacientes esperando por uma triagem");
            }
            
            System.out.println("Chamando paciente (CPF): " + atendimento.getCpfPaciente());

            System.out.print("CPF do Enfermeiro responsável: ");
            String cpfEnf = ConsoleUtil.lerString(scanner);

            if (enfermeiroService.buscarEnfermeiroCpf(cpfEnf) == null){
                throw new Exception("Enfermeiro com CPF " + cpfEnf + " não encontrado.");
            }

            System.out.print("Peso (kg): ");
            double peso = Double.parseDouble(ConsoleUtil.lerString(scanner));

            System.out.print("Temperatura (C): ");
            double temp = Double.parseDouble(ConsoleUtil.lerString(scanner));

            System.out.print("Prioridade (Baixa, Média, Alta): ");
            String prioridade = ConsoleUtil.lerString(scanner);

            Triagem triagem = new Triagem();
            triagem.setCpfPaciente(atendimento.getCpfPaciente());
            triagem.setCpfEnfermeiro(cpfEnf);
            triagem.setPeso(peso);
            triagem.setTemperatura(temp);
            triagem.setPrioridade(prioridade);
            triagem.setDataTriagem(LocalDate.now());
            triagem.setHoraTriagem(LocalTime.now());

            Triagem triagemSalva = triagemService.realizarTriagem(triagem);

            atendimento.setStatus("Aguardando Consulta");
            atendimento.setIdTriagem(triagemSalva.getIdTriagem());
            
            atendimentoService.atualizarAtendimento(atendimento);

        }, "Triagem finalizada com sucesso! Paciente encaminhado para consulta.", "Erro na triagem");
    }

    @Override protected String obterTituloMenu() { return "Módulo de Triagem"; }
    @Override protected void imprimirOpcoes() { }
    @Override protected boolean processarOpcao(int opcao) { return false; }
}