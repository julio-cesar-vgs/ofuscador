package com.suaempresa.ofuscadorlgpd.strategy;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;

/**
 * Interface funcional para definir uma estratégia de ofuscação de dados.
 */
@FunctionalInterface
public interface EstrategiaOfuscacao {
    /**
     * Ofusca o dado de entrada com base na configuração fornecida.
     *
     * @param dado   O valor original do campo a ser ofuscado.
     * @param config A instância da anotação {@link DadosSensiveis} aplicada ao campo.
     * @return O valor ofuscado.
     */
    Object ofuscar(Object dado, DadosSensiveis config);
}