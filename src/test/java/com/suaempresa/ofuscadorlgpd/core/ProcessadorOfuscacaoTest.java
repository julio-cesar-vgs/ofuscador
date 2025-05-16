package com.suaempresa.ofuscadorlgpd.core;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.anotacoes.Ofuscavel;
import com.suaempresa.ofuscadorlgpd.strategy.TipoEstrategia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testes para ProcessadorOfuscacao")
class ProcessadorOfuscacaoTest {

    private ProcessadorOfuscacao processador;

    @BeforeEach
    void setUp() {
        processador = new ProcessadorOfuscacao();
    }

    @Test
    @DisplayName("Deve ofuscar apenas em classe anotada")
    void testClasseOfuscavel() throws IllegalAccessException {
        UsuarioOfuscavel u = new UsuarioOfuscavel("A", "123456", "e@x.com", "11911", "DOC", null);
        processador.ofuscarDados(u);
        assertEquals("[NOME OCULTO]", u.getNome());
    }

    @Test
    @DisplayName("Nao ofuscar em classe nao anotada")
    void testClasseNaoOfuscavel() throws IllegalAccessException {
        UsuarioNaoOfuscavel u = new UsuarioNaoOfuscavel("A");
        processador.ofuscarDados(u);
        assertEquals("A", u.getNome());
    }

    @Ofuscavel
    static class UsuarioOfuscavel {
        @DadosSensiveis(estrategia = TipoEstrategia.MASCARAR_PARCIALMENTE, caracteresVisiveisInicio = 3, caracteresVisiveisFim = 2, caractereMascaramento = 'X')
        public String cpf;
        @DadosSensiveis(estrategia = TipoEstrategia.MASCARAR_PARCIALMENTE, caracteresVisiveisInicio = 1, caracteresVisiveisFim = 1, caractereMascaramento = '-')
        public String nuloAnotado;
        @DadosSensiveis(estrategia = TipoEstrategia.HASH_SHA256)
        protected String email;
        @DadosSensiveis(estrategia = TipoEstrategia.MASCARAR_PARCIALMENTE, caracteresVisiveisFim = 4)
        String telefone;
        @DadosSensiveis(estrategia = TipoEstrategia.SUBSTITUIR_POR_FIXO, valorFixo = "[NOME OCULTO]")
        private String nome;
        @DadosSensiveis(estrategia = TipoEstrategia.SUBSTITUIR_POR_FIXO)
        private String documento;

        public UsuarioOfuscavel(String nome, String cpf, String email, String telefone, String documento, String nuloAnotado) {
            this.nome = nome;
            this.cpf = cpf;
            this.email = email;
            this.telefone = telefone;
            this.documento = documento;
            this.nuloAnotado = nuloAnotado;
        }

        public String getNome() {
            return nome;
        }

        public String getCpf() {
            return cpf;
        }

        public String getEmail() {
            return email;
        }

        public String getTelefone() {
            return telefone;
        }

        public String getDocumento() {
            return documento;
        }

        public String getNuloAnotado() {
            return nuloAnotado;
        }
    }

    static class UsuarioNaoOfuscavel {
        @DadosSensiveis(estrategia = TipoEstrategia.SUBSTITUIR_POR_FIXO, valorFixo = "[NAO MUDAR]")
        private String nome;

        public UsuarioNaoOfuscavel(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }
    }
}
