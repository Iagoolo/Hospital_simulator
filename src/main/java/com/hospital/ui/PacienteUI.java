package com.hospital.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.hospital.model.Paciente;
import com.hospital.service.PacienteService;
import com.hospital.utils.ConsoleUtil;

public class PacienteUI extends BaseUI{
    
    private PacienteService pacienteService;

    public PacienteUI(PacienteService pacienteService, Scanner scanner){
        super(scanner);
        this.pacienteService = pacienteService;
    }

    @Override
    protected String obterTituloMenu(){
        return "Gerenciador de pacientes";
    }

    @Override
    protected void imprimirOpcoes(){
        System.out.println("1. Cadastrar Novo Paciente");
        System.out.println("2. Listar Todos os Pacientes");
        System.out.println("3. Buscar Paciente");
        System.out.println("4. Deletar Paciente");
        System.out.println("5. Atualizar Paciente");
        System.out.println("0. Voltar ao Menu Principal");
    }

    @Override
    protected boolean processarOpcao(int opcao){
        switch (opcao) {
            case 0:
                return false;
            
            case 1:
                cadastrarPaciente();
                return true;
            
            case 2:
                listarTodosPacientes();
                return true;
                
            case 3:
                buscarPaciente();
                return true;
                
            case 4:
                deletarPaciente();
                return true;
            
            case 5:
                atualizarPaciente();
                return true;

            default:
                return true;
            }
    }


    /**
     * Cadastra um novo paciente no sistema.
     * 
     * Este método solicita ao usuário informações necessárias para o cadastro de um paciente,
     * incluindo CPF, nome completo, endereço, idade, nome da mãe e nome do pai. Após coletar
     * essas informações, um objeto Paciente é criado e preenchido com os dados fornecidos.
     * Em seguida, o método chama o serviço de paciente para realizar o cadastro no banco de dados.
     * 
     * O método trata exceções relacionadas ao banco de dados e outras exceções inesperadas,
     * exibindo mensagens de erro apropriadas ao usuário.
     * 
     * @throws SQLException Se ocorrer um erro ao interagir com o banco de dados durante o cadastro.
     * @throws Exception Se ocorrer um erro inesperado durante a execução do método.
     */
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

    /**
     * Lista todos os pacientes cadastrados no sistema.
     * 
     * Este método recupera a lista de pacientes do serviço de pacientes e imprime
     * suas informações no console. Se não houver pacientes cadastrados, uma mensagem
     * informando que nenhum paciente está cadastrado será exibida. Em caso de erro
     * ao acessar o banco de dados, uma mensagem de erro será exibida no console.
     * 
     * Exibe as informações dos pacientes em um formato tabular com as colunas:
     * CPF, Nome e Endereço.
     * 
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
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

    /**
     * Busca um paciente pelo CPF fornecido pelo usuário.
     * 
     * Este método solicita ao usuário que insira o CPF de um paciente e tenta
     * localizar o paciente correspondente utilizando o serviço de paciente.
     * Se o paciente for encontrado, suas informações, como nome, idade,
     * nome da mãe, nome do pai, endereço e sintomas, são exibidas no console.
     * Caso contrário, uma mensagem informando que o paciente não foi encontrado
     * é exibida. Em caso de erro durante a busca, uma mensagem de erro é
     * exibida com detalhes sobre a exceção.
     * 
     * @throws SQLException Se ocorrer um erro ao buscar o paciente no banco de dados.
     */
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

    /**
     * Atualiza os dados de um paciente no sistema.
     * 
     * Este método solicita ao usuário o CPF do paciente a ser atualizado e, em seguida,
     * permite que o usuário insira novos dados, como nome, endereço, idade, nome da mãe
     * e nome do pai. Se o paciente não for encontrado, uma mensagem de erro será exibida.
     * Os campos que não forem alterados podem ser deixados em branco, e os dados existentes
     * serão mantidos. O método também lida com exceções relacionadas ao banco de dados.
     * 
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados durante a atualização.
     */
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

    /**
     * Deleta um paciente do sistema com base no CPF fornecido.
     * 
     * Este método solicita ao usuário que insira o CPF do paciente que deseja deletar.
     * Se o paciente não existir, uma mensagem de erro será exibida. Caso contrário,
     * o usuário será solicitado a confirmar a exclusão do paciente. Se a confirmação
     * for recebida, o paciente será deletado e uma mensagem de sucesso será exibida.
     * Se a operação for cancelada, uma mensagem informando o cancelamento será exibida.
     * 
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados durante a operação de exclusão.
     */
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
