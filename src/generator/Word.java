// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Represents words of context-free grammars. Words are lists
 * of symbols. A symbol can be a terminal or nonterminal.
 */
public class Word implements Comparable<Word>, Iterable<Symbol> {

    /** Symbols of the word */
    private final Symbol[] symbols;

    /**
     * Create a new word with a list of symbols.
     * @param symbols The symbols for the new word
     */
    public Word(Symbol... symbols) {
        this.symbols = symbols;
    }

    @Override
    public int compareTo(Word o) {
        return this.toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word other = (Word)o;
        return Arrays.equals(symbols, other.symbols);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(symbols);
    }

    @Override
    public Iterator<Symbol> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < symbols.length;
            }

            @Override
            public Symbol next() {
                return symbols[index++];
            }
        };
    }

    @Override
    public void forEach(Consumer<? super Symbol> action) {
        for (Symbol symbol : symbols) {
            action.accept(symbol);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (symbols == null || symbols.length == 0) {
            return "";

        } else {
            for (Symbol sy : symbols) {
                sb.append(sy);
                sb.append(" ");
            }

            sb.delete(sb.length() - 1, sb.length());
        }

        return sb.toString();
    }
}
