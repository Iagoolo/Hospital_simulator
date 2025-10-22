package com.hospital.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.hospital.model.Enfermeiro;
import com.hospital.service.EnfermeiroService;
import com.hospital.utils.ConsoleUtil;

public class EnfermeirosUI {
    
    private EnfermeiroService enfermeiroService;
    private Scanner scanner;

    public EnfermeirosUI(EnfermeiroService enfermeiroService, Scanner scanner){
        this.enfermeiroService = enfermeiroService;
        this.scanner = scanner;
    }

    public void menuGerenciarEnfermeiros(){
        boolean execute = true;
        while (execute){
            System.out.println("\n-------Gerenciador de Enfermeiros--------");
            System.out.println("1. Cadastrar Novo Enfermeiro");
            System.out.println("2. Listar Todos os Enfermeiros");
            System.out.println("3. Buscar Enfermeiro");
            System.out.println("4. Deletar Enfermeiro");
            System.out.println("5. Atualizar Enfermeiro");
            System.out.println("0. Voltar ao Menu Principal");

            int opcao = ConsoleUtil.lerInt(scanner);

            switch (opcao) {
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    execute = false;

                    break;

                case 1:
                    cadastrarEnfermeiro();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 2:
                    listarTodosEnfermeiros();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 3:
                    buscarEnfermeiro();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 4:
                    deletarEnfermeiro();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 5:
                    atualizarEnfermeiro();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;

                default:
                    break;
            }
        }
    }

    public void cadastrarEnfermeiro(){
        System.out.println("\n -----Cadastro de Novo Enfermeiro--------");

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

            Enfermeiro enfermeiro = new Enfermeiro();
            enfermeiro.setCpf(cpf);
            enfermeiro.setEndereco(endereco);
            enfermeiro.setIdade(idade);
            enfermeiro.setNome(nome);
            enfermeiro.setNomeMae(mae);
            enfermeiro.setNomePai(pai);

            enfermeiroService.cadastrarEnfermeiro(enfermeiro);

        } catch(SQLException s){
            System.err.println("\n[ERRO NO BANCO DE DADOS] Não foi possível cadastrar o enfermeiro.");
            System.err.println("Detalhes: " + s.getMessage());
        } catch(Exception e){
            System.err.println("\n[ERRO INESPERADO]" + e.getMessage());
        }
    }

    public void listarTodosEnfermeiros() {
        System.out.println("\n-----Lista de todos os Médicos cadastrados-----");
        try{
            List<Enfermeiro> enfermeiros = enfermeiroService.listarTodosEnfermeiros();

            if (enfermeiros.isEmpty()){
                System.out.println("Nenhum enfermeiro cadastrado");
                return;
            }

            System.out.printf("%-15s | %-30s | %s%n", "CPF", "Nome", "Endereço" );
            System.out.println("-".repeat(70));

            for (Enfermeiro enfermeiro : enfermeiros){
                System.out.printf("%-15s | %-30s | %s%n",
                    enfermeiro.getCpf(),
                    enfermeiro.getNome(),
                    enfermeiro.getEndereco());
            }
        } catch (SQLException e){
            System.err.println("[ERRO NO BANCO DE DADOS] Não foi possível listar enfermeiros: " + e.getMessage());
        }
    }

    public void buscarEnfermeiro(){
        System.out.println("\n---- Buscar Enfermeiros -----");

        try {
            System.out.print("Digite o CPF do enfermeiro: ");
            String cpf = ConsoleUtil.lerString(scanner);

            Enfermeiro enfermeiro = enfermeiroService.buscarEnfermeiroCpf(cpf);

            if (enfermeiro != null){
                System.out.println("\nEnfermeiro encontrado!");
                System.out.println("Nome: " + enfermeiro.getNome());
                System.out.println("Idade: " + enfermeiro.getIdade());
                System.out.println("Nome da mãe: " + enfermeiro.getNomeMae());
                System.out.println("Nome do pai: " + enfermeiro.getNomePai());
                System.out.println("Endereço: " + enfermeiro.getEndereco());

            } else {
                System.out.println("\nEnfermeiro não encontrado");
            } 
        } catch (SQLException e){
            System.out.println("\n[ERRO] Enfermeiro não encontrado: " + e.getMessage());
        }
    }

    public void atualizarEnfermeiro(){
        System.out.println("\n---- Atualizar Enfermeiro -----");
        try{
            System.out.print("Digite o CPF do Enfermeiro a ser atualizado: ");
            String cpf = ConsoleUtil.lerString(scanner);

            Enfermeiro enfermeiro = enfermeiroService.buscarEnfermeiroCpf(cpf);

            if (enfermeiro == null){
                System.out.println("enfermeiro não encontrado.");
                return;
            }

            System.out.println("\nDigite os novos dados. (Pressione ENTER caso não haja mudança)");

            System.out.print("Nome atual " + enfermeiro.getNome() + "\nNovo nome: ");
            String nome = scanner.nextLine();

            System.out.print("Endereço atual: " + enfermeiro.getEndereco() + "\nNovo endereço: ");
            String endereco = scanner.nextLine();

            System.out.print("Idade atual: " + enfermeiro.getIdade() + "\nNova idade: ");
            String idadeStr = scanner.nextLine();

            System.out.print("Nome atual da mãe: " + enfermeiro.getNomeMae() + "\nNovo nome da mãe: ");
            String nomeMae = scanner.nextLine();

            System.out.print("Nome atual do pai: " + enfermeiro.getNomePai() + "\nNovo nome do pai: ");
            String nomePai = scanner.nextLine();

            Enfermeiro upEnfermeiro = enfermeiro;

            if (nome != null && !nome.trim().isEmpty()) {
                upEnfermeiro.setNome(nome);
            } 

            if (endereco != null && !endereco.trim().isEmpty()){
                upEnfermeiro.setEndereco(endereco);
            }

            if (idadeStr != null && !idadeStr.trim().isEmpty()){
                try {
                    upEnfermeiro.setIdade(Integer.parseInt(idadeStr));
                } catch (NumberFormatException e){
                    System.out.println("Idade inválida. Mantendo idade anterior.");
                }
            }

            if (nomeMae != null && !nomeMae.trim().isEmpty()){
                upEnfermeiro.setNomeMae(nomeMae);
            }

            if (nomePai != null && !nomePai.trim().isEmpty()){
                upEnfermeiro.setNomePai(nomePai);
            }

            enfermeiroService.atualizarEnfermeiro(upEnfermeiro);
        } catch (SQLException e){
            System.err.println("[ERRO NO BANCO DE DADOS] Não foi possível atualizar enfermeiro: " + e.getMessage());
        }
    }

    public void deletarEnfermeiro(){
        System.out.println("\n---- Deletar Enfermeiro -----");
        try {
            System.out.print("Digite o CPF do enfermeiro: ");
            String cpf = ConsoleUtil.lerString(scanner);

            if (enfermeiroService.buscarEnfermeiroCpf(cpf) == null) {
                System.out.println("Enfermeiro não existente!");
                return;
            }

            System.out.print("Tem certeza que deseja deletar esse enfermeiro? (S/N)");
            System.out.println("Nome do enfermeiro: " + enfermeiroService.buscarEnfermeiroCpf(cpf).getNome());

            String confirmacao = ConsoleUtil.lerString(scanner);

            if (confirmacao.equalsIgnoreCase("S")){
                enfermeiroService.deletarEnfermeiro(cpf);
                System.out.println("Enfermeiro deletado com sucesso!");
            } else {
                System.out.println("\nOperação Cancelada!");
            }

        } catch (SQLException e){
            System.err.println("[ERRO] Não foi possível deletar enfermeiro: " + e.getMessage());
        }
    }
}

