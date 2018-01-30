// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
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

    /**
     * Check if this word contains no symbols
     * @return True when the word has no symbols
     */
    public boolean isEmpty() {
        return symbols.length == 0;
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

    /**
     * Get the first symbol of the word
     * @return The first symbol
     */
    public Symbol first() {
        if (isEmpty()) {
            throw new IllegalStateException("First called on an empty word");
        } else {
            return symbols[0];
        }
    }

    /**
     * Get the last symbol of the word
     * @return The last symbol
     */
    public Symbol last() {
        if (isEmpty()) {
            throw new IllegalStateException("Last called on an empty word");
        } else {
            return symbols[symbols.length - 1];
        }
    }

    /**
     * Create a new word with all symbols on the left side of sy
     * @param sy Take all symbols until this symbol is found
     * @return Word with all left symbols of sy (excluding sy)
     */
    public Word leftOf(Symbol sy) {
        int index = indexOf(sy);

        if (index == -1) {
            throw new NoSuchElementException("Symbol " + sy.toString() + " not found");

        } else {
            return new Word(Arrays.copyOfRange(symbols, 0, index));
        }
    }

    /**
     * Create a new word with all symbols on the right side of sy
     * @param sy Skip all symbols until this symbol is found
     * @return Word with all right symbols of sy (excluding sy)
     */
    public Word rightOf(Symbol sy) {
        int index = indexOf(sy);

        if (index == -1) {
            throw new NoSuchElementException("Symbol " + sy.toString() + " not found");

        } else {
            return new Word(Arrays.copyOfRange(symbols, indexOf(sy) + 1, symbols.length));
        }
    }

    /**
     * Create a new word that starts with the specified beginIndex
     * @param beginIndex Skip this number of symbols
     * @return Word that starts with the symbol of beginIndex
     */
    public Word subWord(int beginIndex) {
        return new Word(Arrays.copyOfRange(symbols, beginIndex, symbols.length));
    }

    private int indexOf(Symbol sy) {
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] == sy) return i;
        }

        return -1;
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
