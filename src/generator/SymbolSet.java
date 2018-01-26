// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Set (no duplicates) to store symbols for context-free grammars.
 * The toString() method will return the set in a readable format.
 */
public class SymbolSet extends LinkedHashSet<Symbol> {

    /**
     * Create a new set of symbols
     * @param symbols Add these symbols to the set
     */
    public SymbolSet(Symbol... symbols) {
        this.addAll(Arrays.asList(symbols));
    }

    @Override
    public String toString() {

        if (isEmpty()) {
            return "{}";

        } else {
            StringBuilder sb = new StringBuilder("{");

            for (Symbol sy : this) {
                sb.append(sy);
                sb.append(", ");
            }

            sb.delete(sb.length() - 2, sb.length());
            sb.append("}");

            return sb.toString();
        }
    }

    /**
     * Create a symbol set from a stream with this collector implementation.
     *
     * Supplier: Create a new empty set as mutable result container
     * Accumulator: Fold elements into the result set simply by adding them
     * Combiner: Union is used to merge two partial result sets
     * Finisher: There is no final result processing so the identity function is used
     * Characteristics: The collector guarantees no order and will use an identity finish function
     */
    private static class SymbolSetCollectorImpl implements Collector<Symbol, SymbolSet, SymbolSet> {

        @Override
        public Supplier<SymbolSet> supplier() {
            return SymbolSet::new;
        }

        @Override
        public BiConsumer<SymbolSet, Symbol> accumulator() {
            return SymbolSet::add;
        }

        @Override
        public BinaryOperator<SymbolSet> combiner() {
            return (left, right) -> {
                if (left.size() < right.size()) {
                    right.addAll(left);
                    return right;
                } else {
                    left.addAll(right);
                    return left;
                }
            };
        }

        @Override
        public Function<SymbolSet, SymbolSet> finisher() {
            return x -> x;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Collector.Characteristics.UNORDERED,
                    Collector.Characteristics.IDENTITY_FINISH);
        }
    }

    /** Singleton for the SymbolSet collector */
    private static final SymbolSetCollectorImpl symbolSetCollector = new SymbolSetCollectorImpl();

    /**
     * Get a collector instance to create symbol sets from streams
     * @return Collector which collects all elements into a symbol set
     */
    public static Collector<Symbol, SymbolSet, SymbolSet> toSymbolSet() {
        return symbolSetCollector;
    }

    /**
     * Create a new set with the elements that are in a or b
     * @param a First set
     * @param b Second set
     * @return The new union set
     */
    public static SymbolSet Union(SymbolSet a, SymbolSet b) {
        SymbolSet unionSet = new SymbolSet();
        unionSet.addAll(a);
        unionSet.addAll(b);
        return unionSet;
    }

    /**
     * Create a new set with the elements of a without the elements of b
     * @param a First set
     * @param b Second set
     * @return The new difference set
     */
    public static SymbolSet Difference(SymbolSet a, SymbolSet b) {
        SymbolSet diffSet = new SymbolSet();
        diffSet.addAll(a);
        diffSet.removeAll(b);
        return diffSet;
    }

    /**
     * Create a new set with all elements that are exclusive in a or b
     * @param a First set
     * @param b Second set
     * @return The new symmetric difference set
     */
    public static SymbolSet SymmetricDifference(SymbolSet a, SymbolSet b) {
        return Union(Difference(a, b), Difference(b, a));
    }

    /**
     * Create a new set with all elements that are in a and b.
     * The elements of a are used to construct the new set.
     * @param a First set
     * @param b Second set
     * @return The new intersection set
     */
    public static SymbolSet Intersection(SymbolSet a, SymbolSet b) {
        SymbolSet intersectSet = new SymbolSet();

        for (Symbol sy : a) {
            if (b.contains(sy)) {
                intersectSet.add(sy);
            }
        }

        return intersectSet;
    }
}
