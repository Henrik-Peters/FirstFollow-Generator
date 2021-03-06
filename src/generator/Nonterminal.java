// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

/**
 * Represents the nonterminal symbols of context-free grammars.
 * A string is used to store the content of the symbol.
 */
public class Nonterminal extends Symbol {

    /**
     * Create a new nonterminal symbol
     * @param text Content of the nonterminal
     */
    public Nonterminal(String text) {
        super(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nonterminal)) return false;
        Nonterminal other = (Nonterminal)o;
        return text.equals(other.text);
    }
}
