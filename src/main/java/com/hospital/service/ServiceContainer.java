package com.hospital.service;

import java.sql.Connection;

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
