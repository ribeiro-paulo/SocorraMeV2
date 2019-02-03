package com.example.gladson.socorramev2.helper;

public class Validator {

    public static boolean isValidCPF(String CPF) {
        // Verifica se o CPF não é formado por uma sequência de números iguais.
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return (false);

        char dig10, dig11;
        int sm, i, r, num, height;

        // Calculo do 1o. Digito Verificador
        sm = 0;
        height = 10;

        // Converte cada elemento da string em inteiros.
        for (i = 0; i < 9; i++) {
            num = (CPF.charAt(i) - 48);
            sm = sm + (num * height);
            height = height - 1;
        }

        r = 11 - (sm % 11);
        if ((r == 10) || (r == 11)) dig10 = '0';
        else dig10 = (char) (r + 48);

        // Calculo do 2o. Digito Verificador
        sm = 0;
        height = 11;

        // Converte cada elemento da string em inteiros.
        for (i = 0; i < 10; i++) {
            num = (int) (CPF.charAt(i) - 48);
            sm = sm + (num * height);
            height = height - 1;
        }

        r = 11 - (sm % 11);
        if ((r == 10) || (r == 11)) dig11 = '0';
        else dig11 = (char) (r + 48);

        // Verifica se os digitos calculados conferem com os digitos informados.
        if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) return (true);
        else return (false);

    }
}
