// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.Objects;

/**
 * Represents production rules of context-free grammars.
 * The rule is applied by replacing the left side with the right side.
 */
public class Production {

    /** The left hand side (head) of the production rule */
    public final Nonterminal LeftSide;

    /** The right hand side (body) of the production rule */
    public final Word RightSide;

    /**
     * Create a the production rule
     * @param leftSide The nonterminal for the left hand side
     * @param rightSide The word for the right hand side
     */
    public Production(Nonterminal leftSide, Word rightSide) {
        this.LeftSide = leftSide;
        this.RightSide = rightSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Production)) return false;
        Production other = (Production) o;
        return this.LeftSide.equals(other.LeftSide) && this.RightSide.equals(other.RightSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(LeftSide, RightSide);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LeftSide.toString());
        sb.append(" -> ");
        sb.append(RightSide.toString());
        return sb.toString();
    }
}
