// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import org.junit.jupiter.api.Test;
import java.util.stream.Stream;

import static generator.SymbolSet.*;
import static generator.Singletons.*;
import static org.junit.jupiter.api.Assertions.*;

class SymbolSetTest {

    @Test
    void testAdd() {
        Terminal A = new Terminal("A");
        SymbolSet s = new SymbolSet();

        assertFalse(s.contains(A));
        s.add(A);
        assertTrue(s.contains(A));
    }

    @Test
    void testConstructor() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");

        SymbolSet s = new SymbolSet(A);
        assertTrue(s.contains(A));

        s = new SymbolSet(A, B);
        assertTrue(s.contains(A));
        assertTrue(s.contains(B));
    }

    @Test
    void testCollector() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal C = new Terminal("C");
        Terminal D = new Terminal("D");

        //0 elements
        SymbolSet s0 = Stream.of(new Terminal[]{}).collect(toSymbolSet());
        assertEquals(0, s0.size());


        //1 element
        SymbolSet s1 = Stream.of(A)
                .filter(t -> t.text.equals("A"))
                .collect(toSymbolSet());

        assertTrue(s1.contains(A));
        assertEquals(1, s1.size());


        //2 elements
        SymbolSet s2 = Stream.of(A, B)
                .filter(t -> t.text.equals("A") || t.text.equals("B"))
                .collect(toSymbolSet());

        assertTrue(s2.contains(A));
        assertTrue(s2.contains(B));
        assertEquals(2, s2.size());


        //3 elements
        SymbolSet s3 = Stream.of(A, B, C).collect(toSymbolSet());
        assertTrue(s3.contains(A));
        assertTrue(s3.contains(B));
        assertTrue(s3.contains(B));
        assertEquals(3, s3.size());


        //4 elements filtered to 2 elements
        SymbolSet s4 = Stream.of(A, B, C, D)
                .filter(t -> t.text.equals("A") || t.text.equals("C"))
                .collect(toSymbolSet());

        assertTrue(s4.contains(A));
        assertFalse(s4.contains(B));
        assertTrue(s4.contains(C));
        assertFalse(s4.contains(D));
        assertEquals(2, s4.size());
    }

    @Test
    void testTextFormat() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal C = new Terminal("C");
        Terminal D = new Terminal("D");

        SymbolSet s0 = new SymbolSet();
        assertEquals("{}", s0.toString());

        SymbolSet s1 = new SymbolSet(A);
        assertEquals("{A}", s1.toString());

        SymbolSet s2 = new SymbolSet(A, B);
        assertEquals("{A, B}", s2.toString());

        SymbolSet s3 = new SymbolSet(A, B, C);
        assertEquals("{A, B, C}", s3.toString());

        SymbolSet s4 = new SymbolSet(A, B, C, D);
        assertEquals("{A, B, C, D}", s4.toString());
    }

    @Test
    void testUnion() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal C = new Terminal("C");
        Terminal D = new Terminal("D");
        Terminal E = new Terminal("E");

        //Union with the empty set
        SymbolSet s0 = new SymbolSet(A, B, C);
        SymbolSet r0 = Union(s0, EmptySet);
        SymbolSet r1 = Union(EmptySet, s0);
        SymbolSet r2 = Union(EmptySet, EmptySet);

        assertEquals(s0, r0);
        assertEquals(s0, r1);
        assertEquals(EmptySet, r2);

        //Union with two disjoint sets
        SymbolSet s1 = new SymbolSet(D, E);
        SymbolSet r3 = Union(s0, s1);
        assertEquals(new SymbolSet(A, B, C, D, E), r3);

        //Union with two non-disjoint sets
        SymbolSet s2 = new SymbolSet(B, C, D, E);
        SymbolSet r4 = Union(s0, s2);
        assertEquals(new SymbolSet(A, B, C, D, E), r4);
    }

    @Test
    void testDifference() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal C = new Terminal("C");
        Terminal D = new Terminal("D");
        Terminal E = new Terminal("E");

        //Difference with the empty set
        SymbolSet s0 = new SymbolSet(A, B, C);
        SymbolSet r0 = Difference(s0, EmptySet);
        SymbolSet r1 = Difference(EmptySet, s0);
        SymbolSet r2 = Difference(EmptySet, EmptySet);

        assertEquals(s0, r0);
        assertEquals(EmptySet, r1);
        assertEquals(EmptySet, r2);

        //Difference with a disjoint set
        SymbolSet s1 = new SymbolSet(D, E);
        SymbolSet r3 = Difference(s0, s1);
        assertEquals(s0, r3);

        //Difference with a non-disjoint set
        SymbolSet s2 = new SymbolSet(B, C, D, E);
        SymbolSet r4 = Difference(s0, s2);
        assertEquals(new SymbolSet(A), r4);
    }

    @Test
    void testSymmetricDifference() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal C = new Terminal("C");
        Terminal D = new Terminal("D");
        Terminal E = new Terminal("E");

        //Symmetric difference with the empty set
        SymbolSet s0 = new SymbolSet(A, B, C);
        SymbolSet r0 = SymmetricDifference(s0, EmptySet);
        SymbolSet r1 = SymmetricDifference(EmptySet, s0);
        SymbolSet r2 = SymmetricDifference(EmptySet, EmptySet);

        assertEquals(s0, r0);
        assertEquals(s0, r1);
        assertEquals(EmptySet, r2);

        //Symmetric difference with a disjoint set
        SymbolSet s1 = new SymbolSet(D, E);
        SymbolSet r3 = SymmetricDifference(s0, s1);
        assertEquals(new SymbolSet(A, B, C, D, E), r3);

        //Symmetric difference with a non-disjoint set
        SymbolSet s2 = new SymbolSet(B, C, D, E);
        SymbolSet r4 = SymmetricDifference(s0, s2);
        assertEquals(new SymbolSet(A, D, E), r4);
    }

    @Test
    void testIntersection() {
        Terminal A = new Terminal("A");
        Terminal B = new Terminal("B");
        Terminal C = new Terminal("C");
        Terminal D = new Terminal("D");
        Terminal E = new Terminal("E");

        //Intersection with the empty set
        SymbolSet s0 = new SymbolSet(A, B, C);
        SymbolSet r0 = Intersection(s0, EmptySet);
        SymbolSet r1 = Intersection(EmptySet, s0);
        SymbolSet r2 = Intersection(EmptySet, EmptySet);

        assertEquals(EmptySet, r0);
        assertEquals(EmptySet, r1);
        assertEquals(EmptySet, r2);

        //Intersection with a disjoint set
        SymbolSet s1 = new SymbolSet(D, E);
        SymbolSet r3 = Intersection(s0, s1);
        assertEquals(EmptySet, r3);

        //Intersection with a non-disjoint set
        SymbolSet s2 = new SymbolSet(B, C, D, E);
        SymbolSet r4 = Intersection(s0, s2);
        assertEquals(new SymbolSet(B, C), r4);
    }
}
