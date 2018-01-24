// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

/**
 * Contains all global constants of the generator.
 */
public final class Singletons {

    /** The empty word is represented by an empty string */
    public static final Terminal ε = new Terminal("");

    /** Set that only contains the empty word */
    public static final SymbolSet εSet = new SymbolSet(ε);

    /** The end-of-file marker for the follow sets */
    public static final Terminal Endmarker = new Terminal("$");

    /** The symbol set with no elements */
    public static final SymbolSet EmptySet = new SymbolSet();
}
