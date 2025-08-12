package test;

import dao.ConsultaDAO;
import dao.EnfermeiroDAO;
import dao.MedicoDAO;
import dao.PacienteDAO;
import model.Consulta;
import model.Enfermeiro;
import model.Medico;
import model.Paciente;
import model.Triagem;
import dao.TriagemDAO;

import java.sql.Connection;
import java.sql.DriverManager;

public class Teste {
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
            Paciente p = pacienteDAO.buscarPorCpf("12345678901");
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

            TriagemDAO triagemDAO = new TriagemDAO(conn);
            Triagem triagem = new Triagem();
            triagem.setPrioridade("Alta");
            triagem.setDataTriagem(java.time.LocalDate.now());
            triagem.setHoraTriagem(java.time.LocalTime.now());
            triagem.setTemperatura(36.5);
            triagem.setPeso(70.0);
            triagem.setCpfPaciente("12345678901");
            triagem.setCpfEnfermeiro("98765432100");
            triagemDAO.add(triagem);

            Triagem t = triagemDAO.buscarTriagem(triagem.getIdTriagem());
            System.out.println("Triagem encontrada: " + t.getPrioridade());

            ConsultaDAO consultaDAO = new ConsultaDAO(conn);
            Consulta consulta = new Consulta();
            consulta.setIdTriagem(triagem.getIdTriagem());
            consulta.setSala(1);
            consulta.setDataConsulta(java.time.LocalDate.now());
            consulta.setHoraConsulta(java.time.LocalTime.now());
            consulta.setObservacao("Consulta de rotina");
            consulta.setDiagnostico("Saudável");
            consulta.setCpfPaciente("12345678901");
            consulta.setCpfMedico("12312312312");
            consultaDAO.addConsulta(consulta);

            Consulta c = consultaDAO.buscarConsulta(consulta.getIdConsulta());
            System.out.println("Consulta encontrada: " + c.getObservacao());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
