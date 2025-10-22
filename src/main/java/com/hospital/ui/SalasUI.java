package com.hospital.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.hospital.model.Sala;
import com.hospital.service.SalaService;
import com.hospital.utils.ConsoleUtil;

public class SalasUI {
    
    private SalaService salaService;
    private Scanner scanner;

    public SalasUI(SalaService salaService, Scanner scanner){
        this.salaService = salaService;
        this.scanner = scanner;
    }

    public void menuGerenciarMedicos(){
        boolean execute = true;
        while (execute){
            System.out.println("\n-------Gerenciador de Salas--------");
            System.out.println("1. Cadastrar Nova Sala");
            System.out.println("2. Listar Todas as Salas");
            System.out.println("3. Buscar Salas");
            System.out.println("4. Deletar Sala");
            System.out.println("5. Atualizar Sala");
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
        System.out.println("\n -----Cadastro de Nova Sala--------");

        try{
            System.out.println("Id da sala (Apenas números)");
            int idSala  = ConsoleUtil.lerInt(scanner);

            System.out.println("Andar da sala:");
            int andar = ConsoleUtil.lerInt(scanner);

            System.out.println("Tipo de sala");
            String tipoSala = ConsoleUtil.lerString(scanner);

            Sala sala = new Sala();
            sala.setAndar(andar);
            sala.setIdSala(idSala);
            sala.setTipoSala(tipoSala);

            salaService.cadastrarSala(sala);

        } catch(SQLException s){
            System.err.println("\n[ERRO NO BANCO DE DADOS] Não foi possível cadastrar a sala.");
            System.err.println("Detalhes: " + s.getMessage());
        } catch(Exception e){
            System.err.println("\n[ERRO INESPERADO]" + e.getMessage());
        }
    }

    public void listarTodasSalas() {
        System.out.println("\n-----Lista de todos as salas cadastradas-----");
        try{
            List<Sala> salas = salaService.listarTodasSalas();

            if (salas.isEmpty()){
                System.out.println("Nenhuma sala cadastrada");
                return;
            }

            System.out.printf("%-15s | %-30s | %s%n", "CPF", "Nome", "Endereço" );
            System.out.println("-".repeat(70));

            for (Sala sala : salas){
                System.out.printf("%-15s | %-30s | %s%n",
                    sala.getAndar(),
                    sala.getIdSala(),
                    sala.getTipoSala());
            }
        } catch (SQLException e){
            System.err.println("[ERRO NO BANCO DE DADOS] Não foi possível listar medicos: " + e.getMessage());
        }
    }

    public void buscarSala(){
        System.out.println("\n---- Buscar Sala -----");

        try {
            System.out.print("Digite o ID do sala: ");
            int idSala = ConsoleUtil.lerInt(scanner);

            Sala sala = salaService.buscarSala(idSala);

            if (sala != null){
                System.out.println("\nSala encontrada!");
                System.out.println("Tipo de sala: " + sala.getTipoSala());

            } else {
                System.out.println("\nSala não encontrada");
            } 
        } catch (SQLException e){
            System.out.println("\n[ERRO] Sala não encontrada: " + e.getMessage());
        }
    }

    public void atualizarSala(){
        System.out.println("\n---- Atualizar Saça -----");
        try{
            System.out.print("Digite o ID da sala a ser atualizada: ");
            int idSala = ConsoleUtil.lerInt(scanner);

            Sala sala = salaService.buscarSala(idSala);

            if (sala == null){
                System.out.println("sala não encontrada.");
                return;
            }

            System.out.println("\nDigite os novos dados. (Pressione ENTER caso não haja mudança)");

            System.out.print("Tipo atual " + sala.getTipoSala() + "\nNovo tipo: ");
            String tipoSala = scanner.nextLine();

            Sala upSala = sala;

            if (tipoSala != null && !tipoSala.trim().isEmpty()) {
                upSala.setTipoSala(tipoSala);
            } 

            salaService.atualizarSala(upSala);
        } catch (SQLException e){
            System.err.println("[ERRO NO BANCO DE DADOS] Não foi possível atualizar sala: " + e.getMessage());
        }
    }

    public void deletarSala(){
        System.out.println("\n---- Deletar Sala -----");
        try {
            System.out.print("Digite o ID da sala: ");
            int idSala = ConsoleUtil.lerInt(scanner);

            if (salaService.buscarSala(idSala) == null){
                System.out.println("Sala não encontrada");
                return;
            }

            System.out.print("Tem certeza que deseja deletar essa sala? (S/N)");
            System.out.println("Tipo de sala: " + salaService.buscarSala(idSala).getTipoSala());

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

