package com.suaempresa.ofuscadorlgpd.modelo;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.strategy.TipoEstrategia;

/**
 * Classe de exemplo para demonstrar que a ofuscação NÃO ocorre
 * se a classe não estiver anotada com @Ofuscavel, mesmo que tenha campos @DadosSensiveis.
 */
// NENHUMA ANOTAÇÃO @Ofuscavel AQUI
public class ProdutoNaoOfuscavel {

    @DadosSensiveis(estrategia = TipoEstrategia.SUBSTITUIR_POR_FIXO, valorFixo = "[CODIGO SECRETO OCULTO]")
    private String codigoSecreto; // Este campo NÃO será ofuscado

    private String nomeProduto;
    private double preco;

    public ProdutoNaoOfuscavel(String codigoSecreto, String nomeProduto, double preco) {
        this.codigoSecreto = codigoSecreto;
        this.nomeProduto = nomeProduto;
        this.preco = preco;
    }

    // Getters
    public String getCodigoSecreto() {
        return codigoSecreto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return "ProdutoNaoOfuscavel (SEM @Ofuscavel) {" +
                "\n  codigoSecreto = '" + codigoSecreto + '\'' + // Deve permanecer original
                ",\n  nomeProduto = '" + nomeProduto + '\'' +
                ",\n  preco = " + preco +
                "\n}";
    }
}