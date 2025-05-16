package com.suaempresa.ofuscadorlgpd;

import com.suaempresa.ofuscadorlgpd.core.ProcessadorOfuscacao;
import com.suaempresa.ofuscadorlgpd.modelo.Pessoa;
import com.suaempresa.ofuscadorlgpd.modelo.ProdutoNaoOfuscavel;

/**
 * Classe principal para demonstrar o uso da biblioteca de ofuscação de dados.
 */
public class MainExemplo {
    public static void main(String[] args) {
        ProcessadorOfuscacao processador = new ProcessadorOfuscacao();

        // --- Exemplo com Pessoa (anotada com @Ofuscavel) ---
        Pessoa pessoa1 = new Pessoa(
                "Maria Joaquina de Amaral Pereira Góes",
                "12345678900",
                "maria.joaquina@emailprivado.com",
                "11987654321",
                "4000111122223333",
                30,
                "Rua das Palmeiras, 123, Bairro Feliz"
        );

        System.out.println("--- Dados Originais (Pessoa --- @Ofuscavel) ---");
        System.out.println(pessoa1);
        try {
            processador.ofuscarDados(pessoa1); // Processador deve processar esta classe
            System.out.println("\n--- Dados Ofuscados (Pessoa --- @Ofuscavel) ---");
            System.out.println(pessoa1);
        } catch (IllegalAccessException e) {
            System.err.println("Erro ao tentar ofuscar dados da Pessoa: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n========================================\n");

        // --- Exemplo com ProdutoNaoOfuscavel (NÃO anotada com @Ofuscavel) ---
        ProdutoNaoOfuscavel produto = new ProdutoNaoOfuscavel(
                "XYZ-SECRET-789",
                "Notebook Super Potente",
                7500.99
        );
        String codigoOriginalProduto = produto.getCodigoSecreto(); // Salva para verificação

        System.out.println("--- Dados Originais (ProdutoNaoOfuscavel --- SEM @Ofuscavel) ---");
        System.out.println(produto);
        try {
            processador.ofuscarDados(produto); // Processador deve IGNORAR esta classe
            System.out.println("\n--- Dados Após Tentativa de Ofuscação (ProdutoNaoOfuscavel --- SEM @Ofuscavel) ---");
            System.out.println(produto);

            // Verificação explícita
            if (produto.getCodigoSecreto().equals(codigoOriginalProduto)) {
                System.out.println(">>> VERIFICAÇÃO: Código Secreto do Produto NÃO foi ofuscado, como esperado.");
            } else {
                System.err.println(">>> ERRO NA VERIFICAÇÃO: Código Secreto do Produto FOI ofuscado INDEVIDAMENTE.");
            }

        } catch (IllegalAccessException e) {
            // Não deve ocorrer neste caso, mas é bom manter
            System.err.println("Erro ao tentar processar ProdutoNaoOfuscavel: " + e.getMessage());
            e.printStackTrace();
        }
    }
}