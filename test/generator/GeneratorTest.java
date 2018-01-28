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
        assertEquals("{A={b}, B={c}, S={$}, C={$, b}}", Follow(g).toString());
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
        assertEquals("{A={a, ε}, B={b, ε}, S={a, ε, b, c}, C={c}, D={d, ε}, E={e, ε}}", First(g).toString());
        assertEquals("{A={b, c}, B={c}, S={$}, C={d, e, $}, D={e, $}, E={$}}", Follow(g).toString());
        assertEquals("", Predict(g).toString());
    }
}
