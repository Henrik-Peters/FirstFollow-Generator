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
    void testCombinedMultilines() {
        String[] lines = new String[]{
                "S -> A B",
                "A -> A | a",
                "B -> B c",
                "A -> S B | b",
                "B -> c"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B}, {a, c, b}, {S -> A B, A -> A, A -> a, B -> B c, A -> S B, A -> b, B -> c}, S)", g.toString());
    }

    @Test
    void testRedudantMultilines() {
        String[] lines = new String[]{
                "S -> A",
                "A -> a",
                "A -> a",
                "A -> ab"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A}, {a, ab}, {S -> A, A -> a, A -> ab}, S)", g.toString());
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

    @Test
    void testNonterminalReplace() {
        String[] lines = new String[]{
                "S -> A* | AAA",
                "A* -> a | AAA"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A*}, {AAA, a}, {S -> A*, S -> AAA, A* -> a, A* -> AAA}, S)", g.toString());
    }

    @Test
    void testNonterminalInlineReplace() {
        String[] lines = new String[]{
                "S -> A | xABx",
                "A -> a | x AB x",
                "AB -> a"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, AB}, {xx, a, x}, {S -> A, S -> x AB x, A -> a, A -> x AB x, AB -> a}, S)", g.toString());
    }

    @Test
    void testMissingWhitespaces() {
        String[] lines = new String[]{
                "S -> AB C",
                "A -> a",
                "B -> b",
                "C -> c"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B, C}, {a, b, c}, {S -> A B C, A -> a, B -> b, C -> c}, S)", g.toString());
    }

    @Test
    void testMissingTerminalWhitespaces() {
        String[] lines = new String[]{
                "S -> AB | ABc",
                "A -> a",
                "AB -> b"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, AB}, {c, a, b}, {S -> AB, S -> AB c, A -> a, AB -> b}, S)", g.toString());
    }

    @Test
    void testLeftSideWhitespaces() {
        String[] lines = new String[]{
                "S  -> A AB",
                "  A -> a",
                "  AB     -> b"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, AB}, {a, b}, {S -> A AB, A -> a, AB -> b}, S)", g.toString());
    }

    @Test
    void testRightSideWhitespaces() {
        String[] lines = new String[]{
                "S ->  A     AB ",
                "A ->     a",
                "AB-> b   "
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, AB}, {a, b}, {S -> A AB, A -> a, AB -> b}, S)", g.toString());
    }

    @Test
    void testBothSidedWhitespaces() {
        String[] lines = new String[]{
                "S  ->  A     AB ",
                "  A ->     a",
                "  AB->     b   "
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, AB}, {a, b}, {S -> A AB, A -> a, AB -> b}, S)", g.toString());
    }

    @Test
    void testEmptyLines() {
        String[] lines = new String[]{
                "S -> A",
                "",
                "A -> a",
                ""
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A}, {a}, {S -> A, A -> a}, S)", g.toString());
    }

    @Test
    void testSingleUnusedProduction() {
        String[] lines = new String[]{
                "S -> A",
                "A -> a | b",
                "B -> c"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B}, {a, b, c}, {S -> A, A -> a, A -> b, B -> c}, S)", g.toString());
    }

    @Test
    void testUnusedProductions() {
        String[] lines = new String[]{
                "S -> A",
                "D -> f",
                "A -> a | b | C",
                "B -> c",
                "C -> S"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, D, A, B, C}, {f, a, b, c}, {S -> A, D -> f, A -> a, A -> b, A -> C, B -> c, C -> S}, S)", g.toString());
    }
}
