// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

/**
 * Represents the terminal symbols of context-free grammars.
 * A string is used to store the content of the symbol.
 */
public class Terminal extends Symbol {

    /**
     * Create a new terminal symbol
     * @param text Content of the terminal
     */
    public Terminal(String text) {
        super(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Terminal)) return false;
        Terminal other = (Terminal)o;
        return text.equals(other.text);
    }
}
