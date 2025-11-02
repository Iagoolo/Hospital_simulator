package com.hospital.ui;

import java.sql.SQLException;
import java.util.Scanner;

import com.hospital.exception.AcaoComExcecao;
import com.hospital.utils.ConsoleUtil;

public abstract class BaseUI {
    
    protected Scanner scanner;

    public BaseUI(Scanner scanner) {
        this.scanner = scanner;
    }

    public final void exibirMenu(){
        boolean executando = true;

        while(executando){
            System.out.println("\n---- " + obterTituloMenu() + " ----\n");
            imprimirOpcoes();
            int opcao = ConsoleUtil.lerInt(scanner);
            executando = processarOpcao(opcao);

            if (executando) {
                pausar();
            }
        }

        System.out.println("Voltando ao menu anterior...");
    }

    protected void pausar(){
        System.out.println("\nPressione ENTER para continuar");
        scanner.nextLine();
    }

    protected boolean confirmar(){
        System.out.println("Tem certeza que deseja realizar essa ação? (S/N)");
        return scanner.nextLine().equalsIgnoreCase("S");
    }

    protected void executarAcao(AcaoComExcecao acao, String mensagemSucesso, String mensagemErroBase) {
        try {
            acao.executar();
            if (mensagemSucesso != null && !mensagemSucesso.isEmpty()) {
                System.out.println("\n" + mensagemSucesso);
            }
        } catch (SQLException e) {
            System.err.println("\n[ERRO DE BANCO DE DADOS] " + mensagemErroBase);
            System.err.println("Detalhes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\n[ERRO] " + mensagemErroBase);
            if (e.getMessage() != null && !e.getClass().getName().equals(e.getMessage())) {
                 System.err.println("Motivo: " + e.getMessage());
            }
        }
    }

    protected abstract String obterTituloMenu();
    protected abstract void imprimirOpcoes();
    protected abstract boolean processarOpcao(int opcao);
}
