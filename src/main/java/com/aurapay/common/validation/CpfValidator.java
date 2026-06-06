package com.aurapay.common.validation;

/**
 * Valida CPF brasileiro. Espera o documento já normalizado (somente os 11 dígitos,
 * sem pontos ou traços) e confere os dois dígitos verificadores.
 */
public final class CpfValidator {

    private CpfValidator() {
    }

    public static boolean isValid(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        // Rejeita sequências repetidas (00000000000, 11111111111, ...), que passam no cálculo mas são inválidas.
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        int firstCheck = calculateCheckDigit(cpf, 9);
        int secondCheck = calculateCheckDigit(cpf, 10);

        return firstCheck == Character.getNumericValue(cpf.charAt(9))
                && secondCheck == Character.getNumericValue(cpf.charAt(10));
    }

    private static int calculateCheckDigit(String cpf, int length) {
        int sum = 0;
        int weight = length + 1;
        for (int i = 0; i < length; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * weight;
            weight--;
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
