package com.hospital.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.hospital.model.Paciente;
import com.hospital.service.PacienteService;
import com.hospital.utils.ConsoleUtil;

public class PacienteUI {
    
    private PacienteService pacienteService;
    private Scanner scanner;

    public PacienteUI(PacienteService pacienteService, Scanner scanner){
        this.pacienteService = pacienteService;
        this.scanner = scanner;
    }

    public void menuGerenciarPacientes(){
        boolean execute = true;
        while (execute){
            System.out.println("\n-------Gerenciador de pacientes--------");
            System.out.println("1. Cadastrar Novo Paciente");
            System.out.println("2. Listar Todos os Pacientes");
            System.out.println("3. Buscar Paciente");
            System.out.println("4. Deletar Paciente");
            System.out.println("5. Atualizar Paciente");
            System.out.println("0. Voltar ao Menu Principal");

            int opcao = ConsoleUtil.lerInt(scanner);

            switch (opcao) {
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    execute = false;

                    break;

                case 1:
                    cadastrarPaciente();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 2:
                    listarTodosPacientes();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 3:
                    buscarPaciente();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 4:
                    deletarPaciente();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 5:
                    atualizarPaciente();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;

                default:
                    break;
            }
        }
    }

    public void cadastrarPaciente(){
        System.out.println("\n -----Cadastro de Novo Paciente--------");

        try{
            System.out.println("CPF (Apenas números)");
            String cpf = ConsoleUtil.lerString(scanner);

            System.out.println("Nome Completo:");
            String nome = ConsoleUtil.lerString(scanner);

            System.out.println("Endereço:");
            String endereco = ConsoleUtil.lerString(scanner);

            System.out.println("Idade:");
            int idade = ConsoleUtil.lerInt(scanner);

            System.out.println("Nome da mãe:");
            String mae = ConsoleUtil.lerString(scanner);

            System.out.println("Nome do pai:");
            String pai = ConsoleUtil.lerString(scanner);

            Paciente paciente = new Paciente();
            paciente.setCpf(cpf);
            paciente.setEndereco(endereco);
            paciente.setIdade(idade);
            paciente.setNome(nome);
            paciente.setNomeMae(mae);
            paciente.setNomePai(pai);

            pacienteService.cadastrarPaciente(paciente);

        } catch(SQLException s){
            System.err.println("\n[ERRO NO BANCO DE DADOS] Não foi possível cadastrar o paciente.");
            System.err.println("Detalhes: " + s.getMessage());
        } catch(Exception e){
            System.err.println("\n[ERRO INESPERADO]" + e.getMessage());
        }
    }

    public void listarTodosPacientes() {
        System.out.println("\n-----Lista de todos os Pacientes cadastrados-----");
        try{
            List<Paciente> pacientes = pacienteService.listarTodosPacientes();

            if (pacientes.isEmpty()){
                System.out.println("Nenhum paciente cadastrado");
                return;
            }

            System.out.printf("%-15s | %-30s | %s%n", "CPF", "Nome", "Endereço" );
            System.out.println("-".repeat(70));

            for (Paciente paciente : pacientes){
                System.out.printf("%-15s | %-30s | %s%n",
                    paciente.getCpf(),
                    paciente.getNome(),
                    paciente.getEndereco());
            }
        } catch (SQLException e){
            System.err.println("[ERRO NO BANCO DE DADOS] Não foi possível listar pacientes: " + e.getMessage());
        }
    }

    public void buscarPaciente(){
        System.out.println("\n---- Buscar Paciente -----");

        try {
            System.out.print("Digite o CPF do paciente: ");
            String cpf = ConsoleUtil.lerString(scanner);

            Paciente paciente = pacienteService.buscarPacienteCpf(cpf);

            if (paciente != null){
                System.out.println("\nPaciente encontrado!");
                System.out.println("Nome: " + paciente.getNome());
                System.out.println("Idade: " + paciente.getIdade());
                System.out.println("Nome da mãe: " + paciente.getNomeMae());
                System.out.println("Nome do pai: " + paciente.getNomePai());
                System.out.println("Endereço: " + paciente.getEndereco());

                if (paciente.getSintomas() != null && !paciente.getSintomas().isEmpty()){
                    System.out.println("Sintomas: ");
                    for (String sintoma : paciente.getSintomas()){
                        System.out.println("* " + sintoma);
                    }
                }
            } else {
                System.out.println("\nPaciente não encontrado");
            } 
        } catch (SQLException e){
            System.out.println("\n[ERRO] Paciente não encontrado: " + e.getMessage());
        }
    }

    public void atualizarPaciente(){
        System.out.println("\n---- Atualizar Paciente -----");
        try{
            System.out.print("Digite o CPF do paciente a ser atualizado: ");
            String cpf = ConsoleUtil.lerString(scanner);

            Paciente paciente = pacienteService.buscarPacienteCpf(cpf);

            if (paciente == null){
                System.out.println("Paciente não encontrado.");
                return;
            }

            System.out.println("\nDigite os novos dados. (Pressione ENTER caso não haja mudança)");

            System.out.print("Nome atual " + paciente.getNome() + "\nNovo nome: ");
            String nome = scanner.nextLine();

            System.out.print("Endereço atual: " + paciente.getEndereco() + "\nNovo endereço: ");
            String endereco = scanner.nextLine();

            System.out.print("Idade atual: " + paciente.getIdade() + "\nNova idade: ");
            String idadeStr = scanner.nextLine();

            System.out.print("Nome atual da mãe: " + paciente.getNomeMae() + "\nNovo nome da mãe: ");
            String nomeMae = scanner.nextLine();

            System.out.print("Nome atual do pai: " + paciente.getNomePai() + "\nNovo nome do pai: ");
            String nomePai = scanner.nextLine();

            Paciente upPaciente = paciente;

            if (nome != null && !nome.trim().isEmpty()) {
                upPaciente.setNome(nome);
            } 

            if (endereco != null && !endereco.trim().isEmpty()){
                upPaciente.setEndereco(endereco);
            }

            if (idadeStr != null && !idadeStr.trim().isEmpty()){
                try {
                    upPaciente.setIdade(Integer.parseInt(idadeStr));
                } catch (NumberFormatException e){
                    System.out.println("Idade inválida. Mantendo idade anterior.");
                }
            }

            if (nomeMae != null && !nomeMae.trim().isEmpty()){
                upPaciente.setNomeMae(nomeMae);
            }

            if (nomePai != null && !nomePai.trim().isEmpty()){
                upPaciente.setNomePai(nomePai);
            }

            pacienteService.atualizar(upPaciente);
        } catch (SQLException e){
            System.err.println("[ERRO NO BANCO DE DADOS] Não foi possível atualizar paciente: " + e.getMessage());
        }
    }

    public void deletarPaciente(){
        System.out.println("\n---- Deletar Paciente -----");
        try {
            System.out.print("Digite o CPF do paciente: ");
            String cpf = ConsoleUtil.lerString(scanner);

            if (pacienteService.buscarPacienteCpf(cpf) == null) {
                System.out.println("Paciente não existente!");
                return;
            }

            System.out.print("Tem certeza que deseja deletar esse paciente? (S/N)");
            System.out.println("Nome do paciente: " + pacienteService.buscarPacienteCpf(cpf).getNome());

            String confirmacao = ConsoleUtil.lerString(scanner);

            if (confirmacao.equalsIgnoreCase("S")){
                pacienteService.deletarPaciente(cpf);
                System.out.println("Paciente deletado com sucesso!");
            } else {
                System.out.println("\nOperação Cancelada!");
            }

        } catch (SQLException e){
            System.err.println("[ERRO] Não foi possível deletar paciente: " + e.getMessage());
        }
    }
}
