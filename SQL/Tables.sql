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
    Alergias TEXT,
    Historico_exames TEXT,
    Historico_familiar TEXT,
    Consultas_realizadas TEXT,
    Vacinas TEXT,
    CONSTRAINT fk_hist_med_paciente FOREIGN KEY (CPF_paciente) REFERENCES Paciente(CPF_paciente)
);

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

CREATE TABLE Consulta (
    id_consulta SERIAL PRIMARY KEY,
    data_consulta DATE NOT NULL,
    hora_consulta TIME NOT NULL,
    Sala INT,
    Observacao TEXT,
    Diagnostico TEXT,
    CPF_paciente VARCHAR(11) NOT NULL,
    CPF_medico VARCHAR(11) NOT NULL,
    id_triagem INT,
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (CPF_paciente) REFERENCES Paciente(CPF_paciente),
    CONSTRAINT fk_consulta_medico FOREIGN KEY (CPF_medico) REFERENCES Medico(CPF_medico),
    CONSTRAINT fk_consulta_triagem FOREIGN KEY (id_triagem) REFERENCES Triagem(id_triagem)
);

CREATE TABLE Prescricao (
    id_prescricao SERIAL PRIMARY KEY,
    id_consulta INT NOT NULL,
    CONSTRAINT fk_presc_consulta FOREIGN KEY (id_consulta) REFERENCES Consulta(id_consulta),
    CONSTRAINT fk_presc_medicamento FOREIGN KEY (id_medicamento) REFERENCES Medicamentos(id_medicamento)
);

CREATE TABLE Medicamentos (
    id_medicamento SERIAL PRIMARY KEY,
    Nome VARCHAR(100) NOT NULL,
    Formula VARCHAR(255),
    Forma VARCHAR(50),
    Via_administracao VARCHAR(50)
);

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

CREATE TABLE Exames (
    id_exame SERIAL PRIMARY KEY,
    id_consulta INT NOT NULL,
    Tipo VARCHAR(100) NOT NULL,
    Resultado TEXT,
    Data DATE,
    CONSTRAINT fk_exames_consulta FOREIGN KEY (id_consulta) REFERENCES Consulta(id_consulta)
);

-- Tabela de Salas. Uma sala existe independentemente de uma chamada.
CREATE TABLE Sala (
    id_sala SERIAL PRIMARY KEY,
    Numero VARCHAR(10) NOT NULL,
    Andar INT,
    Corredor VARCHAR(10),
    Tipo_sala VARCHAR(50) 
);

CREATE TABLE Chamada (
    id_chamada SERIAL PRIMARY KEY,
    Senha VARCHAR(10) NOT NULL,
    Hora_chamada TIME NOT NULL,
    Status VARCHAR(20) DEFAULT 'Aguardando',
    id_consulta INT,
    id_triagem INT,
    id_sala INT NOT NULL,
    CONSTRAINT fk_chamada_consulta FOREIGN KEY (id_consulta) REFERENCES Consulta(id_consulta),
    CONSTRAINT fk_chamada_triagem FOREIGN KEY (id_triagem) REFERENCES Triagem(id_triagem),
    CONSTRAINT fk_chamada_sala FOREIGN KEY (id_sala) REFERENCES Sala(id_sala)
);