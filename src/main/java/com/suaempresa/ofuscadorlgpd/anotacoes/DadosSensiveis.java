package com.suaempresa.ofuscadorlgpd.anotacoes;

import com.suaempresa.ofuscadorlgpd.strategy.TipoEstrategia;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para marcar campos que contêm dados sensíveis e precisam ser ofuscados.
 * Especifica a estratégia de ofuscação a ser aplicada e seus parâmetros.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DadosSensiveis {
    /**
     * Define a estratégia de ofuscação a ser utilizada para o campo anotado.
     *
     * @return O tipo de estratégia de ofuscação.
     */
    TipoEstrategia estrategia();

    /**
     * Valor fixo a ser usado quando a estratégia é SUBSTITUIR_POR_FIXO.
     * O valor padrão é "[DADO OFUSCADO]".
     *
     * @return A string que substituirá o valor original.
     */
    String valorFixo() default "[DADO OFUSCADO]";

    /**
     * Número de caracteres a serem mantidos visíveis no início da string.
     * Utilizado por estratégias de mascaramento parcial.
     * O valor padrão é 0.
     *
     * @return O número de caracteres visíveis no início.
     */
    int caracteresVisiveisInicio() default 0;

    /**
     * Número de caracteres a serem mantidos visíveis no fim da string.
     * Utilizado por estratégias de mascaramento parcial.
     * O valor padrão é 0.
     *
     * @return O número de caracteres visíveis no fim.
     */
    int caracteresVisiveisFim() default 0;

    /**
     * Caractere a ser utilizado para mascarar os dados.
     * Utilizado por estratégias de mascaramento.
     * O valor padrão é '*'.
     *
     * @return O caractere de mascaramento.
     */
    char caractereMascaramento() default '*';
}