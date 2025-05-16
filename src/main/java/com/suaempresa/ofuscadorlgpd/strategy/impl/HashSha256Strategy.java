package com.suaempresa.ofuscadorlgpd.strategy.impl;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.strategy.EstrategiaOfuscacao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Estratégia de ofuscação que aplica um hash SHA-256 ao valor do campo.
 */
public class HashSha256Strategy implements EstrategiaOfuscacao {

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public Object ofuscar(Object dado, DadosSensiveis config) {
        if (dado == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(String.valueOf(dado).getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erro crítico: Algoritmo SHA-256 não encontrado. " + e.getMessage());
            return "[ERRO AO GERAR HASH]"; // Retorna um placeholder em caso de erro
        }
    }
}