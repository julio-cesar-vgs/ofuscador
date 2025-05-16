package com.suaempresa.ofuscadorlgpd.strategy.impl;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.strategy.EstrategiaOfuscacao;

/**
 * Estratégia de ofuscação que mascara parcialmente uma string.
 */
public class MascararParcialmenteStrategy implements EstrategiaOfuscacao {

    @Override
    public Object ofuscar(Object dado, DadosSensiveis config) {
        if (dado == null) {
            return null;
        }
        String valor = String.valueOf(dado);
        if (valor.isEmpty()) {
            return "";
        }

        int inicioVisivel = config.caracteresVisiveisInicio();
        int fimVisivel = config.caracteresVisiveisFim();
        char mascara = config.caractereMascaramento();
        int tamanhoTotal = valor.length();

        // Garante que os valores não sejam negativos
        if (inicioVisivel < 0) inicioVisivel = 0;
        if (fimVisivel < 0) fimVisivel = 0;

        // Se a soma dos caracteres visíveis for maior ou igual ao tamanho total
        if (inicioVisivel + fimVisivel >= tamanhoTotal) {
            // Se ambos são 0, mascara tudo. Caso contrário, retorna original pois não há o que mascarar no meio.
            if (inicioVisivel == 0 && fimVisivel == 0 && tamanhoTotal > 0) {
                return String.valueOf(mascara).repeat(tamanhoTotal);
            }
            return valor;
        }

        StringBuilder sb = new StringBuilder();

        // Parte inicial visível
        sb.append(valor.substring(0, inicioVisivel));

        // Parte mascarada
        int tamanhoMascarado = tamanhoTotal - inicioVisivel - fimVisivel;
        sb.append(String.valueOf(mascara).repeat(Math.max(0, tamanhoMascarado)));

        // Parte final visível
        if (fimVisivel > 0) {
            sb.append(valor.substring(tamanhoTotal - fimVisivel));
        }

        return sb.toString();
    }
}