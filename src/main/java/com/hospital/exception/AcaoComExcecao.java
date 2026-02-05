package com.hospital.exception;

/* Interface funcional para ações que podem lançar exceções */
@FunctionalInterface
public interface AcaoComExcecao {
    void executar() throws Exception;
} 
