// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.HashMap;
import java.util.Map;

import static generator.Singletons.*;
import static generator.SymbolSet.*;

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

        boolean setChanged;
        SymbolSet set;

        do {
            setChanged = false;

            for (Production rule : grammar.Productions) {
                set = firstSets.get(rule.LeftSide);

                for (Symbol sy : rule.RightSide) {

                    if (sy instanceof Terminal) {
                        set = Union(set, new SymbolSet(sy));
                        if (sy != ε) break;

                    } else {
                        Nonterminal n = (Nonterminal)sy;
                        set = Union(set, firstSets.get(n));

                        if (!firstSets.get(n).contains(ε)) {
                            break;
                        }
                    }
                }

                //Check if the set is growing
                if (set.size() != firstSets.get(rule.LeftSide).size()) {
                    firstSets.put(rule.LeftSide, set);
                    setChanged = true;
                }
            }

        } while (setChanged);
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
