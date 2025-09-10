package com.hospital;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.hospital.model.Paciente;
import com.hospital.service.AtendimentoService;
import com.hospital.service.ConsultaService;
import com.hospital.service.EnfermeiroService;
import com.hospital.service.ExamesService;
import com.hospital.service.HistoricoMedicoService;
import com.hospital.service.ItemPrescricaoService;
import com.hospital.service.MedicamentosService;
import com.hospital.service.MedicoService;
import com.hospital.service.PacienteService;
import com.hospital.service.PrescricaoService;
import com.hospital.service.SalaService;
import com.hospital.service.TriagemService;
import com.hospital.ui.PacienteUI;
import com.hospital.utils.ConnectionFactory;
import com.hospital.utils.ConsoleUtil;

public class simuladorHospital {

    private Connection connection;
    private Scanner scanner;

    private AtendimentoService atendimentoService;
    private ConsultaService consultaService;
    private EnfermeiroService enfermeiroService;
    private ExamesService examesService;
    private HistoricoMedicoService historicoMedicoService;
    private ItemPrescricaoService itemPrescricaoService;
    private MedicamentosService medicamentosService;
    private MedicoService medicoService;
    private PacienteService pacienteService;
    private PrescricaoService prescricaoService;
    private SalaService salaService;
    private TriagemService triagemService;

    private PacienteUI pacienteUI;

    public simuladorHospital(Connection connection){
        this.connection = connection;
        this.scanner = new Scanner(System.in);
        this.atendimentoService = new AtendimentoService(connection);
        this.consultaService = new ConsultaService(connection);
        this.enfermeiroService = new EnfermeiroService(connection);
        this.examesService = new ExamesService(connection);
        this.historicoMedicoService = new HistoricoMedicoService(connection);
        this.itemPrescricaoService = new ItemPrescricaoService(connection);
        this.medicamentosService = new MedicamentosService(connection);
        this.medicoService = new MedicoService(connection);
        this.pacienteService = new PacienteService(connection);
        this.prescricaoService = new PrescricaoService(connection);
        this.salaService = new SalaService(connection);
        this.triagemService = new TriagemService(connection);

        this.pacienteUI = new PacienteUI(pacienteService, scanner);
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
                    System.out.println("Gereciando médicos...");

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
