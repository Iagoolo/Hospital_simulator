package com.hospital.ui;

import java.util.List;
import java.util.Scanner;

import com.hospital.model.Medicamento;
import com.hospital.service.MedicamentosService;
import com.hospital.utils.ConsoleUtil;

// Classe responsável por gerenciar a interface de medicamentos no simulador hospitalar
public class MedicamentosUI extends BaseUI {

    private MedicamentosService medicamentosService;
    
    public MedicamentosUI(MedicamentosService medicamentosService, Scanner scanner) {
        super(scanner);
        this.medicamentosService = medicamentosService;
    }
    @Override
    protected String obterTituloMenu() {
        return "Gerenciador de Medicamentos";
    }

    @Override
    protected void imprimirOpcoes() {
        System.out.println("1. Cadastrar Novo Medicamento");
        System.out.println("2. Listar Todos os Medicamentos");
        System.out.println("3. Buscar Medicamento");
        System.out.println("4. Deletar Medicamento");
        System.out.println("5. Atualizar Medicamento");
        System.out.println("0. Voltar ao Menu Principal");
    }

    @Override
    protected boolean processarOpcao(int opcao) {
        switch (opcao) {
            case 0:
                return false;
            
            case 1:
                cadastrarMedicamento();
                return true;
        
            case 2:
                listarMedicamentos();
                return true;

            case 3:
                deletarMedicamento();
                return true;
            case 4:
                atualizarMedicamento();
                return true;

            default:
                return true;
        }
    }

    /** 
     * Cadastra um novo medicamento no sistema.
     * 
     * Este método solicita ao usuário informações necessárias para o cadastro de um medicamento,
     * incluindo nome comercial, fórmula/composição, forma farmacêutica e via de administração.
     * Após coletar os dados, um objeto Medicamento é criado e preenchido com as informações fornecidas.
     * Em seguida, o método chama o serviço de cadastro para persistir os dados no banco de dados.
     * 
     * Caso ocorra um erro durante o processo de cadastro, o método captura exceções específicas
     * relacionadas ao banco de dados e outras exceções inesperadas, exibindo mensagens de erro apropriadas.
     */
    public void cadastrarMedicamento() {
        System.out.println("--- Cadastrar Novo Medicamento ---");
        
        executarAcao(() -> {
            System.out.print("Nome Comercial (ex: Novalgina): ");
            String nome = ConsoleUtil.lerString(scanner);

            System.out.print("Fórmula/Composição (ex: Dipirona 500mg): ");
            String formula = ConsoleUtil.lerString(scanner);

            System.out.print("Forma Farmacêutica (ex: Comprimido, Xarope): ");
            String forma = ConsoleUtil.lerString(scanner);

            System.out.print("Via de Administração (ex: Oral, Intravenosa): ");
            String via = ConsoleUtil.lerString(scanner);

            Medicamento med = new Medicamento();
            med.setNome(nome);
            med.setFormula(formula);
            med.setForma(forma);
            med.setViaAdministracao(via);

            medicamentosService.cadastrarMedicamentos(med);

        }, "Medicamento cadastrado com sucesso!", "Erro ao cadastrar medicamento");
    }

    /**
     * Lista todos os medicamentos cadastrados no sistema.
     * 
     * Este método recupera a lista de medicamentos através do serviço de medicamentos
     * e imprime suas informações no console. Se não houver medicamentos cadastrados,
     * uma mensagem informando que nenhum medicamento está cadastrado será exibida.
     * 
     * Em caso de erro ao acessar o banco de dados, uma mensagem de erro será
     * exibida no console.
     */
    private void listarMedicamentos() {
        System.out.println("\n--- Estoque de Medicamentos ---");

        try {
            List<Medicamento> lista = medicamentosService.listarMedicamentos();
            
            if (lista.isEmpty()) {
                System.out.println("Nenhum medicamento cadastrado.");
                return;
            }

            // Cabeçalho ajustado para seus campos
            System.out.printf("%-4s | %-15s | %-25s | %-15s | %-15s%n", 
                "ID", "Nome", "Fórmula", "Forma", "Via");
            System.out.println("-".repeat(85));
            
            for (Medicamento m : lista) {
                System.out.printf("%-4d | %-15s | %-25s | %-15s | %-15s%n", 
                    m.getIdMedicamento(), 
                    truncate(m.getNome(), 15), 
                    truncate(m.getFormula(), 25), 
                    truncate(m.getForma(), 15),
                    truncate(m.getViaAdministracao(), 15));
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        }
    }

    /** 
     * Atualiza as informações de um medicamento existente.
     * Este método solicita ao usuário o ID do medicamento que deseja atualizar e verifica se ele existe no sistema.
     * Se o medicamento for encontrado, o usuário é solicitado a fornecer os novos dados para o medicamento, como nome, fórmula, forma farmacêutica e via de administração.
     * O método então atualiza o objeto Medicamento com as novas informações e chama o serviço de atualização para persistir as mudanças no banco de dados.
     * @throws Exception Se o medicamento com o ID fornecido não for encontrado ou se ocorrer um erro durante a atualização, uma mensagem de erro apropriada será exibida.
     */
    private void atualizarMedicamento() {
        System.out.println("\n--- Atualizar Medicamento ---");
        
        executarAcao(() -> {
            System.out.print("Digite o ID do medicamento a ser atualizado: ");
            int id = ConsoleUtil.lerInt(scanner);

            // Verifica se existe antes de pedir os dados
            Medicamento medExistente = medicamentosService.buscarMedicamento(id);
            if (medExistente == null) {
                throw new Exception("Medicamento com ID " + id + " não encontrado.");
            }

            System.out.println("Medicamento selecionado: " + medExistente.getNome());
            System.out.println("--- Digite os novos dados ---");

            System.out.print("Novo Nome (Atual: " + medExistente.getNome() + "): ");
            String nome = ConsoleUtil.lerString(scanner);

            System.out.print("Nova Fórmula (Atual: " + medExistente.getFormula() + "): ");
            String formula = ConsoleUtil.lerString(scanner);

            System.out.print("Nova Forma (Atual: " + medExistente.getForma() + "): ");
            String forma = ConsoleUtil.lerString(scanner);

            System.out.print("Nova Via (Atual: " + medExistente.getViaAdministracao() + "): ");
            String via = ConsoleUtil.lerString(scanner);

            // Atualiza o objeto
            medExistente.setNome(nome);
            medExistente.setFormula(formula);
            medExistente.setForma(forma);
            medExistente.setViaAdministracao(via);

            medicamentosService.atualizarMedicamento(medExistente);

        }, "Medicamento atualizado com sucesso!", "Erro ao atualizar medicamento");
    }

    private void deletarMedicamento() {
        System.out.println("\n--- Deletar Medicamento ---");
        
        executarAcao(() -> {
            System.out.print("Digite o ID do medicamento a ser excluído: ");
            int id = ConsoleUtil.lerInt(scanner);

            Medicamento med = medicamentosService.buscarMedicamento(id);
            if (med == null) {
                throw new Exception("Medicamento não encontrado.");
            }

            System.out.println("Você vai deletar: " + med.getNome() + " (" + med.getFormula() + ")");
            System.out.print("Tem certeza? (S/N): ");
            String confirmacao = ConsoleUtil.lerString(scanner);

            if (!confirmacao.equalsIgnoreCase("S")) {
                throw new Exception("Operação cancelada pelo usuário.");
            }

            // AVISO: Se o medicamento já estiver em alguma receita (Prescrição),
            // o banco de dados vai bloquear a exclusão (Foreign Key Constraint).
            // Isso é o comportamento correto!
            medicamentosService.deletarMedicamento(id);

        }, "Medicamento excluído com sucesso!", "Erro ao deletar medicamento");
    }

    // Método auxiliar para cortar strings longas na tabela
    private String truncate(String str, int width) {
        if (str != null && str.length() > width) {
            return str.substring(0, width - 3) + "...";
        }
        return str != null ? str : "";
    }
}
