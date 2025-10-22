package com.hospital.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.hospital.model.Medico;
import com.hospital.service.MedicoService;
import com.hospital.utils.ConsoleUtil;

public class MedicoUI {
    
    private MedicoService medicoService;
    private Scanner scanner;

    public MedicoUI(MedicoService medicoService, Scanner scanner){
        this.medicoService = medicoService;
        this.scanner = scanner;
    }

    public void menuGerenciarMedicos(){
        boolean execute = true;
        while (execute){
            System.out.println("\n-------Gerenciador de Médicos--------");
            System.out.println("1. Cadastrar Novo Médico");
            System.out.println("2. Listar Todos os Médicos");
            System.out.println("3. Buscar Médicos");
            System.out.println("4. Deletar Médico");
            System.out.println("5. Atualizar Médico");
            System.out.println("0. Voltar ao Menu Principal");

            int opcao = ConsoleUtil.lerInt(scanner);

            switch (opcao) {
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    execute = false;

                    break;

                case 1:
                    cadastrarMedico();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 2:
                    listarTodosMedicos();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 3:
                    buscarMedico();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 4:
                    deletarMedico();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;
                
                case 5:
                    atualizarMedico();
                    System.out.println("\nPressione ENTER para continuar");
                    scanner.nextLine();
                    break;

                default:
                    break;
            }
        }
    }

    public void cadastrarMedico(){
        System.out.println("\n -----Cadastro de Novo Médico--------");

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

            Medico medico = new Medico();
            medico.setCpf(cpf);
            medico.setEndereco(endereco);
            medico.setIdade(idade);
            medico.setNome(nome);
            medico.setNomeMae(mae);
            medico.setNomePai(pai);

            medicoService.cadastrarMedico(medico);

        } catch(SQLException s){
            System.err.println("\n[ERRO NO BANCO DE DADOS] Não foi possível cadastrar o médico.");
            System.err.println("Detalhes: " + s.getMessage());
        } catch(Exception e){
            System.err.println("\n[ERRO INESPERADO]" + e.getMessage());
        }
    }

    public void listarTodosMedicos() {
        System.out.println("\n-----Lista de todos os Médicos cadastrados-----");
        try{
            List<Medico> medicos = medicoService.listarTodosMedicos();

            if (medicos.isEmpty()){
                System.out.println("Nenhum médico cadastrado");
                return;
            }

            System.out.printf("%-15s | %-30s | %s%n", "CPF", "Nome", "Endereço" );
            System.out.println("-".repeat(70));

            for (Medico medico : medicos){
                System.out.printf("%-15s | %-30s | %s%n",
                    medico.getCpf(),
                    medico.getNome(),
                    medico.getEndereco());
            }
        } catch (SQLException e){
            System.err.println("[ERRO NO BANCO DE DADOS] Não foi possível listar medicos: " + e.getMessage());
        }
    }

    public void buscarMedico(){
        System.out.println("\n---- Buscar Médicos -----");

        try {
            System.out.print("Digite o CPF do médico: ");
            String cpf = ConsoleUtil.lerString(scanner);

            Medico medico = medicoService.buscarMedicoCpf(cpf);

            if (medico != null){
                System.out.println("\nMédico encontrado!");
                System.out.println("Nome: " + medico.getNome());
                System.out.println("Idade: " + medico.getIdade());
                System.out.println("Nome da mãe: " + medico.getNomeMae());
                System.out.println("Nome do pai: " + medico.getNomePai());
                System.out.println("Endereço: " + medico.getEndereco());

                if (medico.getEspecialidades() != null && !medico.getEspecialidades().isEmpty()){
                    System.out.println("Especialidades: ");
                    for (String especialidade : medico.getEspecialidades()){
                        System.out.println("* " + especialidade);
                    }
                }
            } else {
                System.out.println("\nMédico não encontrado");
            } 
        } catch (SQLException e){
            System.out.println("\n[ERRO] Médico não encontrado: " + e.getMessage());
        }
    }

    public void atualizarMedico(){
        System.out.println("\n---- Atualizar Medico -----");
        try{
            System.out.print("Digite o CPF do Medico a ser atualizado: ");
            String cpf = ConsoleUtil.lerString(scanner);

            Medico medico = medicoService.buscarMedicoCpf(cpf);

            if (medico == null){
                System.out.println("medico não encontrado.");
                return;
            }

            System.out.println("\nDigite os novos dados. (Pressione ENTER caso não haja mudança)");

            System.out.print("Nome atual " + medico.getNome() + "\nNovo nome: ");
            String nome = scanner.nextLine();

            System.out.print("Endereço atual: " + medico.getEndereco() + "\nNovo endereço: ");
            String endereco = scanner.nextLine();

            System.out.print("Idade atual: " + medico.getIdade() + "\nNova idade: ");
            String idadeStr = scanner.nextLine();

            System.out.print("Nome atual da mãe: " + medico.getNomeMae() + "\nNovo nome da mãe: ");
            String nomeMae = scanner.nextLine();

            System.out.print("Nome atual do pai: " + medico.getNomePai() + "\nNovo nome do pai: ");
            String nomePai = scanner.nextLine();

            Medico upMedico = medico;

            if (nome != null && !nome.trim().isEmpty()) {
                upMedico.setNome(nome);
            } 

            if (endereco != null && !endereco.trim().isEmpty()){
                upMedico.setEndereco(endereco);
            }

            if (idadeStr != null && !idadeStr.trim().isEmpty()){
                try {
                    upMedico.setIdade(Integer.parseInt(idadeStr));
                } catch (NumberFormatException e){
                    System.out.println("Idade inválida. Mantendo idade anterior.");
                }
            }

            if (nomeMae != null && !nomeMae.trim().isEmpty()){
                upMedico.setNomeMae(nomeMae);
            }

            if (nomePai != null && !nomePai.trim().isEmpty()){
                upMedico.setNomePai(nomePai);
            }

            medicoService.atualizarMedico(upMedico);
        } catch (SQLException e){
            System.err.println("[ERRO NO BANCO DE DADOS] Não foi possível atualizar médico: " + e.getMessage());
        }
    }

    public void deletarMedico(){
        System.out.println("\n---- Deletar Médico -----");
        try {
            System.out.print("Digite o CPF do médico: ");
            String cpf = ConsoleUtil.lerString(scanner);

            if (medicoService.buscarMedicoCpf(cpf) == null) {
                System.out.println("Médico não existente!");
                return;
            }

            System.out.print("Tem certeza que deseja deletar esse médico? (S/N)");
            System.out.println("Nome do médico: " + medicoService.buscarMedicoCpf(cpf).getNome());

            String confirmacao = ConsoleUtil.lerString(scanner);

            if (confirmacao.equalsIgnoreCase("S")){
                medicoService.deletarMedico(cpf);
                System.out.println("Médico deletado com sucesso!");
            } else {
                System.out.println("\nOperação Cancelada!");
            }

        } catch (SQLException e){
            System.err.println("[ERRO] Não foi possível deletar médico: " + e.getMessage());
        }
    }
}
