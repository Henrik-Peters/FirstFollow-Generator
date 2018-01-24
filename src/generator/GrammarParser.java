// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.Arrays;

/**
 * Contains all methods used to parse
 * context-free grammars by text input.
 */
public class GrammarParser {

    /**
     * Parse a context-free grammar by a text array
     * @param lines Every line should contain one production
     * @return The parsed context-free grammar
     */
    public static Grammar ParseGrammar(String[] lines) {

        //Parse nonterminals
        Nonterminal[] nonterminalsArray = Arrays.stream(lines)
                .map(line -> line.split("->")[0].trim())
                .map(Nonterminal::new).toArray(Nonterminal[]::new);

        SymbolSet nonterminals = new SymbolSet(nonterminalsArray);


        //Parse terminals
        SymbolSet terminals = new SymbolSet();

        //Parse productions
        ProductionSet productions = new ProductionSet();


        //Find the start symbol
        Nonterminal start = null;
        String startText = lines[0].split("->")[0].trim();

        for (Symbol sy : nonterminals) {
            if (sy.text.equals(startText)) {
                start = (Nonterminal)sy;
            }
        }

        return new Grammar(nonterminals, terminals, productions, start);
    }
}
