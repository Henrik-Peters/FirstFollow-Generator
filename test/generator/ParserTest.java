// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import org.junit.jupiter.api.Test;
import static generator.GrammarParser.ParseGrammar;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void testSingleProduction() {
        String[] lines = new String[]{ "S -> a" };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S}, {a}, {S -> a}, S)", g.toString());
    }

    @Test
    void testMultiProductions() {
        String[] lines = new String[]{
                "S -> a",
                "B -> b"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, B}, {a, b}, {S -> a, B -> b}, S)", g.toString());
    }

    @Test
    void testEmptyProduction() {
        String[] lines = new String[]{ "S -> ε" };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S}, {ε}, {S -> ε}, S)", g.toString());
    }

    @Test
    void testCombinedProduction() {
        String[] lines = new String[]{ "S -> a | b" };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S}, {a, b}, {S -> a, S -> b}, S)", g.toString());
    }

    @Test
    void testNonterminalProductions() {
        String[] lines = new String[]{
                "S -> A B C",
                "A -> a",
                "B -> b",
                "C -> c"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B, C}, {a, b, c}, {S -> A B C, A -> a, B -> b, C -> c}, S)", g.toString());
    }

    @Test
    void testCombinedNonterminals() {
        String[] lines = new String[]{
                "S -> A B | C",
                "A -> A | a",
                "B -> B C",
                "C -> c"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B, C}, {a, c}, {S -> A B, S -> C, A -> A, A -> a, B -> B C, C -> c}, S)", g.toString());
    }

    @Test
    void testMultiCharacterTerminals() {
        String[] lines = new String[]{ "S -> abc" };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S}, {abc}, {S -> abc}, S)", g.toString());
    }

    @Test
    void testLongestTerminalMatch() {
        String[] lines = new String[]{
                "S -> A B | abc",
                "A -> a | B",
                "B -> ab"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B}, {abc, a, ab}, {S -> A B, S -> abc, A -> a, A -> B, B -> ab}, S)", g.toString());
    }

    @Test
    void testMultiCharacterNonterminals() {
        String[] lines = new String[]{
                "S -> AB",
                "AB -> a"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, AB}, {a}, {S -> AB, AB -> a}, S)", g.toString());
    }

    @Test
    void testLongestNonterminalMatch() {
        String[] lines = new String[]{
                "S -> A | AB | ABC",
                "A -> AB",
                "ABC -> a",
                "AB -> ABC"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, ABC, AB}, {a}, {S -> A, S -> AB, S -> ABC, A -> AB, ABC -> a, AB -> ABC}, S)", g.toString());
    }
}
