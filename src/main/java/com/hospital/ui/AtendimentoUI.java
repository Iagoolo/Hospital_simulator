package com.hospital.ui;

import java.util.Scanner;

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

    public AtendimentoUI (Scanner scanner, PacienteService paciente, TriagemService triagem, SalaService sala, ConsultaService consulta, MedicoService medico, EnfermeiroService enfermeiro){
        super(scanner);
        this.pacienteService = paciente;
        this.triagemService = triagem;
        this.salaService = sala;
        this.consultaService = consulta;
        this.medicoService = medico;
        this.enfermeiroService = enfermeiro;
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

    public void registrarChegada(){}

    public void realizarProximaTriagem(){}

    public void iniciarConsulta(){}

    public void registrarPosConsulta(){}

    public void finalizarAtendimento(){}
}
