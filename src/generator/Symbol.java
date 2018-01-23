// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

/**
 * Represent the symbols of context-free grammars by this class.
 * A string is used to store the content of the symbol.
 */
public abstract class Symbol implements Comparable<Symbol> {

    /** The content of the symbol */
    private final String text;

    /**
     * Create a new symbol and set the content of the symbol
     * @param text Content of the new symbol
     */
    public Symbol(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(Symbol o) {
        return text.compareTo(o.text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;
        Symbol other = (Symbol)o;
        return text.equals(other.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    @Override
    public String toString() {
        return text;
    }
}
