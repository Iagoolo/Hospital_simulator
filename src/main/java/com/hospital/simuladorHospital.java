package com.hospital;

import java.sql.Connection;
import java.util.Scanner;

import com.hospital.service.ServiceContainer;
import com.hospital.ui.*;
import com.hospital.utils.ConnectionFactory;
import com.hospital.utils.ConsoleUtil;

/**
 * Classe principal do simulador de atendimento hospitalar.
 */
public class simuladorHospital {

    private Scanner scanner;
    private ServiceContainer services;

    private PacienteUI pacienteUI;
    private MedicoUI medicoUI;
    private EnfermeirosUI enfermeirosUI;
    private SalasUI salasUI;
    private AtendimentoUI atendimentoUI;
    private TriagemUI triagemUI;
    private ConsultaUI consultaUI;
    private MedicamentosUI medicamentosUI;

    public simuladorHospital(Connection connection) {
        this.scanner = new Scanner(System.in);
        this.services = new ServiceContainer(connection);
    
        this.pacienteUI = new PacienteUI(services.pacienteService, services.historicoMedicoService, scanner);
        this.medicoUI = new MedicoUI(services.medicoService, scanner);
        this.enfermeirosUI = new EnfermeirosUI(services.enfermeiroService, scanner);
        this.salasUI = new SalasUI(services.salaService, scanner);
        this.medicamentosUI = new MedicamentosUI(services.medicamentosService, scanner);
        this.triagemUI = new TriagemUI(scanner, 
            services.triagemService, 
            services.atendimentoService, 
            services.enfermeiroService
        );
        
        this.consultaUI = new ConsultaUI(
            scanner,
            services.consultaService,
            services.atendimentoService,
            services.medicoService,
            services.salaService,
            services.prescricaoService,
            services.examesService,
            services.medicamentosService,
            services.historicoMedicoService
        );


        this.atendimentoUI = new AtendimentoUI(
            scanner, 
            services.atendimentoService,
            services.pacienteService,
            services.consultaService,
            services.historicoMedicoService,
            services.medicoService,
            triagemUI, 
            consultaUI
        );
    }
    
    /**
     * Ponto de entrada da aplicação.
     */
    public static void main(String[] args) {

        try(Connection conn = ConnectionFactory.createConnection()){
            simuladorHospital simulador = new simuladorHospital(conn);
            simulador.run();
        } catch (Exception e){
            System.err.println("Erro fatal na aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Executa o loop principal do simulador.
     */
    public void run(){
        boolean execute = true;
        while(execute){
            exibirMenuPrincipal();
            int opcao = ConsoleUtil.lerInt(scanner);
            
            switch (opcao) {
                case 0:
                    System.out.println("Saindo...");
                    execute = false;
                    break;
                
                case 1:
                    pacienteUI.exibirMenu();;

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 2:
                    medicoUI.exibirMenu();

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;

                case 3: 
                    enfermeirosUI.exibirMenu();

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;

                case 4:
                    salasUI.exibirMenu();

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 5:
                    medicamentosUI.exibirMenu();

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 6:
                    atendimentoUI.exibirMenu();

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;

                default:
                    break;
            }
        }

        System.out.println("Encerrando programa...");
    }

    /**
     * Exibe o menu principal do simulador.
     */
    public void exibirMenuPrincipal(){
        System.out.println("-------Simulador de Atendimento Hospitalar--------");
        System.out.println("1. Gerenciar Pacientes");
        System.out.println("2. Gerenciar Médicos");
        System.out.println("3. Gerenciar Enfermeiros");
        System.out.println("4. Gerenciar Salas");
        System.out.println("5. Gerenciar Medicamentos");
        System.out.println("6. Iniciar Novo Atendimento");
        System.out.println("0. Sair");
        System.out.println("Escolha uma opção: ");
    }
}
