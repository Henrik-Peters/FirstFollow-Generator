// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.Arrays;
import static generator.SymbolSet.*;
import static generator.Singletons.*;

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
        SymbolSet nonterminals = Arrays.stream(lines)
                .map(line -> line.split("->")[0].trim())
                .map(Nonterminal::new)
                .collect(toSymbolSet());

        //Parse terminals
        SymbolSet terminals = Arrays.stream(lines)
                .map(line -> {
                    String rightSide = line.split("->")[1].trim();

                    for (Symbol n : nonterminals) {
                        rightSide = rightSide.replaceAll(n.text, "");
                    }

                    rightSide = rightSide.replaceAll("\\|", "").trim();
                    rightSide = rightSide.replaceAll("( )+", " ").trim();
                    return rightSide;

                }).filter(str -> !str.isEmpty())
                .flatMap(str -> Arrays.stream(str.split(" ")))
                .map(str -> (str.equals("ε")) ? ε : new Terminal(str))
                .collect(toSymbolSet());

        //Parse productions
        ProductionSet productions = new ProductionSet();

        //Find the start symbol
        String startText = lines[0].split("->")[0].trim();
        Nonterminal start = nonterminals.stream()
                .filter(n -> n.text.equals(startText))
                .findFirst()
                .map(Nonterminal.class::cast)
                .orElse(null);

        return new Grammar(nonterminals, terminals, productions, start);
    }
}
