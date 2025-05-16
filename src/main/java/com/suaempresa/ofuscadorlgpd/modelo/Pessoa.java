package com.suaempresa.ofuscadorlgpd.modelo;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.anotacoes.Ofuscavel;
import com.suaempresa.ofuscadorlgpd.strategy.TipoEstrategia;

/**
 * Classe de exemplo para demonstrar a ofuscação de dados.
 * Esta classe É anotada com @Ofuscavel, portanto seus campos @DadosSensiveis serão processados.
 */
@Ofuscavel // Adiciona a anotação @Ofuscavel à classe Pessoa
public class Pessoa {

    @DadosSensiveis(estrategia = TipoEstrategia.SUBSTITUIR_POR_FIXO, valorFixo = "[NOME CONFIDENCIAL]")
    private String nomeCompleto;

    @DadosSensiveis(estrategia = TipoEstrategia.MASCARAR_PARCIALMENTE, caracteresVisiveisInicio = 3, caracteresVisiveisFim = 2, caractereMascaramento = 'X')
    private String cpf;

    @DadosSensiveis(estrategia = TipoEstrategia.HASH_SHA256)
    private String email;

    @DadosSensiveis(estrategia = TipoEstrategia.MASCARAR_PARCIALMENTE, caracteresVisiveisFim = 4, caractereMascaramento = '*')
    private String telefone;

    @DadosSensiveis(estrategia = TipoEstrategia.MASCARAR_PARCIALMENTE, caracteresVisiveisInicio = 0, caracteresVisiveisFim = 0, caractereMascaramento = 'Z')
    private String numeroCartao;

    private int idade; // Campo não sensível
    private String enderecoNaoSensivel; // Campo não sensível

    public Pessoa(String nomeCompleto, String cpf, String email, String telefone, String numeroCartao, int idade, String enderecoNaoSensivel) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.numeroCartao = numeroCartao;
        this.idade = idade;
        this.enderecoNaoSensivel = enderecoNaoSensivel;
    }

    // Getters para facilitar a verificação nos exemplos e testes
    public String getNomeCompleto() {
        return nomeCompleto;
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

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public int getIdade() {
        return idade;
    }

    public String getEnderecoNaoSensivel() {
        return enderecoNaoSensivel;
    }


    @Override
    public String toString() {
        return "Pessoa (@Ofuscavel) {" +
                "\n  nomeCompleto = '" + nomeCompleto + '\'' +
                ",\n  cpf = '" + cpf + '\'' +
                ",\n  email = '" + email + '\'' + // Será o hash
                ",\n  telefone = '" + telefone + '\'' +
                ",\n  numeroCartao = '" + numeroCartao + '\'' +
                ",\n  idade = " + idade +
                ",\n  enderecoNaoSensivel = '" + enderecoNaoSensivel + '\'' +
                "\n}";
    }
}