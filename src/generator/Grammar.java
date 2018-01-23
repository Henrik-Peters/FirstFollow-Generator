// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

/**
 * Represents context-free grammars.
 * Grammars should be immutable.
 */
public class Grammar {

    /** Allowed variables (nonterminals) of the grammar */
    public final SymbolSet Nonterminals;

    /** Allowed terminal symbols of the grammar */
    public final SymbolSet Terminals;

    /** Production rules that are allowed for derivation of words */
    public final ProductionSet Productions;

    /** Every word of this grammar must begin with this start symbol */
    public final Nonterminal StartSymbol;

    /**
     * Create a new context-free grammar
     * @param nonterminals Allowed variables (nonterminals)
     * @param terminals Allowed terminal symbols
     * @param productions Production rules for derivation of words
     * @param startSymbol First symbol for all allowed word
     */
    public Grammar(SymbolSet nonterminals, SymbolSet terminals, ProductionSet productions, Nonterminal startSymbol) {
        this.Nonterminals = nonterminals;
        this.Terminals = terminals;
        this.Productions = productions;
        this.StartSymbol = startSymbol;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(Nonterminals.toString());
        sb.append(", ");
        sb.append(Terminals.toString());
        sb.append(", ");
        sb.append(Productions.toString());
        sb.append(", ");
        sb.append(StartSymbol.toString());
        sb.append(")");
        return sb.toString();
    }
}
