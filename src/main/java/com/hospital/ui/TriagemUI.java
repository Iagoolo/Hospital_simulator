package com.hospital.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import com.hospital.model.Atendimento;
import com.hospital.model.Paciente;
import com.hospital.model.Triagem;
import com.hospital.service.ServiceContainer;
import com.hospital.utils.ConsoleUtil;

public class TriagemUI extends BaseUI {

    private ServiceContainer services;

    public TriagemUI(Scanner scanner, ServiceContainer services) {
        super(scanner);
        this.services = services;
    }

    /**
     * Realiza o processo de triagem de um paciente no sistema.
     * Este método busca o próximo paciente aguardando triagem, coleta informações do enfermeiro responsável,
     * e registra os dados da triagem, incluindo peso, temperatura e prioridade.
     * 
     * O método executa as seguintes etapas:
     * 1. Busca o próximo paciente na fila de triagem.
     * 2. Verifica se o paciente existe; caso contrário, lança uma exceção.
     * 3. Solicita o CPF do enfermeiro responsável e verifica se ele está cadastrado.
     * 4. Coleta informações sobre o peso e a temperatura do paciente.
     * 5. Solicita a prioridade da triagem (Baixa, Média, Alta).
     * 6. Cria um objeto Triagem com as informações coletadas e registra a data e hora da triagem.
     * 7. Atualiza o status do atendimento do paciente para "Aguardando Consulta" e associa a triagem ao atendimento.
     * 
     * @throws Exception Se não houver pacientes aguardando triagem ou se o enfermeiro não for encontrado.
     */
    public void realizarTriagemWizard() {
        System.out.println("\n--- Realizar Próxima Triagem ---");

        executarAcao(() -> {
    
            Atendimento atendimento = services.atendimentoService.buscarProximoPaciente();
            
            if (atendimento == null) {
                throw new Exception("Não há pacientes esperando por uma triagem");
            }
            
            Paciente paciente = services.pacienteService.buscarPacienteCpf(atendimento.getCpfPaciente());
            String nomePaciente = (paciente != null) ? paciente.getNome() : "Paciente não encontrado (CPF: " + atendimento.getCpfPaciente() + ")";
            
            System.out.println("Chamando paciente: " + nomePaciente);

            System.out.print("CPF do Enfermeiro responsável: ");
            String cpfEnf = ConsoleUtil.lerString(scanner);

            if (services.enfermeiroService.buscarEnfermeiroCpf(cpfEnf) == null){
                throw new Exception("Enfermeiro com CPF " + cpfEnf + " não encontrado.");
            }

            System.out.print("Peso (kg): ");
            double peso = Double.parseDouble(ConsoleUtil.lerString(scanner));

            System.out.print("Temperatura (C): ");
            String tempSt = ConsoleUtil.lerString(scanner).replace(",", ".");
            double temp = Double.parseDouble(tempSt);

            String prioridade = priority();

            Triagem triagem = new Triagem();
            triagem.setCpfPaciente(atendimento.getCpfPaciente());
            triagem.setCpfEnfermeiro(cpfEnf);
            triagem.setPeso(peso);
            triagem.setTemperatura(temp);
            triagem.setPrioridade(prioridade);
            triagem.setDataTriagem(LocalDate.now());
            triagem.setHoraTriagem(LocalTime.now());

            Triagem triagemSalva = services.triagemService.realizarTriagem(triagem);

            atendimento.setStatus("Aguardando Consulta");
            atendimento.setIdTriagem(triagemSalva.getIdTriagem());
            
            services.atendimentoService.atualizarAtendimento(atendimento);

        }, "Triagem finalizada com sucesso! Paciente encaminhado para consulta.", "Erro na triagem");
    }

    /***
     * Função responsável por ler a prioridade de atendimento do paciente, transformando um valor passado de forma inteiro e retornando
     * como uma string: Alta, média, alta
     * @return string da prioridade
     */
    private String priority(){

        System.out.println("Digite a prioridade de atendimento de acordo com a tabela");
        while(true){
            System.out.println("1. Alta");
            System.out.println("2. Média");
            System.out.println("3. Baixa");
            
            int temp = ConsoleUtil.lerInt(scanner);

            switch (temp) {
                case 1:
                    return "Alta";
                
                case 2:
                    return "Média";
                
                case 3:
                    return "Baixa";
            
                default:
                    System.out.println("Valor inválido. Tente novamente");
                    break;
            }
        }
    }

    @Override protected String obterTituloMenu() { return "Módulo de Triagem"; }
    @Override protected void imprimirOpcoes() { }
    @Override protected boolean processarOpcao(int opcao) { return false; }
}