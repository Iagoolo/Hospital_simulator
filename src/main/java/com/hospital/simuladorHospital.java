package com.hospital;

import java.sql.Connection;
import java.util.Scanner;
import com.hospital.service.MedicoService;
import com.hospital.service.PacienteService;
import com.hospital.ui.MedicoUI;
import com.hospital.ui.PacienteUI;
import com.hospital.utils.ConnectionFactory;
import com.hospital.utils.ConsoleUtil;

public class simuladorHospital {

    private Scanner scanner;
    private MedicoService medicoService;
    private PacienteService pacienteService;

    private PacienteUI pacienteUI;
    private MedicoUI medicoUI;

    public simuladorHospital(Connection connection){
        this.scanner = new Scanner(System.in);
        
        this.medicoService = new MedicoService(connection);
        this.pacienteService = new PacienteService(connection);

        this.pacienteUI = new PacienteUI(pacienteService, scanner);
        this.medicoUI = new MedicoUI(medicoService, scanner);

    }
    
    public static void main(String[] args) {

        try(Connection conn = ConnectionFactory.createConnection()){
            simuladorHospital simulador = new simuladorHospital(conn);
            simulador.run();
        } catch (Exception e){
            System.err.println("Erro fatal na aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
                   pacienteUI.menuGerenciarPacientes();

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 2:
                    medicoUI.menuGerenciarMedicos();

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;

                case 3: 
                    System.out.println("Gerenciando enfermeios...");

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;

                case 4:
                    System.out.println("Gerenciando salas...");

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 5:
                    System.out.println("Iniciando novo atendimento");

                    System.out.println("Pressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                default:
                    break;
            }
        }

        System.out.println("Encerrando programa...");
    }

    public void exibirMenuPrincipal(){
        System.out.println("-------Simulador de Atendimento Hospitalar--------");
        System.out.println("1. Gerenciar Pacientes");
        System.out.println("2. Gerenciar Médicos");
        System.out.println("3. Gerenciar Enfermeiros");
        System.out.println("4. Gerenciar Salas");
        System.out.println("5. Iniciar Novo Atendimento");
        System.out.println("0. Sair");
        System.out.println("Escolha uma opção: ");
    }
}
