package com.suaempresa.ofuscadorlgpd.strategy.impl;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.strategy.TipoEstrategia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Testes para MascararParcialmenteStrategy")
class MascararParcialmenteStrategyTest {

    private MascararParcialmenteStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MascararParcialmenteStrategy();
    }

    // Método auxiliar para criar um mock da anotação DadosSensiveis
    private DadosSensiveis criarMockDadosSensiveis(int inicio, int fim, char mascara) {
        return new DadosSensiveis() {
            @Override
            public TipoEstrategia estrategia() {
                return TipoEstrategia.MASCARAR_PARCIALMENTE;
            }

            @Override
            public String valorFixo() {
                return "";
            }

            @Override
            public int caracteresVisiveisInicio() {
                return inicio;
            }

            @Override
            public int caracteresVisiveisFim() {
                return fim;
            }

            @Override
            public char caractereMascaramento() {
                return mascara;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return DadosSensiveis.class;
            }
        };
    }

    @Test
    @DisplayName("Deve retornar nulo se o dado de entrada for nulo")
    void ofuscar_quandoDadoNulo_retornaNulo() {
        DadosSensiveis config = criarMockDadosSensiveis(2, 2, '*');
        assertNull(strategy.ofuscar(null, config));
    }

    @Test
    @DisplayName("Deve retornar string vazia se o dado de entrada for uma string vazia")
    void ofuscar_quandoDadoStringVazia_retornaStringVazia() {
        DadosSensiveis config = criarMockDadosSensiveis(1, 1, '*');
        assertEquals("", strategy.ofuscar("", config));
    }

    @ParameterizedTest
    @CsvSource({
            // dadoEntrada, inicioVisivel, fimVisivel, mascara, esperado
            "1234567890, 3, 2, '*', '123*****90'",
            "abcdefghij, 2, 3, '#', 'ab#####hij'",
            "sensivel, 0, 0, 'X', 'XXXXXXXX'", // Mascarar tudo
            "curto, 5, 5, '*', 'curto'",      // inicio + fim >= tamanho
            "curto, 2, 0, '*', 'cu***'",      // Apenas início visível
            "curto, 0, 2, '*', '***to'",      // Apenas fim visível
            "1, 1, 0, '*', '1'",             // String de 1 char, mostrar início
            "1, 0, 1, '*', '1'",             // String de 1 char, mostrar fim
            "1, 0, 0, '*', '*'",             // String de 1 char, mascarar tudo
            "12, 1, 1, '*', '12'",           // String de 2 chars, mostrar tudo
            "12, 1, 0, '*', '1*'",           // String de 2 chars
            "12345, -1, 2, 'Z', 'ZZZ45'",    // inicioVisivel negativo tratado como 0
            "12345, 2, -2, 'Y', '12YYY'"     // fimVisivel negativo tratado como 0
    })
    @DisplayName("Deve mascarar parcialmente a string corretamente")
    void ofuscar_comDiferentesConfiguracoes_retornaMascaradoCorreto(
            String dadoEntrada, int inicioVisivel, int fimVisivel, char mascara, String esperado) {
        DadosSensiveis config = criarMockDadosSensiveis(inicioVisivel, fimVisivel, mascara);
        assertEquals(esperado, strategy.ofuscar(dadoEntrada, config));
    }

    @Test
    @DisplayName("Deve mascarar tudo se inicioVisivel e fimVisivel forem 0 e string não vazia")
    void ofuscar_quandoInicioEFimZeroEStringNaoVazia_mascaraTudo() {
        DadosSensiveis config = criarMockDadosSensiveis(0, 0, 'Z');
        assertEquals("ZZZZZZZZ", strategy.ofuscar("palavraZ", config));
    }

    @Test
    @DisplayName("Deve retornar original se inicio + fim >= tamanho e algum for > 0 (ou ambos)")
    void ofuscar_quandoSomaVisiveisMaiorOuIgualTamanhoEAlgumMaiorZero_retornaOriginal() {
        DadosSensiveis config = criarMockDadosSensiveis(3, 3, '*'); // 3+3 = 6, tamanho = 5
        assertEquals("teste", strategy.ofuscar("teste", config));

        DadosSensiveis config2 = criarMockDadosSensiveis(5, 0, '*'); // 5+0 = 5, tamanho = 5
        assertEquals("teste", strategy.ofuscar("teste", config2));

        DadosSensiveis config3 = criarMockDadosSensiveis(0, 5, '*'); // 0+5 = 5, tamanho = 5
        assertEquals("teste", strategy.ofuscar("teste", config3));
    }
}