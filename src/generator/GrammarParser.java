// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.*;
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
        //Remove invalid lines
        lines = Arrays.stream(lines)
                .filter(line -> line.contains("->"))
                .toArray(String[]::new);

        //Parse nonterminals
        SymbolSet nonterminals = Arrays.stream(lines)
                .map(line -> line.split("->")[0].trim())
                .map(Nonterminal::new)
                .collect(toSymbolSet());

        //Sort the nonterminals by their length for the longest match
        Nonterminal[] sortedNonterminals = nonterminals.stream()
                .sorted((a, b) -> b.text.length() - a.text.length())
                .toArray(Nonterminal[]::new);

        //Parse terminals
        SymbolSet terminals = Arrays.stream(lines)
                .map(line -> {
                    String rightSide = line.split("->")[1].trim();

                    for (Symbol n : sortedNonterminals) {
                        rightSide = rightSide.replace(n.text, "");
                    }

                    rightSide = rightSide.replaceAll("\\|", "").trim();
                    rightSide = rightSide.replaceAll("( )+", " ").trim();
                    return rightSide;

                }).filter(str -> !str.isEmpty())
                .flatMap(str -> Arrays.stream(str.split(" ")))
                .map(str -> (str.equals("ε")) ? ε : new Terminal(str))
                .collect(toSymbolSet());

        //Sort the terminals by their length for the longest match
        Terminal[] sortedTerminals = terminals.stream()
                .sorted((a, b) -> b.text.length() - a.text.length())
                .toArray(Terminal[]::new);


        //Parse productions
        ProductionSet productions = new ProductionSet();

        for (String line : lines) {
            String[] lineSplit = line.split("->");
            String[] subRuleSplit = lineSplit[1].split("\\|");

            Nonterminal leftSide = nonterminals.stream()
                    .filter(n -> n.text.equals(lineSplit[0].trim()))
                    .findFirst()
                    .map(Nonterminal.class::cast)
                    .orElse(null);

            for (String subRule : subRuleSplit) {
                List<Symbol> rightSide = new LinkedList<>();
                String restRule = subRule.trim();

                while (!restRule.isEmpty()) {
                    boolean foundNonterminal = false;

                    //Try to match with the longest nonterminal first
                    for (Nonterminal n : sortedNonterminals) {

                        if (restRule.length() >= n.text.length() &&
                            n.text.equals(restRule.substring(0, n.text.length()))) {

                            rightSide.add(n);
                            restRule = restRule.substring(n.text.length()).trim();
                            foundNonterminal = true;
                            break;
                        }
                    }

                    //Try to match with the longest terminal
                    if (!foundNonterminal) {
                        for (Terminal t : sortedTerminals) {

                            if (restRule.charAt(0) == 'ε') {
                                rightSide.add(ε);
                                restRule = restRule.substring(1);
                                break;
                            }

                            if (t != ε &&
                                restRule.length() >= t.text.length() &&
                                t.text.equals(restRule.substring(0, t.text.length()))) {
                                rightSide.add(t);
                                restRule = restRule.substring(t.text.length()).trim();
                                break;
                            }
                        }
                    }
                }

                if (!rightSide.isEmpty()) {
                    Word rightWord = new Word(rightSide.toArray(new Symbol[0]));
                    productions.add(new Production(leftSide, rightWord));
                }
            }
        }

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
