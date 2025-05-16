package com.suaempresa.ofuscadorlgpd.core;

import com.suaempresa.ofuscadorlgpd.anotacoes.DadosSensiveis;
import com.suaempresa.ofuscadorlgpd.anotacoes.Ofuscavel; // Importa a nova anotação
import com.suaempresa.ofuscadorlgpd.strategy.EstrategiaOfuscacao;
import com.suaempresa.ofuscadorlgpd.strategy.TipoEstrategia;
import com.suaempresa.ofuscadorlgpd.strategy.impl.HashSha256Strategy;
import com.suaempresa.ofuscadorlgpd.strategy.impl.MascararParcialmenteStrategy;
import com.suaempresa.ofuscadorlgpd.strategy.impl.SubstituirPorFixoStrategy;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Processador responsável por encontrar campos anotados com {@link DadosSensiveis}
 * em um objeto e aplicar a estratégia de ofuscação correspondente,
 * desde que a classe do objeto esteja anotada com {@link Ofuscavel}.
 */
public class ProcessadorOfuscacao {

    private final Map<TipoEstrategia, EstrategiaOfuscacao> mapaEstrategias;

    /**
     * Construtor que inicializa e registra as estratégias de ofuscação disponíveis.
     */
    public ProcessadorOfuscacao() {
        mapaEstrategias = new HashMap<>();
        mapaEstrategias.put(TipoEstrategia.MASCARAR_PARCIALMENTE, new MascararParcialmenteStrategy());
        mapaEstrategias.put(TipoEstrategia.HASH_SHA256, new HashSha256Strategy());
        mapaEstrategias.put(TipoEstrategia.SUBSTITUIR_POR_FIXO, new SubstituirPorFixoStrategy());
    }

    /**
     * Ofusca os dados de um objeto que estão marcados com a anotação {@link DadosSensiveis},
     * somente se a classe do objeto estiver anotada com {@link Ofuscavel}.
     * O método modifica o objeto original.
     *
     * @param objeto O objeto cujos campos serão inspecionados e possivelmente ofuscados.
     * @param <T> O tipo do objeto.
     * @return O mesmo objeto passado como parâmetro, com os campos sensíveis ofuscados (se aplicável).
     * @throws IllegalAccessException Se houver problemas ao acessar os campos via reflexão.
     * @throws NullPointerException Se o objeto fornecido for nulo.
     */
    public <T> T ofuscarDados(T objeto) throws IllegalAccessException {
        Objects.requireNonNull(objeto, "O objeto a ser ofuscado não pode ser nulo.");

        Class<?> classeDoObjeto = objeto.getClass();

        // VERIFICAÇÃO DA ANOTAÇÃO @Ofuscavel NA CLASSE
        if (!classeDoObjeto.isAnnotationPresent(Ofuscavel.class)) {
            // System.out.println("INFO: Classe " + classeDoObjeto.getSimpleName() + " não está anotada com @Ofuscavel. Nenhum dado será ofuscado.");
            return objeto; // Retorna o objeto original sem modificações
        }
        // System.out.println("INFO: Processando classe " + classeDoObjeto.getSimpleName() + " anotada com @Ofuscavel.");


        for (Field campo : classeDoObjeto.getDeclaredFields()) {
            if (campo.isAnnotationPresent(DadosSensiveis.class)) {
                DadosSensiveis anotacao = campo.getAnnotation(DadosSensiveis.class);
                TipoEstrategia tipoEstrategia = anotacao.estrategia();
                EstrategiaOfuscacao estrategia = mapaEstrategias.get(tipoEstrategia);

                if (estrategia == null) {
                    System.err.println("ALERTA: Nenhuma estratégia de ofuscação registrada para o tipo: " + tipoEstrategia +
                            " no campo '" + campo.getName() + "' da classe '" + classeDoObjeto.getSimpleName() +
                            "'. O campo não será ofuscado.");
                    continue;
                }

                boolean eraAcessivel = campo.canAccess(objeto);
                if (!eraAcessivel) {
                    campo.setAccessible(true);
                }

                Object valorOriginal = campo.get(objeto);
                if (valorOriginal != null) {
                    Object valorOfuscado = estrategia.ofuscar(valorOriginal, anotacao);
                    campo.set(objeto, valorOfuscado);
                }

                if (!eraAcessivel) {
                    campo.setAccessible(false);
                }
            }
        }
        return objeto;
    }
}