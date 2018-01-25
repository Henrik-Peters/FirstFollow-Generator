# FirstFollow-Generator

Generate the first, follow and predict sets of a given context-free grammar. These sets are useful for the construction
of predictive parsers to avoid backtracking. This generator comes with a basic user interface to display the sets and
the context-free grammar in a human-friendly format. The purpose of this program is to allow an inside into the parser
construction theory.

## Building

Compiling the program will require the **Java Development Environment 9 or later** and **ant**, to compile the generator use:

```bash
ant compile
```
or create an executable jar file with:

```bash
ant jar
```

## Requirements

Inorder to run the generator you need the **Java Runtime Environment 9 or later**.

## Syntax for context-free grammars

The following rules were used to parse context-free grammars. There is a small grammar example
when you start the program. Make sure that your grammar input satisfies these rules to ensure
a correct parsing process:

* Production rules are separated into left and right by "->"
* The left side of production rules contains exactly one nonterminal
* The right side of production rules has at least one symbol
* All symbols on the left side of productions are nonterminal symbols
* All symbols that are not on any left side of a production are terminal symbols
* Rules with the same left side can be combined into fewer rules with "|"
* Symbols are separated by whitespaces but are not required when one symbol is a nonterminal
* Terminal symbols must be separated by whitespaces
* The end-of-file marker will be added automatically for the follow sets
* The start symbol will be the left side of the first production rule
* The empty word is represented by &#949; 'greek small letter epsilon' (U+03B5)

## Error handling

There is no error handling or error hinting in the grammar parser at the moment. This is done to keep
the parsing process very simple. If you have any problems calculating the sets, it is most likely
that you are violating the syntax rules for context-free grammars from above.
