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

    /**
     * Exibe um menu interativo ao usuário e processa as opções selecionadas.
     * 
     * Este método implementa um loop contínuo que:
     * <ul>
     *   <li>Exibe o título do menu obtido através do método {@link #obterTituloMenu()}</li>
     *   <li>Imprime as opções disponíveis através do método {@link #imprimirOpcoes()}</li>
     *   <li>Lê a entrada do usuário utilizando {@link ConsoleUtil#lerInt(Scanner)}</li>
     *   <li>Processa a opção selecionada através do método {@link #processarOpcao(int)}</li>
     *   <li>Pausa a execução entre iterações para melhor legibilidade</li>
     * </ul>
     * 
     * O loop continua enquanto o método {@link #processarOpcao(int)} retornar {@code true}.
     * Quando retorna {@code false}, o menu é encerrado e a mensagem "Voltando ao menu anterior..."
     * é exibida na tela.
     * 
     * @see #obterTituloMenu()
     * @see #imprimirOpcoes()
     * @see #processarOpcao(int)
     * @see #pausar()
     * @see ConsoleUtil#lerInt(Scanner)
     */
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


    /**
     * Pausa a execução do programa e aguarda a entrada do usuário.
     * 
     * Este método exibe uma mensagem na tela solicitando ao usuário que pressione
     * ENTER para continuar a execução. Útil para manter a visibilidade das informações
     * exibidas antes de prosseguir com a próxima operação.
     * 
     * O método bloqueia a execução até que o usuário pressione a tecla ENTER,
     * consumindo a próxima linha de entrada do Scanner.
     */
    protected void pausar(){
        System.out.println("\nPressione ENTER para continuar");
        scanner.nextLine();
    }

    /**
     * Solicita confirmação do usuário para realizar uma ação.
     * 
     * Este método exibe uma mensagem pedindo ao usuário para confirmar se deseja
     * prosseguir com a ação atual. A confirmação é obtida através da leitura de
     * uma linha de entrada do scanner, esperando que o usuário digite "S" ou "s"
     * para confirmar.
     * 
     * @return {@code true} se o usuário digitou "S" ou "s" (confirmação positiva),
     *         {@code false} caso contrário
     * 
     * @see Scanner#nextLine()
     */
    protected boolean confirmar(){
        System.out.println("Tem certeza que deseja realizar essa ação? (S/N)");
        return scanner.nextLine().equalsIgnoreCase("S");
    }

    /**
     * Executa uma ação encapsulada com tratamento de erros centralizado.
     * 
     * <p>Este método fornece um mecanismo centralizado para executar operações que podem lançar
     * exceções, tratando especificamente erros de banco de dados e exceções genéricas. 
     * Caso a execução seja bem-sucedida, uma mensagem de sucesso é exibida ao usuário.</p>
     * 
     * <p>O tratamento de erros funciona da seguinte forma:
     * <ul>
     *   <li><strong>SQLException:</strong> Erros de banco de dados são capturados e exibidos
     *       com a mensagem de erro base e os detalhes específicos da exceção.</li>
     *   <li><strong>Exception:</strong> Qualquer outra exceção é capturada e exibida com
     *       a mensagem de erro base e o motivo da exceção, se disponível.</li>
     * </ul>
     * </p>
     * 
     * @param acao a ação a ser executada. Deve implementar a interface {@link AcaoComExcecao}
     *             e conter a lógica que pode lançar exceções.
     * @param mensagemSucesso a mensagem a ser exibida caso a ação seja executada com sucesso.
     *                        Se {@code null} ou vazia, nenhuma mensagem será exibida.
     * @param mensagemErroBase a mensagem base a ser exibida em caso de erro, fornecendo contexto
     *                         sobre qual operação falhou.
     * 
     * @see AcaoComExcecao
     * @see SQLException
     */
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
