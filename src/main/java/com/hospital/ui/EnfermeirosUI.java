package com.hospital.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.hospital.model.Enfermeiro;
import com.hospital.service.EnfermeiroService;
import com.hospital.utils.ConsoleUtil;

public class EnfermeirosUI extends BaseUI{
    
    private EnfermeiroService enfermeiroService;

    public EnfermeirosUI(EnfermeiroService enfermeiroService, Scanner scanner){
        super(scanner);
        this.enfermeiroService = enfermeiroService;
    }

    @Override
    protected String obterTituloMenu(){
        return "Gerenciador de Enfermeiros";
    }

    @Override
    protected void imprimirOpcoes(){
        System.out.println("1. Cadastrar Novo Enfermeiro");
        System.out.println("2. Listar Todos os Enfermeiros");
        System.out.println("3. Buscar Enfermeiro");
        System.out.println("4. Deletar Enfermeiro");
        System.out.println("5. Atualizar Enfermeiro");
        System.out.println("0. Voltar ao Menu Principal");
    }

    @Override
    protected boolean processarOpcao(int opcao){
        switch (opcao) {
            case 0:
                return false;

            case 1:
                cadastrarEnfermeiro();
                return true;
                
            case 2:
                listarTodosEnfermeiros();
                return true;
                
            case 3:
                buscarEnfermeiro();
                return true;
                
            case 4:
                deletarEnfermeiro();
                return true;
                
            case 5:
                atualizarEnfermeiro();
                return true;

            default:
                return false;
        }
    }

    /**
     * Cadastra um novo enfermeiro no sistema.
     * 
     * Este método solicita ao usuário informações necessárias para o cadastro de um enfermeiro,
     * incluindo CPF, nome completo, endereço, idade, nome da mãe e nome do pai. As informações
     * são lidas a partir da entrada do console. Após coletar os dados, um objeto Enfermeiro é
     * criado e preenchido com as informações fornecidas. O enfermeiro é então cadastrado através
     * do serviço de enfermeiro.
     * 
     * Em caso de erro ao interagir com o banco de dados, uma mensagem de erro específica é exibida.
     * Para outros tipos de exceções, uma mensagem de erro genérica é apresentada.
     * 
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados durante o cadastro.
     * @throws Exception Para qualquer outro erro inesperado que possa ocorrer durante a execução.
     */
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

    /**
     * Lista todos os enfermeiros cadastrados no sistema.
     * 
     * Este método recupera uma lista de enfermeiros através do serviço de enfermeiro
     * e imprime suas informações no console. Se não houver enfermeiros cadastrados,
     * uma mensagem informando que a lista está vazia será exibida. Em caso de erro
     * ao acessar o banco de dados, uma mensagem de erro será exibida.
     * 
     * Exibe as seguintes informações para cada enfermeiro:
     * - CPF
     * - Nome
     * - Endereço
     * 
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
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

    /**
     * Busca um enfermeiro pelo CPF fornecido pelo usuário.
     * 
     * Este método solicita ao usuário que insira o CPF de um enfermeiro e tenta 
     * localizar o enfermeiro correspondente utilizando o serviço de enfermeiros. 
     * Se o enfermeiro for encontrado, suas informações (nome, idade, nome da mãe, 
     * nome do pai e endereço) são exibidas no console. Caso contrário, uma mensagem 
     * informando que o enfermeiro não foi encontrado é exibida. 
     * 
     * Em caso de erro durante a busca (por exemplo, problemas de conexão com o banco 
     * de dados), uma mensagem de erro é exibida.
     * 
     * @throws SQLException Se ocorrer um erro ao acessar os dados do enfermeiro.
     */
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

    /**
     * Atualiza as informações de um enfermeiro no sistema.
     * 
     * Este método solicita ao usuário o CPF do enfermeiro a ser atualizado e, em seguida, 
     * permite que o usuário insira novos dados para o enfermeiro, como nome, endereço, 
     * idade, nome da mãe e nome do pai. Se o usuário não fornecer novos dados, os dados 
     * existentes serão mantidos. O método também trata exceções relacionadas ao banco de dados 
     * e exibe mensagens apropriadas em caso de erro.
     * 
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados durante a atualização.
     */
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

    /**
     * Deleta um enfermeiro do sistema com base no CPF fornecido pelo usuário.
     * 
     * Este método solicita ao usuário que insira o CPF do enfermeiro que deseja deletar.
     * Se o enfermeiro não existir, uma mensagem de erro será exibida. Caso contrário,
     * o método pedirá confirmação do usuário antes de proceder com a exclusão.
     * Se o usuário confirmar a exclusão, o enfermeiro será removido do sistema e
     * uma mensagem de sucesso será exibida. Se a operação for cancelada, uma mensagem
     * informando que a operação foi cancelada será exibida.
     * 
     * @throws SQLException Se ocorrer um erro ao tentar deletar o enfermeiro do banco de dados.
     */
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

