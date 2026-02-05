package com.hospital.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.hospital.model.Sala;
import com.hospital.service.SalaService;
import com.hospital.utils.ConsoleUtil;

/**
 * Classe responsável pela interface de usuário para gerenciar salas.
 */
public class SalasUI extends BaseUI{
    
    private SalaService salaService;

    public SalasUI(SalaService salaService, Scanner scanner){
        super(scanner);
        this.salaService = salaService;
    }
    
    @Override
    protected String obterTituloMenu(){
        return "Gerenciador de Salas";
    }

    @Override
    protected void imprimirOpcoes(){
        System.out.println("1. Cadastrar Nova Sala");
        System.out.println("2. Listar Todas as Salas");
        System.out.println("3. Buscar Salas");
        System.out.println("4. Deletar Sala");
        System.out.println("5. Atualizar Sala");
        System.out.println("0. Voltar ao Menu Principal");
    }

    protected boolean processarOpcao(int opcao){
        switch (opcao) {
            case 0:
                return false;

            case 1:
                cadastrarSala();
                return true;
                
            case 2:
                listarTodasSalas();
                return true;
                
            case 3:
                buscarSala();
                return true;
            
            case 4:
                deletarSala();
                return true;
                
            case 5:
                atualizarSala();
                return true;

            default:
                return true;
        }
    }
    
    /**
     * Método para cadastrar uma nova sala.
     */
    public void cadastrarSala(){
        System.out.println("\n -----Cadastro de Nova Sala--------");

        try{
            System.out.println("Andar da sala:");
            int andar = ConsoleUtil.lerInt(scanner);

            System.out.println("Tipo de sala");
            String tipoSala = ConsoleUtil.lerString(scanner);

            Sala sala = new Sala();
            sala.setAndar(andar);
            sala.setTipoSala(tipoSala);

            salaService.cadastrarSala(sala);

        } catch(SQLException s){
            System.err.println("\n[ERRO NO BANCO DE DADOS] Não foi possível cadastrar a sala.");
            System.err.println("Detalhes: " + s.getMessage());
        } catch(Exception e){
            System.err.println("\n[ERRO INESPERADO]" + e.getMessage());
        }
    }


    /**
     * Exibe uma lista formatada de todas as salas cadastradas no sistema.
     * 
     * Este método recupera todas as salas do banco de dados através do serviço
     * e as exibe em um formato tabular com colunas para ID, Andar e Tipo de Sala.
     * 
     * Se nenhuma sala estiver cadastrada, uma mensagem informativa será exibida.
     * Em caso de erro na conexão com o banco de dados, uma mensagem de erro será
     * apresentada com detalhes da exceção.
     * 
     * @throws SQLException Capturada internamente, exibindo mensagem de erro ao usuário
     *                      caso ocorra problema ao acessar o banco de dados
     */
    public void listarTodasSalas() {
        System.out.println("\n-----Lista de todos as salas cadastradas-----");
        try{
            List<Sala> salas = salaService.listarTodasSalas();

            if (salas.isEmpty()){
                System.out.println("Nenhuma sala cadastrada");
                return;
            }

            System.out.printf("%-15s | %-30s | %s%n", "ID", "Andar", "Tipo" );
            System.out.println("-".repeat(70));

            for (Sala sala : salas){
                System.out.printf("%-15s | %-30s | %s%n",
                    sala.getIdSala(),
                    sala.getAndar(),
                    sala.getTipoSala());
            }
        } catch (SQLException e){
            System.err.println("[ERRO NO BANCO DE DADOS] Não foi possível listar salas: " + e.getMessage());
        }
    }

    
    /**
     * Busca uma sala pelo ID fornecido pelo usuário.
     * 
     * Este método solicita ao usuário que insira o ID da sala e tenta 
     * recuperar a sala correspondente através do serviço de sala. 
     * Se a sala for encontrada, exibe o tipo da sala. Caso contrário, 
     * informa que a sala não foi encontrada. 
     * 
     * Em caso de erro durante a busca, como problemas de conexão com 
     * o banco de dados, uma mensagem de erro é exibida.
     * 
     * @throws SQLException Se ocorrer um erro ao buscar a sala no banco de dados.
     */
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


    /**
     * Atualiza os dados de uma sala existente no sistema.
     * 
     * Este método solicita ao usuário o ID da sala a ser atualizada, busca a sala
     * no banco de dados e permite ao usuário informar novos dados para atualização.
     * Se a sala não for encontrada, uma mensagem informativa é exibida.
     * Em caso de erro durante a atualização, como problemas de conexão com o banco de dados,
     * uma mensagem de erro é exibida com detalhes da exceção.
     * 
     * @throws SQLException Se ocorrer um erro ao atualizar a sala no banco de dados.
     */
    public void atualizarSala(){
        System.out.println("\n---- Atualizar Sala -----");
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

    /**
     * Deleta uma sala do sistema com base no ID fornecido pelo usuário.
     * 
     * Este método solicita ao usuário que insira o ID da sala a ser deletada,
     * verifica se a sala existe e pede confirmação antes de proceder com a
     * exclusão. Se a sala não for encontrada, uma mensagem informativa é exibida.
     * Em caso de erro durante a exclusão, como problemas de conexão com o banco
     * de dados, uma mensagem de erro é exibida com detalhes da exceção.
     * 
     * @throws SQLException Se ocorrer um erro ao deletar a sala no banco de dados.
     */
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

            try{
                if (confirmacao.equalsIgnoreCase("S")){
                    salaService.deletarSala(idSala);
                    System.out.println("Sala deletada com sucesso!");
                } else {
                    System.out.println("\nOperação Cancelada!");
                }
            } catch(Exception ep){
                System.err.println(ep);
            }

        } catch (SQLException e){
            System.err.println("[ERRO] Não foi possível deletar sala: " + e.getMessage());
        }
    }
}

