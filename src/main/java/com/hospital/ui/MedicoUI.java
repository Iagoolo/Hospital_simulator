package com.hospital.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.hospital.model.Medico;
import com.hospital.service.MedicoService;
import com.hospital.utils.ConsoleUtil;

public class MedicoUI extends BaseUI {
    
    private MedicoService medicoService;

    public MedicoUI(MedicoService medicoService, Scanner scanner){
        super(scanner);
        this.medicoService = medicoService;
    }

    @Override
    public void imprimirOpcoes(){
        System.out.println("1. Cadastrar Novo Médico");
        System.out.println("2. Listar Todos os Médicos");
        System.out.println("3. Buscar Médicos");
        System.out.println("4. Deletar Médico");
        System.out.println("5. Atualizar Médico");
        System.out.println("0. Voltar ao Menu Principal");
    }

    @Override
    public String obterTituloMenu(){
        return "Gerenciador de Médicos";
    }

    @Override
    public boolean processarOpcao(int opcao){
        switch (opcao) {
            case 0:
                return false;
            
            case 1:
                cadastrarMedico();
                return true;
        
            case 2:
                listarTodosMedicos();
                return true;

            case 3:
                buscarMedico();
                return true;

            case 4:
                deletarMedico();
                return true;
            case 5:
                atualizarMedico();
                return true;

            default:
                return true;
        }
    }  

    /**
     * Cadastra um novo médico no sistema.
     * 
     * Este método solicita ao usuário informações necessárias para o cadastro de um médico,
     * incluindo CPF, nome completo, endereço, idade, nome da mãe e nome do pai. 
     * Após coletar os dados, um objeto Medico é criado e preenchido com as informações fornecidas.
     * Em seguida, o método chama o serviço de cadastro para persistir os dados no banco de dados.
     * 
     * Caso ocorra um erro durante o processo de cadastro, o método captura exceções específicas
     * relacionadas ao banco de dados e outras exceções inesperadas, exibindo mensagens de erro apropriadas.
     * 
     * @throws SQLException Se ocorrer um erro ao interagir com o banco de dados.
     * @throws Exception Para qualquer outra exceção inesperada que possa ocorrer.
     */
    public void cadastrarMedico(){
        System.out.println("\n -----Cadastrar Novo Médico--------");

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

    /**
     * Lista todos os médicos cadastrados no sistema.
     * 
     * Este método recupera a lista de médicos através do serviço de médicos
     * e imprime suas informações no console. Se não houver médicos cadastrados,
     * uma mensagem informando que nenhum médico está cadastrado será exibida.
     * 
     * Em caso de erro ao acessar o banco de dados, uma mensagem de erro será
     * exibida no console.
     * 
     * Exibe as informações dos médicos no seguinte formato:
     * 
     * CPF            | Nome                           | Endereço
     * ------------------------------------------------------------
     * <cpf>         | <nome>                         | <endereço>
     * 
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
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

    /**
     * Busca um médico pelo CPF fornecido pelo usuário.
     * 
     * Este método solicita ao usuário que insira o CPF de um médico e tenta 
     * localizar as informações correspondentes. Se o médico for encontrado, 
     * exibe detalhes como nome, idade, nome da mãe, nome do pai, endereço 
     * e especialidades. Caso contrário, informa que o médico não foi encontrado.
     * 
     * O método lida com exceções de SQL que podem ocorrer durante a busca 
     * no banco de dados, exibindo uma mensagem de erro apropriada.
     * 
     * @throws SQLException Se ocorrer um erro ao buscar o médico no banco de dados.
     */
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

    /**
     * Atualiza as informações de um médico no sistema.
     * 
     * Este método solicita ao usuário o CPF do médico a ser atualizado e, em seguida, 
     * permite que o usuário insira novos dados, como nome, endereço, idade, nome da mãe 
     * e nome do pai. Se o usuário não fornecer novos dados, as informações existentes 
     * serão mantidas. O método também trata exceções relacionadas ao banco de dados 
     * e valida a entrada do usuário para a idade.
     * 
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados durante a atualização.
     */
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

    /**
     * Deleta um médico do sistema com base no CPF fornecido pelo usuário.
     * 
     * Este método solicita ao usuário que insira o CPF do médico que deseja deletar.
     * Se o médico não existir, uma mensagem de erro é exibida. Caso contrário, o nome
     * do médico é mostrado e o usuário é solicitado a confirmar a operação de deleção.
     * Se o usuário confirmar, o médico é deletado e uma mensagem de sucesso é exibida.
     * Se o usuário cancelar, uma mensagem de operação cancelada é exibida.
     * 
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados durante a operação de deleção.
     */
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
