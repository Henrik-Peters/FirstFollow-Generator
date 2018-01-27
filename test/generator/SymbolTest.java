// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import org.junit.jupiter.api.Test;
import static generator.Singletons.*;
import static org.junit.jupiter.api.Assertions.*;

class SymbolTest {

    @Test
    void testSingleCharacter() {
        Terminal A = new Terminal("A");
        assertEquals("A", A.toString());

        Nonterminal B = new Nonterminal("B");
        assertEquals("B", B.toString());
    }

    @Test
    void testMultiCharacter() {
        Terminal AB = new Terminal("AB");
        assertEquals("AB", AB.toString());

        Nonterminal CD = new Nonterminal("CD");
        assertEquals("CD", CD.toString());
    }

    @Test
    void testEmptySymbol() {
        assertEquals("ε", ε.toString());
    }

    @Test
    void testEquals() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal A2 = new Terminal("A");
        Nonterminal NA = new Nonterminal("A");
        Nonterminal NB = new Nonterminal("B");
        Nonterminal NA2 = new Nonterminal("A");

        assertTrue(A.equals(A));
        assertTrue(A.equals(A2));
        assertFalse(A.equals(B));
        assertFalse(A.equals(NA));
        assertFalse(A.equals(NB));
        assertFalse(A.equals(NA2));

        assertTrue(NA.equals(NA));
        assertTrue(NA.equals(NA2));
        assertFalse(NA.equals(A));
        assertFalse(NA.equals(A2));
        assertFalse(NA.equals(B));
        assertFalse(NA.equals(NB));
    }
}
