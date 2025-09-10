package test;

import java.sql.Connection;
import java.sql.DriverManager;

import com.hospital.dao.AtendimentoDAO;
import com.hospital.dao.ConsultaDAO;
import com.hospital.dao.EnfermeiroDAO;
import com.hospital.dao.ExamesDAO;
import com.hospital.dao.HistoricoMedicoDAO;
import com.hospital.dao.MedicamentosDAO;
import com.hospital.dao.MedicoDAO;
import com.hospital.dao.PacienteDAO;
import com.hospital.dao.PrescricaoDAO;
import com.hospital.dao.SalaDAO;
import com.hospital.dao.TriagemDAO;
import com.hospital.model.Atendimento;
import com.hospital.model.Consulta;
import com.hospital.model.Enfermeiro;
import com.hospital.model.Exames;
import com.hospital.model.HistoricoMedico;
import com.hospital.model.ItemPrescricao;
import com.hospital.model.Medicamento;
import com.hospital.model.Medico;
import com.hospital.model.Paciente;
import com.hospital.model.Prescricao;
import com.hospital.model.Sala;
import com.hospital.model.Triagem;

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
            paciente.setIdade(30);
            pacienteDAO.add(paciente);
            // Definindo idade do paciente
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
            enfermeiroDAO.add(enfermeiro);
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
            medicoDAO.add(medico);

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
            triagem.setCpfPaciente("12345678903");
            triagem.setCpfEnfermeiro("98765432100");
            triagemDAO.add(triagem);

            for (Triagem t : triagemDAO.listarTriagens()) {
                System.out.println("Triagem encontrada: " + t.getPrioridade() + " " + t.getIdTriagem());
            }

            Atendimento atendimento = new Atendimento();
            atendimento.setHoraAtendimento(java.sql.Time.valueOf(java.time.LocalTime.now()));
            atendimento.setStatus("Em atendimento");
            atendimento.setSenha("123456");
            atendimento.setCpfPaciente(paciente.getCpf());
            AtendimentoDAO atendimentoDAO = new AtendimentoDAO(conn);
            atendimentoDAO.add(atendimento);

            Sala sala = new Sala();
            sala.setTipoSala("Consultório");
            sala.setAndar(1);
            SalaDAO salaDAO = new SalaDAO(conn);
            salaDAO.add(sala);
            atendimento.setIdSala(sala.getIdSala());
            System.out.println(atendimento.getIdSala());

            ConsultaDAO consultaDAO = new ConsultaDAO(conn);
            Consulta consulta = new Consulta();
            consulta.setIdTriagem(triagem.getIdTriagem());
            consulta.setSala(1);
            consulta.setDataConsulta(java.time.LocalDate.now());
            consulta.setHoraConsulta(java.time.LocalTime.now());
            consulta.setObservacao("Consulta de rotina");
            consulta.setDiagnostico("Saudável");
            consulta.setCpfPaciente("12345678903");
            consulta.setCpfMedico("12312312312");
            consultaDAO.add(consulta);

            Consulta c = consultaDAO.buscarConsulta(consulta.getIdConsulta());
            System.out.println("Consulta encontrada: " + c.getObservacao());

            Medicamento medicamento = new Medicamento();
            medicamento.setIdMedicamento(1);
            medicamento.setNome("Dipirona");
            medicamento.setForma("Comprimido");
            medicamento.setFormula("C4H5NO2");
            medicamento.setViaAdministracao("Oral");
            MedicamentosDAO medicamentosDAO = new MedicamentosDAO(conn);
            medicamentosDAO.add(medicamento);

            Prescricao prescricao = new Prescricao();
            prescricao.setIdConsulta(c.getIdConsulta());
            c.setPrescricao(prescricao);
            ItemPrescricao item = new ItemPrescricao();
            item.setIdMedicamento(medicamento.getIdMedicamento());
            item.setDosagem("500mg");
            item.setFrequencia("2x ao dia");
            item.setDuracao("7 dias");
            item.setObservacoes("Tomar após as refeições");
            prescricao.addItem(item);

            PrescricaoDAO prescricaoDAO = new PrescricaoDAO(conn);
            prescricaoDAO.add(prescricao);
            consultaDAO.atualizarConsulta(c);
            System.out.println("Consulta atualizada: " + c.getIdConsulta() + " " + c.getPrescricao().getIdPrescricao());

            System.out.println("Item adicionado à prescrição: " + item.getIdMedicamento());
            System.out.println("Prescrição atualizada: " + prescricao.getIdPrescricao());

            atendimentoDAO.atualizarSalaEStatus(atendimento.getIdAtendimento(), sala.getIdSala(), atendimento.getSenha());
            atendimento.setHoraAtendimento(java.sql.Time.valueOf(java.time.LocalTime.now()));
            atendimento.setIdConsulta(c.getIdConsulta());
            atendimento.setIdTriagem(triagem.getIdTriagem());
            atendimento.setCpfPaciente(c.getCpfPaciente());
            atendimentoDAO.finalizarAtendimento(atendimento.getIdAtendimento());

            System.out.println("Atendimento finalizado: " + atendimento.getIdAtendimento());

            HistoricoMedico historico = new HistoricoMedico();
            historico.setCpfPaciente(atendimento.getCpfPaciente());
            historico.setObservacoes("Paciente atendido com sucesso");
            historico.setStatusHistorico("Ativo");
            historico.setUltimaAtualizacao(new java.sql.Date(System.currentTimeMillis()));
            HistoricoMedicoDAO historicoDAO = new HistoricoMedicoDAO(conn);
            historicoDAO.add(historico);
            System.out.println("Histórico adicionado: " + historico.getCpfPaciente());
            

            Exames exame = new Exames();
            exame.setIdConsulta(consulta.getIdConsulta());
            exame.setTipo("Sangue");
            exame.setSolicitadoEm(java.sql.Date.valueOf(java.time.LocalDate.now()));
            exame.setResultado("Normal");
            exame.setIdHistorico(historico.getIdHistorico());
            ExamesDAO examesDAO = new ExamesDAO(conn);
            examesDAO.add(exame);

            historico.addExame(exame);

           historicoDAO.buscarHistoricoPorPaciente("12345678903");

            HistoricoMedico historico2 = historicoDAO.buscarHistoricoPorPaciente("12345678903");
            System.out.println("Histórico encontrado: " + historico2.getCpfPaciente() + " " + historico2.getObservacoes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
