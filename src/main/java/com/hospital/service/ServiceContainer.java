package com.hospital.service;

import java.sql.Connection;

/**
 * A classe ServiceContainer é responsável por instanciar e fornecer acesso a todos os serviços do sistema.
 * Ela atua como um contêiner centralizado para gerenciar as dependências dos serviços, garantindo que
 * cada serviço tenha acesso à conexão com o banco de dados necessária para suas operações.
 *
 * <p>Ao criar uma instância de ServiceContainer, é necessário fornecer uma conexão com o banco de dados,
 * que será compartilhada entre todos os serviços. Isso facilita a gestão das conexões e promove a
 * reutilização de recursos.</p>
 *
 * @author Iago Lô
 */
public class ServiceContainer {
    
    public final PacienteService pacienteService;
    public final MedicoService medicoService;
    public final EnfermeiroService enfermeiroService;
    public final SalaService salaService;
    public final AtendimentoService atendimentoService;
    public final TriagemService triagemService;
    public final ConsultaService consultaService;
    public final PrescricaoService prescricaoService;
    public final MedicamentosService medicamentosService;
    public final ExamesService examesService;
    public final HistoricoMedicoService historicoMedicoService;

    public ServiceContainer(Connection connection) {
        this.pacienteService = new PacienteService(connection);
        this.medicoService = new MedicoService(connection);
        this.enfermeiroService = new EnfermeiroService(connection);
        this.salaService = new SalaService(connection);
        this.atendimentoService = new AtendimentoService(connection);
        this.triagemService = new TriagemService(connection);
        this.consultaService = new ConsultaService(connection);
        this.prescricaoService = new PrescricaoService(connection);
        this.medicamentosService = new MedicamentosService(connection);
        this.examesService = new ExamesService(connection);
        this.historicoMedicoService = new HistoricoMedicoService(connection);
    }
}
