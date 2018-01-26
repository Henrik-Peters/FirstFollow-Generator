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
        return Follow(grammar, First(grammar));
    }

    /**
     * Generate the follow sets for a context-free grammar
     * with the precomputed first sets
     * @param grammar The grammar for the generation
     * @param firstSets The precomputed first sets
     * @return Map with all nonterminals as keys and follow sets as values
     */
    public static Map<Symbol, SymbolSet> Follow(Grammar grammar, Map<Symbol, SymbolSet> firstSets) {
        Map<Symbol, SymbolSet> followSets = new HashMap<>();

        //Initialize with empty sets
        for (Symbol n : grammar.Nonterminals) {
            followSets.put(n, new SymbolSet());
        }

        //Add the end-marker to the start symbol
        followSets.get(grammar.StartSymbol).add(Endmarker);

        boolean setChanged;
        SymbolSet set;

        do {
            setChanged = false;

            for (Production rule : grammar.Productions) {
                for (Symbol sy : rule.RightSide) {

                    if (sy instanceof Nonterminal) {
                        Nonterminal n = (Nonterminal)sy;
                        set = followSets.get(n);

                        //All symbols after the current nonterminal
                        Word restWord = rule.RightSide.rightOf(n);

                        if (restWord.isEmpty()) {
                            set = Union(set, followSets.get(rule.LeftSide));

                        } else {
                            for (Symbol item : restWord) {

                                if (item instanceof Terminal) {
                                    set = Union(set, new SymbolSet(item));
                                    if (item != ε) break;

                                } else {
                                    set = Union(set, Difference(firstSets.get(item), εSet));

                                    if (firstSets.get(item).contains(ε) && item == rule.RightSide.last()) {
                                        set = Union(set, followSets.get(rule.LeftSide));
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }

                        //Check if the set is growing
                        if (set.size() != followSets.get(n).size()) {
                            followSets.put(n, set);
                            setChanged = true;
                        }
                    }
                }
            }

        } while (setChanged);
        return followSets;
    }

    /**
     * Generate the predict sets for a context-free grammar
     * @param grammar The grammar for the generation
     * @return Map with all productions as keys and predict sets as values
     */
    public static Map<Production, SymbolSet> Predict(Grammar grammar) {
        return Predict(grammar, First(grammar), Follow(grammar));
    }

    /**
     * Generate the predict sets for a context-free grammar
     * with the precomputed first and follow sets
     * @param grammar The grammar for the generation
     * @param firstSets The precomputed first sets
     * @param followSets The precomputed follow sets
     * @return Map with all productions as keys and predict sets as values
     */
    public static Map<Production, SymbolSet> Predict(Grammar grammar, Map<Symbol, SymbolSet> firstSets, Map<Symbol, SymbolSet> followSets) {
        Map<Production, SymbolSet> predictSets = new HashMap<>();

        for (Production p : grammar.Productions) {
            predictSets.put(p, First(firstSets, p.RightSide));
            predictSets.get(p).remove(ε);

            if (Nullable(firstSets, p.RightSide)) {
                predictSets.put(p, Union(predictSets.get(p), followSets.get(p.LeftSide)));
            }
        }

        return predictSets;
    }

    /**
     * Generate the first set of a word
     * @param firstSets The precomputed first sets of the grammar
     * @param word The word for the generation
     * @return Set with all first symbols of the word
     */
    private static SymbolSet First(Map<Symbol, SymbolSet> firstSets, Word word) {
        SymbolSet firstSet = new SymbolSet();

        for (Symbol sy : word) {
            if (sy instanceof Terminal) {
                firstSet = Union(firstSet, new SymbolSet(sy));
                if (sy != ε) break;

            } else {
                firstSet = Union(firstSet, firstSets.get(sy));

                if (!firstSets.get(sy).contains(ε)) {
                    break;
                }
            }
        }

        return firstSet;
    }

    /**
     * Check if a word can be transformed into the empty word
     * @param firstSets The precomputed first sets of the grammar
     * @param word The word for the generation
     * @return True when the empty word can be derived
     */
    private static boolean Nullable(Map<Symbol, SymbolSet> firstSets, Word word) {
        boolean nullable = true;

        for (Symbol sy : word) {
            if (sy instanceof Terminal) {
                if (sy != ε) {
                    nullable = false;
                }

            } else {
                if (!firstSets.get(sy).contains(ε)) {
                    nullable = false;
                }
            }
        }

        return nullable;
    }
}
