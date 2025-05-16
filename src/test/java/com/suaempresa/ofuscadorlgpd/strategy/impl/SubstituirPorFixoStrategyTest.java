package com.suaempresa.ofuscadorlgpd.strategy.impl;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.strategy.TipoEstrategia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para SubstituirPorFixoStrategy")
class SubstituirPorFixoStrategyTest {

    private SubstituirPorFixoStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SubstituirPorFixoStrategy();
    }

    private DadosSensiveis criarMockDadosSensiveis(String valorFixo) {
        return new DadosSensiveis() {
            @Override public TipoEstrategia estrategia() { return TipoEstrategia.SUBSTITUIR_POR_FIXO; }
            @Override public String valorFixo() { return valorFixo; }
            @Override public int caracteresVisiveisInicio() { return 0; }
            @Override public int caracteresVisiveisFim() { return 0; }
            @Override public char caractereMascaramento() { return '*'; }
            @Override public Class<? extends Annotation> annotationType() { return DadosSensiveis.class; }
        };
    }

    @Test
    @DisplayName("Deve substituir pelo valor fixo padrão da anotação se o config.valorFixo() for o default")
    void ofuscar_comValorOriginal_retornaValorFixoPadraoDaAnotacao() {
        // O valor padrão de valorFixo() na anotação @DadosSensiveis é "[DADO OFUSCADO]"
        DadosSensiveis config = criarMockDadosSensiveis("[DADO OFUSCADO]"); // Simula o default
        assertEquals("[DADO OFUSCADO]", strategy.ofuscar("qualquercoisa", config));
    }

    @Test
    @DisplayName("Deve substituir pelo valor fixo customizado especificado na configuração")
    void ofuscar_comValorOriginal_retornaValorFixoCustomizado() {
        String valorCustomizado = "[CONFIDENCIAL]";
        DadosSensiveis config = criarMockDadosSensiveis(valorCustomizado);
        assertEquals(valorCustomizado, strategy.ofuscar("dado_original_123", config));
    }

    @Test
    @DisplayName("Deve retornar valor fixo mesmo se o dado de entrada for nulo")
    void ofuscar_quandoDadoNulo_retornaValorFixo() {
        String valorCustomizado = "N/A (Dado Nulo)";
        DadosSensiveis config = criarMockDadosSensiveis(valorCustomizado);
        assertEquals(valorCustomizado, strategy.ofuscar(null, config));
    }

    @Test
    @DisplayName("Deve retornar valor fixo mesmo se o dado de entrada for string vazia")
    void ofuscar_quandoDadoStringVazia_retornaValorFixo() {
        DadosSensiveis config = criarMockDadosSensiveis("[VAZIO_OFUSCADO]");
        assertEquals("[VAZIO_OFUSCADO]", strategy.ofuscar("", config));
    }
}