// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductionTest {

    @Test
    void testConstructor() {
        Nonterminal A = new Nonterminal("A");
        Nonterminal B = new Nonterminal("B");
        Nonterminal C = new Nonterminal("C");
        Word word = new Word(B, C);

        Production p = new Production(A, word);
        assertEquals(A, p.LeftSide);
        assertEquals(word, p.RightSide);
    }

    @Test
    void testEquals() {
        Nonterminal A = new Nonterminal("A");
        Nonterminal B = new Nonterminal("B");
        Nonterminal C = new Nonterminal("C");
        Terminal TC = new Terminal("C");
        Word w0 = new Word();
        Word w1 = new Word(A);
        Word w2 = new Word(A, B);
        Word w3 = new Word(A, B, C);
        Word w4 = new Word(A, B, TC);

        //Equal productions with different lengths on the right side
        Production p0 = new Production(A, w0);
        Production p1 = new Production(A, w1);
        Production p2 = new Production(A, w2);
        Production p3 = new Production(A, w3);
        Production p4 = new Production(A, w4);

        Production p5 = new Production(A, w0);
        Production p6 = new Production(A, w1);
        Production p7 = new Production(A, w2);
        Production p8 = new Production(A, w3);
        Production p9 = new Production(A, w4);

        assertEquals(p0, p5);
        assertEquals(p1, p6);
        assertEquals(p2, p7);
        assertEquals(p3, p8);
        assertEquals(p4, p9);

        //Different types in the last symbol
        Production p10 = new Production(C, w3);
        Production p11 = new Production(C, w4);
        assertFalse(p10.equals(p11));
        assertFalse(p11.equals(p10));

        //Different left side
        Production p12 = new Production(A, w1);
        Production p13 = new Production(B, w1);
        assertFalse(p12.equals(p13));
        assertFalse(p13.equals(p12));

        //Different right side
        Production p14 = new Production(C, w2);
        Production p15 = new Production(C, w3);
        assertFalse(p14.equals(p15));
        assertFalse(p15.equals(p14));
    }

    @Test
    void testTextFormat() {
        Nonterminal A = new Nonterminal("A");
        Nonterminal B = new Nonterminal("B");
        Nonterminal C = new Nonterminal("C");
        Terminal TC = new Terminal("c");
        Word w0 = new Word(A);
        Word w1 = new Word(A, B);
        Word w2 = new Word(A, B, C);
        Word w3 = new Word(A, B, TC);

        //Productions with different right sides
        Production p0 = new Production(A, w0);
        Production p1 = new Production(A, w1);
        Production p2 = new Production(B, w2);
        Production p3 = new Production(A, w3);

        assertEquals("A -> A", p0.toString());
        assertEquals("A -> A B", p1.toString());
        assertEquals("B -> A B C", p2.toString());
        assertEquals("A -> A B c", p3.toString());
    }
}
