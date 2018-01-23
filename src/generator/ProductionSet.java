// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package generator;

import java.util.HashSet;

/**
 * Set (no duplicates) to store productions for context-free grammars.
 * The toString() method will return the set in a readable format.
 */
public class ProductionSet extends HashSet<Production> {

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";

        } else {
            StringBuilder sb = new StringBuilder("{");

            for (Production p : this) {
                sb.append(p);
                sb.append(", ");
            }

            sb.delete(sb.length() - 2, sb.length());
            sb.append("}");

            return sb.toString();
        }
    }
}
