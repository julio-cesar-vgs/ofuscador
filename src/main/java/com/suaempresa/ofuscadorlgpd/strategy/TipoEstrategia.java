package com.suaempresa.ofuscadorlgpd.strategy;

/**
 * Enum que define os tipos de estratégias de ofuscação disponíveis.
 */
public enum TipoEstrategia {
    /**
     * Mascara parcialmente uma string.
     */
    MASCARAR_PARCIALMENTE,

    /**
     * Aplica um hash SHA-256 ao valor do campo.
     */
    HASH_SHA256,

    /**
     * Substitui o valor original do campo por uma string fixa.
     */
    SUBSTITUIR_POR_FIXO,

    TO_STRING_OFUSCADO

}