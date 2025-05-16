package com.suaempresa.ofuscadorlgpd.anotacoes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para marcar classes cujos campos podem ser ofuscados.
 * Se uma classe não estiver anotada com @Ofuscavel, seus campos
 * não serão processados pela biblioteca de ofuscação, mesmo que
 * contenham a anotação @DadosSensiveis.
 */
@Retention(RetentionPolicy.RUNTIME) // Disponível em tempo de execução
@Target(ElementType.TYPE)        // Aplicável a classes, interfaces, enums
public @interface Ofuscavel {
}