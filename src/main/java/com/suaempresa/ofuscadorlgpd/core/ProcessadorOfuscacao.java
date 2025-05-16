package com.suaempresa.ofuscadorlgpd.core;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.anotacoes.Ofuscavel;
import com.suaempresa.ofuscadorlgpd.strategy.EstrategiaOfuscacao;
import com.suaempresa.ofuscadorlgpd.strategy.TipoEstrategia;
import com.suaempresa.ofuscadorlgpd.strategy.impl.HashSha256Strategy;
import com.suaempresa.ofuscadorlgpd.strategy.impl.MascararParcialmenteStrategy;
import com.suaempresa.ofuscadorlgpd.strategy.impl.SubstituirPorFixoStrategy;
import com.suaempresa.ofuscadorlgpd.strategy.impl.ToStringOfuscadoStrategy;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Processador de ofuscação, agora com suporte a TO_STRING_OFUSCADO.
 */
public class ProcessadorOfuscacao {

    private final Map<TipoEstrategia, EstrategiaOfuscacao> mapaEstrategias;

    public ProcessadorOfuscacao() {
        mapaEstrategias = new HashMap<>();
        mapaEstrategias.put(TipoEstrategia.MASCARAR_PARCIALMENTE, new MascararParcialmenteStrategy());
        mapaEstrategias.put(TipoEstrategia.HASH_SHA256, new HashSha256Strategy());
        mapaEstrategias.put(TipoEstrategia.SUBSTITUIR_POR_FIXO, new SubstituirPorFixoStrategy());
        // Registra a nova estratégia de toString ofuscado
        mapaEstrategias.put(TipoEstrategia.TO_STRING_OFUSCADO, new ToStringOfuscadoStrategy(this));
    }

    /**
     * Ofusca campos reais de um objeto anotado com @Ofuscavel e @DadosSensiveis.
     */
    public <T> T ofuscarDados(T objeto) throws IllegalAccessException {
        Objects.requireNonNull(objeto, "O objeto não pode ser nulo.");
        Class<?> cls = objeto.getClass();
        if (!cls.isAnnotationPresent(Ofuscavel.class)) {
            return objeto;
        }
        for (Field f : cls.getDeclaredFields()) {
            if (f.isAnnotationPresent(DadosSensiveis.class)) {
                DadosSensiveis an = f.getAnnotation(DadosSensiveis.class);
                EstrategiaOfuscacao estr = mapaEstrategias.get(an.estrategia());
                if (estr == null) continue;
                boolean acess = f.canAccess(objeto);
                if (!acess) f.setAccessible(true);
                Object val = f.get(objeto);
                if (val != null) {
                    f.set(objeto, estr.ofuscar(val, an));
                }
                if (!acess) f.setAccessible(false);
            }
        }
        return objeto;
    }

    /**
     * Gera uma String segura (ofuscada) para log via TO_STRING_OFUSCADO.
     */
    public <T> String gerarLogOfuscado(T objeto) {
        EstrategiaOfuscacao logStrat = mapaEstrategias.get(TipoEstrategia.TO_STRING_OFUSCADO);
        return (String) logStrat.ofuscar(objeto, null);
    }

    /**
     * Permite à ToStringOfuscadoStrategy reusar o mapa interno
     */
    public EstrategiaOfuscacao getEstrategia(TipoEstrategia tipo) {
        return mapaEstrategias.get(tipo);
    }
}
