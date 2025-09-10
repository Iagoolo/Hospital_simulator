package com.hospital.ui;

import java.sql.SQLException;
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
            System.out.println("3. Atualizar Paciente");
            System.out.println("4. Deletar Paciente");
            System.out.println("0. Voltar ao Menu Principal");

            int opcao = ConsoleUtil.lerInt(scanner);

            switch (opcao) {
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    execute = false;

                    break;

                case 1:
                    cadastrarPaciente();
                    break;
                
                case 2:
                    break;
                
                case 3:
                    break;
                
                case 4:
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
}
