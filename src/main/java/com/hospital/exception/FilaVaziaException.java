package com.hospital.exception;

/**
 * Exception lançada quando uma operação é tentada em uma fila vazia.
 */
public class FilaVaziaException extends Exception{
    public FilaVaziaException(String message){
        super(message);
    }
}
