// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

import static generator.Singletons.*;
import static org.junit.jupiter.api.Assertions.*;

class WordTest {

    @Test
    void testIsEmpty() {
        Word w0 = new Word();
        Word w1 = new Word(new Terminal("A"));
        assertTrue(w0.isEmpty());
        assertFalse(w1.isEmpty());
    }

    @Test
    void testEquals() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal C = new Terminal("C");
        Nonterminal NA = new Nonterminal("A");

        //Equal words with different lengths
        Word w0 = new Word(A);
        Word w1 = new Word(A, B);
        Word w2 = new Word(A, B, C);

        Word w3 = new Word(A);
        Word w4 = new Word(A, B);
        Word w5 = new Word(A, B, C);

        assertEquals(w0, w3);
        assertEquals(w1, w4);
        assertEquals(w2, w5);

        //Different types but with the same text
        Word w7 = new Word(NA);
        assertFalse(w0.equals(w7));
        assertFalse(w7.equals(w0));

        //Different words
        assertFalse(w0.equals(w1));
        assertFalse(w1.equals(w0));
        assertFalse(w0.equals(w2));
        assertFalse(w2.equals(w0));
        assertFalse(w1.equals(w2));
        assertFalse(w2.equals(w1));
    }

    @Test
    void testIterator() {
        Terminal[] terminals = new Terminal[]{
                new Terminal("A"),
                new Terminal("B"),
                new Terminal("C")
        };

        int index = 0;
        Word w = new Word(terminals[0], terminals[1], terminals[2]);

        for (Symbol sy : w) {
            assertEquals(terminals[index++], sy);
        }
    }

    @Test
    void testForEach() {
        Terminal[] terminals = new Terminal[]{
                new Terminal("A"),
                new Terminal("B"),
                new Terminal("C")
        };

        StringBuilder sb = new StringBuilder();
        Word w = new Word(terminals[0], terminals[1], terminals[2]);

        w.forEach(sy -> sb.append(sy.toString()));
        assertEquals("ABC", sb.toString());
    }

    @Test
    void testFirst() {
        Terminal[] terminals = new Terminal[]{
                new Terminal("A"),
                new Terminal("B"),
                new Terminal("C")
        };
        Word w = new Word(terminals[0], terminals[1], terminals[2]);
        assertEquals(terminals[0], w.first());
    }

    @Test
    void testEmptyFirst() {
        Word w = new Word();
        assertThrows(IllegalStateException.class, w::first);
    }

    @Test
    void testLast() {
        Terminal[] terminals = new Terminal[]{
                new Terminal("A"),
                new Terminal("B"),
                new Terminal("C")
        };
        Word w = new Word(terminals[0], terminals[1], terminals[2]);
        assertEquals(terminals[2], w.last());
    }

    @Test
    void testEmptyLast() {
        Word w = new Word();
        assertThrows(IllegalStateException.class, w::last);
    }

    @Test
    void testTextFormat() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("BC");
        Terminal C = new Terminal("D");

        assertEquals("ε", new Word(ε).toString());
        assertEquals("A", new Word(A).toString());
        assertEquals("A BC", new Word(A, B).toString());
        assertEquals("A BC D", new Word(A, B, C).toString());
    }

    @Test
    void testLeftOf() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal C = new Terminal("C");
        Terminal D = new Terminal("D");
        Terminal X = new Terminal("X");
        Word w = new Word(A, B, C, D);

        assertEquals("", w.leftOf(A).toString());
        assertEquals("A", w.leftOf(B).toString());
        assertEquals("A B", w.leftOf(C).toString());
        assertEquals("A B C", w.leftOf(D).toString());
        assertThrows(NoSuchElementException.class, () -> w.leftOf(X));
    }

    @Test
    void testRightOf() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal C = new Terminal("C");
        Terminal D = new Terminal("D");
        Terminal X = new Terminal("X");
        Word w = new Word(A, B, C, D);

        assertEquals("B C D", w.rightOf(A).toString());
        assertEquals("C D", w.rightOf(B).toString());
        assertEquals("D", w.rightOf(C).toString());
        assertEquals("", w.rightOf(D).toString());
        assertThrows(NoSuchElementException.class, () -> w.rightOf(X));
    }
}
