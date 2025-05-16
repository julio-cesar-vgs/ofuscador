package com.suaempresa.ofuscadorlgpd.strategy.impl;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.strategy.EstrategiaOfuscacao;

/**
 * Estratégia de ofuscação que substitui o valor original por um valor fixo.
 */
public class SubstituirPorFixoStrategy implements EstrategiaOfuscacao {

    @Override
    public Object ofuscar(Object dado, DadosSensiveis config) {
        // Mesmo que o dado original seja nulo, a substituição por valor fixo é aplicada.
        // Se o comportamento desejado for manter nulo se original for nulo, adicione: if (dado == null) return null;
        return config.valorFixo();
    }
}