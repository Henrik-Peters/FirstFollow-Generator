// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all methods used for the generation
 * of the first, follow and predict sets.
 */
public class SetGenerator {

    /**
     * Generate the first sets for a context-free grammar
     * @param grammar The grammar for the generation
     * @return Map with all nonterminals as keys and first sets as values
     */
    public static Map<Symbol, SymbolSet> First(Grammar grammar) {
        Map<Symbol, SymbolSet> firstSets = new HashMap<>();

        //Initialize with empty sets
        for (Symbol n : grammar.Nonterminals) {
            firstSets.put(n, new SymbolSet());
        }

        return firstSets;
    }

    /**
     * Generate the follow sets for a context-free grammar
     * @param grammar The grammar for the generation
     * @return Map with all nonterminals as keys and follow sets as values
     */
    public static Map<Symbol, SymbolSet> Follow(Grammar grammar) {
        Map<Symbol, SymbolSet> followSets = new HashMap<>();

        //Initialize with empty sets
        for (Symbol n : grammar.Nonterminals) {
            followSets.put(n, new SymbolSet());
        }

        return followSets;
    }

    /**
     * Generate the predict sets for a context-free grammar
     * @param grammar The grammar for the generation
     * @return Map with all productions as keys and predict sets as values
     */
    public static Map<Production, SymbolSet> Predict(Grammar grammar) {
        Map<Production, SymbolSet> predictSets = new HashMap<>();

        //Initialize with empty sets
        for (Production p : grammar.Productions) {
            predictSets.put(p, new SymbolSet());
        }

        return predictSets;
    }
}
