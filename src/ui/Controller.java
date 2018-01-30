// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package ui;

import generator.*;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import static generator.GrammarParser.*;
import static generator.SetGenerator.*;

public class Controller {
    public TextArea GrammarBox;
    public Button BtnCalc;
    public Label LblGrammar;
    public StackPane LayoutStack;
    public ScrollPane ResultScrollPane;
    public GridPane FirstFollowGrid;
    public GridPane PredictGrid;
    public AnchorPane BackBtn;
    public VBox ResultVBox;

    /** Prototype reference for creating column constraints */
    final ColumnConstraints COLUMN_PROTOTYPE = new ColumnConstraints(
            Control.USE_COMPUTED_SIZE,
            Control.USE_COMPUTED_SIZE,
            Control.USE_COMPUTED_SIZE,
            Priority.SOMETIMES,
            HPos.CENTER,
            true);

    /** Prototype reference for creating row constraints */
    final RowConstraints ROW_PROTOTYPE = new RowConstraints(
            Control.USE_COMPUTED_SIZE,
            Control.USE_COMPUTED_SIZE,
            Control.USE_COMPUTED_SIZE,
            Priority.SOMETIMES,
            VPos.CENTER,
            true);

    /** The style class name for all grid cells */
    final String CELL_STYLE_CLASS = "grid-cell";

    /** The style class name of cells in the first row */
    final String HEADLINE_STYLE_CLASS = "grid-cell-headline";

    /** The style class name of cells in the first column */
    final String FIRST_COLUMN_STYLE_CLASS = "grid-cell-firstCol";

    /** Additionally display the right side of production rules */
    final boolean showFullProductions = true;

    /**
     * Start the grammar parsing, generate all sets,
     * switch to the result page and show the sets
     * @param actionEvent Event data of the button
     */
    public void CalculateSets(ActionEvent actionEvent) {
        ClearGrid(FirstFollowGrid);
        ClearGrid(PredictGrid);
        ResultScrollPane.setVisible(true);
        BackBtn.setVisible(true);

        String[] grammarLines = GrammarBox.getText().split("\n");
        Grammar grammar = ParseGrammar(grammarLines);
        System.out.println("Grammar:");
        System.out.println(grammar.toString());

        Map<Symbol, SymbolSet> firstSets = First(grammar);
        Map<Symbol, SymbolSet> followSets = Follow(grammar, firstSets);
        Map<Production, SymbolSet> predictSets = Predict(grammar, firstSets, followSets);

        AddHeadlines(FirstFollowGrid, "Symbol", "FIRST", "FOLLOW");

        for (Symbol n : grammar.Nonterminals) {
            String symbol = n.toString();

            if (showFullProductions) {
                symbol += " -> " + grammar.Productions.stream()
                        .filter(p -> p.LeftSide == n)
                        .map(p -> p.RightSide.toString())
                        .collect(Collectors.joining(" | "));
            }

            AddRow(FirstFollowGrid, symbol, firstSets.get(n).toString(), followSets.get(n).toString());
        }

        AddHeadlines(PredictGrid, "Production", "PREDICT");

        for (Production p : grammar.Productions) {
            AddRow(PredictGrid, p.toString(), predictSets.get(p).toString());
        }
    }

    /**
     * Navigate back to the grammar input screen
     * @param mouseEvent Event data of the mouse
     */
    public void Back(MouseEvent mouseEvent) {
        ResultScrollPane.setVisible(false);
        BackBtn.setVisible(false);
    }

    /**
     * Remove all children and constraints of a grid
     * @param grid Remove from this grid
     */
    void ClearGrid(GridPane grid) {
        grid.getChildren().clear();
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
    }

    /**
     * Create a new column by a prototype and add it to a grid
     * @param grid Add the new column to this grid
     * @param prototype Create the new column by this reference
     */
    void CreateColumn(GridPane grid, ColumnConstraints prototype) {
        grid.getColumnConstraints().add(new ColumnConstraints(
                prototype.getMinWidth(),
                prototype.getPrefWidth(),
                prototype.getMaxWidth(),
                prototype.getHgrow(),
                prototype.getHalignment(),
                prototype.isFillWidth())
        );
    }

    /**
     * Create a new row by a prototype and add it to a grid
     * @param grid Add the new row to this grid
     * @param prototype Create the new row by this reference
     */
    void CreateRow(GridPane grid, RowConstraints prototype) {
        grid.getRowConstraints().add(new RowConstraints(
                prototype.getMinHeight(),
                prototype.getPrefHeight(),
                prototype.getMaxHeight(),
                prototype.getVgrow(),
                prototype.getValignment(),
                prototype.isFillHeight())
        );
    }

    /**
     * Add headlines to a grid. Maybe you want to
     * clear the grid before calling this function.
     * @param grid Apply the headlines to this grid
     * @param headlines Content of the headlines
     */
    void AddHeadlines(GridPane grid, String... headlines) {

        for (String headline : headlines) {
            CreateColumn(grid, COLUMN_PROTOTYPE);
        }

        CreateRow(grid, ROW_PROTOTYPE);

        for (int i = 0; i < headlines.length; i++) {
            String[] stylesClasses;

            if (i == 0) {
                stylesClasses = new String[]{ CELL_STYLE_CLASS, HEADLINE_STYLE_CLASS, FIRST_COLUMN_STYLE_CLASS };
            } else {
                stylesClasses = new String[]{ CELL_STYLE_CLASS, HEADLINE_STYLE_CLASS };
            }

            SetCell(grid, headlines[i], i, grid.getRowCount() - 1, stylesClasses);
        }
    }

    /**
     * Add data contents to a grid by adding a new row. Make sure
     * that the number of strings matches the number of columns.
     * @param grid Add the new row to this grid
     * @param contents Contents for the new row
     */
    void AddRow(GridPane grid, String... contents) {
        CreateRow(grid, ROW_PROTOTYPE);

        for (int i = 0; i < contents.length; i++) {
            String[] stylesClasses;

            if (i == 0) {
                stylesClasses = new String[]{ CELL_STYLE_CLASS, FIRST_COLUMN_STYLE_CLASS };
            } else {
                stylesClasses = new String[]{ CELL_STYLE_CLASS };
            }

            SetCell(grid, contents[i], i, grid.getRowCount() - 1, stylesClasses);
        }
    }

    /**
     * Set the text of a cell in a grid
     * @param grid The target grid of the cell
     * @param text Text for the cell
     * @param columnIndex Index of the column
     * @param rowIndex Index of the row
     * @param styleClasses Add these style classes to the text label
     */
    void SetCell(GridPane grid, String text, int columnIndex, int rowIndex, String... styleClasses) {
        Label label = new Label(text);
        label.setMinWidth(Region.USE_PREF_SIZE);
        label.setMaxWidth(Double.MAX_VALUE);

        for (String styleClass : styleClasses) {
            label.getStyleClass().add(styleClass);
        }

        grid.add(label, columnIndex, rowIndex);
    }
}
