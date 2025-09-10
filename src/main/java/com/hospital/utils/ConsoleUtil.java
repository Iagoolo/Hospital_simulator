package com.hospital.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUtil {
    
    public static int lerInt(Scanner scanner){
        while(true){
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e){
                System.err.println("Entrada inválida. Por favor, digite apenas números inteiros");
                scanner.nextLine();
                System.out.println("Tente novamente...");
            }
        }
    }

    public static String lerString(Scanner scanner){
        while(true){
            String texto = scanner.nextLine();
            if (texto != null && !texto.trim().isEmpty()){
                return texto.trim();
            }

            System.err.println("A entrada não pode ser vazia. Tente novamente:");
            System.out.print("> ");
        }
    }
}
