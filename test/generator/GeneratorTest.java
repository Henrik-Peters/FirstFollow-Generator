// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import org.junit.jupiter.api.Test;
import static generator.SetGenerator.*;
import static generator.GrammarParser.*;
import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {

    @Test
    void testStartupGrammar() {
        String[] lines = new String[]{
                "S -> ABC",
                "A -> C | a",
                "B -> b | ε",
                "C -> c"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B, C}, {a, b, ε, c}, {S -> A B C, A -> C, A -> a, B -> b, B -> ε, C -> c}, S)", g.toString());
        assertEquals("{A={a, c}, B={b, ε}, S={a, c}, C={c}}", First(g).toString());
        assertEquals("{A={b, c}, B={c}, S={$}, C={$, b, c}}", Follow(g).toString());
        assertEquals("{A -> a={a}, B -> b={b}, C -> c={c}, A -> C={c}, S -> A B C={a, c}, B -> ε={c}}", Predict(g).toString());
    }

    @Test
    void testMiddleNotEmptyGrammar() {
        String[] lines = new String[]{
                "S -> ABCDE",
                "A -> a | ε",
                "B -> b | ε",
                "C -> c",
                "D -> d | ε",
                "E -> e | ε"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B, C, D, E}, {a, ε, b, c, d, e}, {S -> A B C D E, A -> a, A -> ε, B -> b, B -> ε, C -> c, D -> d, D -> ε, E -> e, E -> ε}, S)", g.toString());
        assertEquals("{A={a, ε}, B={b, ε}, S={a, b, c}, C={c}, D={d, ε}, E={e, ε}}", First(g).toString());
        assertEquals("{A={b, c}, B={c}, S={$}, C={d, e, $}, D={e, $}, E={$}}", Follow(g).toString());
        assertEquals("{A -> a={a}, B -> b={b}, C -> c={c}, D -> d={d}, E -> e={e}, S -> A B C D E={a, b, c}, E -> ε={$}, D -> ε={e, $}, B -> ε={c}, A -> ε={b, c}}", Predict(g).toString());
    }

    @Test
    void testPartialEmptyWord() {
        String[] lines = new String[]{
                "S -> A C",
                "A -> B a",
                "B -> b | ε",
                "C -> c"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B, C}, {a, b, ε, c}, {S -> A C, A -> B a, B -> b, B -> ε, C -> c}, S)", g.toString());
        assertEquals("{A={b, a}, B={b, ε}, S={b, a}, C={c}}", First(g).toString());
        assertEquals("{A={c}, B={a}, S={$}, C={$}}", Follow(g).toString());
        assertEquals("{A -> B a={b, a}, B -> b={b}, C -> c={c}, S -> A C={b, a}, B -> ε={a}}", Predict(g).toString());
    }

    @Test
    void testTransitiveEmptyWord() {
        String[] lines = new String[]{
                "S -> A",
                "A -> B",
                "B -> b",
                "B -> ε"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B}, {b, ε}, {S -> A, A -> B, B -> b, B -> ε}, S)", g.toString());
        assertEquals("{A={b, ε}, B={b, ε}, S={b, ε}}", First(g).toString());
        assertEquals("{A={$}, B={$}, S={$}}", Follow(g).toString());
        assertEquals("{B -> b={b}, A -> B={b, $}, S -> A={b, $}, B -> ε={$}}", Predict(g).toString());
    }

    @Test
    void testTransitiveMultiEmptyWord() {
        String[] lines = new String[]{
                "S -> A C",
                "A -> B",
                "B -> b",
                "B -> ε",
                "C -> ε"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A, B, C}, {b, ε}, {S -> A C, A -> B, B -> b, B -> ε, C -> ε}, S)", g.toString());
        assertEquals("{A={b, ε}, B={b, ε}, S={b, ε}, C={ε}}", First(g).toString());
        assertEquals("{A={$}, B={$}, S={$}, C={$}}", Follow(g).toString());
        assertEquals("{B -> b={b}, S -> A C={b, $}, A -> B={b, $}, C -> ε={$}, B -> ε={$}}", Predict(g).toString());
    }

    @Test
    void testIdenticalSymbols() {
        String[] lines = new String[]{
                "S -> a A b A c",
                "A -> x"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, A}, {a, b, c, x}, {S -> a A b A c, A -> x}, S)", g.toString());
        assertEquals("{A={x}, S={a}}", First(g).toString());
        assertEquals("{A={b, c}, S={$}}", Follow(g).toString());
        assertEquals("{A -> x={x}, S -> a A b A c={a}}", Predict(g).toString());
    }

    @Test
    void testCombinedGrammarA() {
        String[] lines = new String[]{
                "F -> ( E ) | id",
                "E -> T | G",
                "G -> + T G | ε",
                "T -> F L",
                "L -> * F L | ε"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({F, E, G, T, L}, {(, ), id, +, ε, *}, {F -> ( E ), F -> id, E -> T, E -> G, G -> + T G, G -> ε, T -> F L, L -> * F L, L -> ε}, F)", g.toString());
        assertEquals("{T={(, id}, E={(, id, +, ε}, F={(, id}, G={+, ε}, L={*, ε}}", First(g).toString());
        assertEquals("{T={), +}, E={)}, F={$, *, ), +}, G={)}, L={), +}}", Follow(g).toString());
        assertEquals("{E -> G={+, )}, T -> F L={(, id}, L -> ε={), +}, F -> id={id}, L -> * F L={*}, G -> + T G={+}, F -> ( E )={(}, G -> ε={)}, E -> T={(, id}}", Predict(g).toString());
    }

    @Test
    void testCombinedGrammarB() {
        String[] lines = new String[]{
                "S -> E",
                "E -> E + T | E - T | T",
                "T -> T * F | T / F | F",
                "F -> ( E ) | id | num"
        };
        Grammar g = ParseGrammar(lines);
        assertEquals("({S, E, T, F}, {+, -, *, /, (, ), id, num}, {S -> E, E -> E + T, E -> E - T, E -> T, T -> T * F, T -> T / F, T -> F, F -> ( E ), F -> id, F -> num}, S)", g.toString());
        assertEquals("{S={(, id, num}, T={(, id, num}, E={(, id, num}, F={(, id, num}}", First(g).toString());
        assertEquals("{S={$}, T={$, +, -, *, /, )}, E={$, +, -, )}, F={$, +, -, *, /, )}}", Follow(g).toString());
        assertEquals("{F -> num={num}, S -> E={(, id, num}, T -> F={(, id, num}, F -> id={id}, E -> E - T={(, id, num}, T -> T / F={(, id, num}, F -> ( E )={(}, E -> E + T={(, id, num}, T -> T * F={(, id, num}, E -> T={(, id, num}}", Predict(g).toString());
    }
}
