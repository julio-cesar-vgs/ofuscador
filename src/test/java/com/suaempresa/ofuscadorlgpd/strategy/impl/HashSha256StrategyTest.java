package com.suaempresa.ofuscadorlgpd.strategy.impl;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.strategy.TipoEstrategia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para HashSha256Strategy")
class HashSha256StrategyTest {

    private HashSha256Strategy strategy;
    private DadosSensiveis mockConfig;

    @BeforeEach
    void setUp() {
        strategy = new HashSha256Strategy();
        mockConfig = new DadosSensiveis() { // Mock simples
            @Override public TipoEstrategia estrategia() { return TipoEstrategia.HASH_SHA256; }
            @Override public String valorFixo() { return ""; }
            @Override public int caracteresVisiveisInicio() { return 0; }
            @Override public int caracteresVisiveisFim() { return 0; }
            @Override public char caractereMascaramento() { return '*'; }
            @Override public Class<? extends Annotation> annotationType() { return DadosSensiveis.class; }
        };
    }

    private String calcularSha256Hex(String texto) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Test
    @DisplayName("Deve retornar nulo se o dado de entrada for nulo")
    void ofuscar_quandoDadoNulo_retornaNulo() {
        assertNull(strategy.ofuscar(null, mockConfig));
    }

    @Test
    @DisplayName("Deve gerar hash SHA-256 para uma string não vazia")
    void ofuscar_paraStringNaoVazia_retornaHashCorreto() throws NoSuchAlgorithmException {
        String entrada = "test_string_123";
        String esperado = calcularSha256Hex(entrada);
        assertEquals(esperado, strategy.ofuscar(entrada, mockConfig));
    }

    @Test
    @DisplayName("Deve gerar hash SHA-256 para uma string vazia")
    void ofuscar_paraStringVazia_retornaHashCorreto() throws NoSuchAlgorithmException {
        String entrada = "";
        // e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855 é o SHA-256 de uma string vazia
        String esperado = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        assertEquals(esperado, strategy.ofuscar(entrada, mockConfig));
    }

    @Test
    @DisplayName("Deve gerar hashes diferentes para strings diferentes")
    void ofuscar_paraStringsDiferentes_retornaHashesDiferentes() {
        String resultado1 = (String) strategy.ofuscar("texto1", mockConfig);
        String resultado2 = (String) strategy.ofuscar("texto2", mockConfig);
        assertNotNull(resultado1);
        assertNotNull(resultado2);
        assertNotEquals(resultado1, resultado2);
    }

    @Test
    @DisplayName("Deve gerar o mesmo hash para a mesma string")
    void ofuscar_paraMesmaString_retornaMesmoHash() {
        String entrada = "dados_sensiveis_aqui";
        String resultado1 = (String) strategy.ofuscar(entrada, mockConfig);
        String resultado2 = (String) strategy.ofuscar(entrada, mockConfig);
        assertEquals(resultado1, resultado2);
    }
}