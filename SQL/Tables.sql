-- Tabela base para todas as pessoas no sistema.
CREATE TABLE Pessoa (
    CPF VARCHAR(11) PRIMARY KEY,
    Nome VARCHAR(150) NOT NULL,
    Endereco VARCHAR(255) NOT NULL,
    Idade INT NOT NULL,
    Nome_pai VARCHAR(150),
    Nome_mae VARCHAR(150) NOT NULL
);

-- Especialização de Pessoa.
CREATE TABLE Medico (
    CPF_medico VARCHAR(11) PRIMARY KEY,
    Turno VARCHAR(50),
    CONSTRAINT fk_medico_pessoa FOREIGN KEY (CPF_medico) REFERENCES Pessoa(CPF)
);

-- Especialização de Pessoa.
CREATE TABLE Enfermeiro (
    CPF_enfermeiro VARCHAR(11) PRIMARY KEY,
    CONSTRAINT fk_enfermeiro_pessoa FOREIGN KEY (CPF_enfermeiro) REFERENCES Pessoa(CPF)
);

-- Especialização de Pessoa.
CREATE TABLE Paciente (
    CPF_paciente VARCHAR(11) PRIMARY KEY,
    CONSTRAINT fk_paciente_pessoa FOREIGN KEY (CPF_paciente) REFERENCES Pessoa(CPF)
);

-- Tabela para o relacionamento N:M entre Médico e Especialização
CREATE TABLE Medico_Especializacao (
    CPF_medico VARCHAR(11) NOT NULL,
    Especializacao VARCHAR(100) NOT NULL,
    PRIMARY KEY (CPF_medico, Especializacao),
    CONSTRAINT fk_med_esp_medico FOREIGN KEY (CPF_medico) REFERENCES Medico(CPF_medico)
);

-- Tabela para registrar múltiplos sintomas por paciente
CREATE TABLE Paciente_Sintomas (
    CPF_paciente VARCHAR(11) NOT NULL,
    Sintomas VARCHAR(255) NOT NULL,
    PRIMARY KEY (CPF_paciente, Sintomas),
    CONSTRAINT fk_pac_sint_paciente FOREIGN KEY (CPF_paciente) REFERENCES Paciente(CPF_paciente)
);

-- Histórico médico é uma relação 1:1 com Paciente
CREATE TABLE Historico_Medico (
    id_historico SERIAL PRIMARY KEY,
    CPF_paciente VARCHAR(11) NOT NULL UNIQUE,
    observacoes TEXT,
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_historico VARCHAR(20) DEFAULT 'Ativo',
    CONSTRAINT fk_historico_paciente FOREIGN KEY (CPF_paciente) REFERENCES Paciente(CPF_paciente)
);

-- Tabela para triagem de pacientes
CREATE TABLE Triagem (
    id_triagem SERIAL PRIMARY KEY,
    Prioridade VARCHAR(50) NOT NULL,
    data_triagem DATE NOT NULL,
    hora_triagem TIME NOT NULL,
    temperatura DECIMAL(4, 2),
    peso DECIMAL(5, 2),
    CPF_enfermeiro VARCHAR(11) NOT NULL,
    CPF_paciente VARCHAR(11) NOT NULL,
    CONSTRAINT fk_triagem_enfermeiro FOREIGN KEY (CPF_enfermeiro) REFERENCES Enfermeiro(CPF_enfermeiro),
    CONSTRAINT fk_triagem_paciente FOREIGN KEY (CPF_paciente) REFERENCES Paciente(CPF_paciente)
);

-- Tabela para consultas médicas
CREATE TABLE Consulta (
    id_consulta SERIAL PRIMARY KEY,
    id_triagem INT,
    id_sala INT,
    data_consulta DATE NOT NULL,
    hora_consulta TIME NOT NULL,
    Observacao TEXT,
    Diagnostico TEXT,
    CPF_paciente VARCHAR(11) NOT NULL,
    CPF_medico VARCHAR(11) NOT NULL,
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (CPF_paciente) REFERENCES Paciente(CPF_paciente),
    CONSTRAINT fk_consulta_medico FOREIGN KEY (CPF_medico) REFERENCES Medico(CPF_medico),
    CONSTRAINT fk_consulta_triagem FOREIGN KEY (id_triagem) REFERENCES Triagem(id_triagem)
);

-- Tabela para prescrições médicas
CREATE TABLE Prescricao (
    id_prescricao SERIAL PRIMARY KEY,
    id_consulta INT NOT NULL,
    CONSTRAINT fk_presc_consulta FOREIGN KEY (id_consulta) REFERENCES Consulta(id_consulta) ON DELETE CASCADE
);

-- Tabela para medicamentos
CREATE TABLE Medicamentos (
    id_medicamento SERIAL PRIMARY KEY,
    Nome VARCHAR(100) NOT NULL,
    Formula VARCHAR(255),
    Forma VARCHAR(50),
    Via_administracao VARCHAR(50)
);

-- Tabela para itens de prescrição
CREATE TABLE Prescricao_Item (
    id_item SERIAL PRIMARY KEY,
    id_prescricao INT NOT NULL,
    id_medicamento INT NOT NULL,
    dosagem VARCHAR(50),
    frequencia VARCHAR(50),
    duracao VARCHAR(50),
    observacoes TEXT,
    FOREIGN KEY (id_prescricao) REFERENCES Prescricao(id_prescricao) ON DELETE CASCADE,
    FOREIGN KEY (id_medicamento) REFERENCES Medicamentos(id_medicamento)
);

-- Tabela para exames
CREATE TABLE Exames (
    id_exame SERIAL PRIMARY KEY,
    id_consulta INT NOT NULL,
    id_historico INT NOT NULL,
    Tipo VARCHAR(100) NOT NULL,
    Solicitado_em DATE NOT NULL DEFAULT CURRENT_DATE,
    Resultado TEXT,
    Data_resultado DATE,
    Status VARCHAR(50) DEFAULT 'Pendente',
    CONSTRAINT fk_exames_consulta FOREIGN KEY (id_consulta) REFERENCES Consulta(id_consulta) ON DELETE CASCADE
);

-- Tabela para salas
CREATE TABLE Sala (
    id_sala SERIAL PRIMARY KEY,
    Andar INT,
    Tipo_sala VARCHAR(50) 
);

-- Tabela para atendimentos
CREATE TABLE Atendimento (
    id_atendimento SERIAL PRIMARY KEY,
    cpf_paciente VARCHAR(11) NOT NULL,
    senha VARCHAR(10) NOT NULL,
    hora_atendimento TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'Aguardando',
    id_consulta INT,
    id_triagem INT,
    id_sala INT,
    
    CONSTRAINT fk_atendimento_consulta FOREIGN KEY (id_consulta) REFERENCES Consulta(id_consulta) ON DELETE CASCADE,
    CONSTRAINT fk_atendimento_triagem FOREIGN KEY (id_triagem) REFERENCES Triagem(id_triagem),
    CONSTRAINT fk_atendimento_sala FOREIGN KEY (id_sala) REFERENCES Sala(id_sala),
    CONSTRAINT fk_atendimento_paciente FOREIGN KEY (cpf_paciente) REFERENCES Paciente(cpf_paciente)
);