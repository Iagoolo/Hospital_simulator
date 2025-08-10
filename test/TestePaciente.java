package test;

import dao.EnfermeiroDAO;
import dao.MedicoDAO;
import dao.PacienteDAO;
import model.Enfermeiro;
import model.Medico;
import model.Paciente;
import java.sql.Connection;
import java.sql.DriverManager;

public class TestePaciente {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/";
        String user = "";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PacienteDAO pacienteDAO = new PacienteDAO(conn);

            // Criar paciente de teste
            Paciente paciente = new Paciente();
            paciente.setCpf("12345678903");
            paciente.setNome("João da Silva");
            paciente.setEndereco("Rua das Flores, 123");
            paciente.setIdade(30);
            paciente.setNomePai("José da Silva");
            paciente.setNomeMae("Maria da Silva");
            paciente.setIdade(30); // Definindo idade do paciente
            //pacienteDAO.deletar("12345678903"); // Deletar paciente após teste

            // Buscar paciente para conferir
            Paciente p = pacienteDAO.buscarPorCpf("12345678903");
            System.out.println("Paciente encontrado: " + p.getNome());

            EnfermeiroDAO enfermeiroDAO = new EnfermeiroDAO(conn);
            Enfermeiro enfermeiro = new Enfermeiro();
            enfermeiro.setCpf("98765432100");
            enfermeiro.setNome("Maria Oliveira");
            enfermeiro.setEndereco("Avenida Brasil, 456");
            enfermeiro.setIdade(28);
            enfermeiro.setNomePai("Carlos Oliveira");
            enfermeiro.setNomeMae("Ana Oliveira");
            Enfermeiro e = enfermeiroDAO.buscarPorCpf("98765432100");
            System.out.println("Enfermeiro encontrado: " + e.getNome());
            
            MedicoDAO medicoDAO = new MedicoDAO(conn);
            Medico medico = new Medico();
            medico.setCpf("12312312312");
            medico.setNome("Dr. João");
            medico.setNomeMae("Maria João");
            medico.setNomePai("José João");
            medico.setEndereco("Rua do Médico, 789");
            medico.setIdade(40);

            Medico m = medicoDAO.buscarPorCpf("12312312312");
            System.out.println("Médico encontrado: " + m.getNome());

            for (Medico medic : medicoDAO.listarTodos()) {
                System.out.println("Médico: " + medic.getNome());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
