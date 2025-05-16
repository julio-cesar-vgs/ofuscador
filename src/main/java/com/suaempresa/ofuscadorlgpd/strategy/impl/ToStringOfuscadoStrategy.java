package com.suaempresa.ofuscadorlgpd.strategy.impl;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.anotacoes.Ofuscavel;
import com.suaempresa.ofuscadorlgpd.core.ProcessadorOfuscacao;
import com.suaempresa.ofuscadorlgpd.strategy.EstrategiaOfuscacao;

import java.lang.reflect.Field;

/**
 * Estratégia que gera uma String de log ofuscado de um objeto,
 * aplicando as regras de @DadosSensiveis em cada campo.
 * Não altera o objeto original.
 */
public class ToStringOfuscadoStrategy implements EstrategiaOfuscacao {

    private final ProcessadorOfuscacao processador;

    public ToStringOfuscadoStrategy(ProcessadorOfuscacao processador) {
        this.processador = processador;
    }

    @Override
    public Object ofuscar(Object objeto, DadosSensiveis ignored) {
        if (objeto == null) {
            return "null";
        }
        Class<?> cls = objeto.getClass();
        // Se a classe não for @Ofuscavel, cai para o toString padrão
        if (!cls.isAnnotationPresent(Ofuscavel.class)) {
            return objeto.toString();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(cls.getSimpleName()).append(" {");
        for (Field f : cls.getDeclaredFields()) {
            boolean acessivel = f.canAccess(objeto);
            f.setAccessible(true);
            sb.append("\n  ").append(f.getName()).append("=");

            try {
                Object valor = f.get(objeto);
                if (f.isAnnotationPresent(DadosSensiveis.class)) {
                    DadosSensiveis an = f.getAnnotation(DadosSensiveis.class);
                    // Reaproveita o map de estratégias para ofuscar o campo
                    EstrategiaOfuscacao estr = processador.getEstrategia(an.estrategia());
                    valor = estr.ofuscar(valor, an);
                }
                sb.append(valor);
            } catch (IllegalAccessException e) {
                sb.append("[ERRO]");
            }

            if (!acessivel) {
                f.setAccessible(false);
            }
        }
        sb.append("\n}");
        return sb.toString();
    }
}
