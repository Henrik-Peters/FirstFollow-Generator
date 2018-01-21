// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package ui;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Controller {
    public TextArea GrammarBox;
    public Button BtnCalc;
    public Label LblGrammar;
    public StackPane LayoutStack;
    public ScrollPane ResultScrollPane;
    public GridPane FirstFollowGrid;

    public void CalculateSets(ActionEvent actionEvent) {
        ClearGrid(FirstFollowGrid);
        ResultScrollPane.setVisible(true);

        AddHeadlines(FirstFollowGrid, "Symbol", "FIRST", "FOLLOW");
    }

    private void ClearGrid(GridPane grid) {
        grid.getChildren().clear();
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
    }

    private void AddHeadlines(GridPane grid, String... headlines) {

        for (String headline : headlines) {
            grid.getColumnConstraints().add(new ColumnConstraints(
                    Control.USE_COMPUTED_SIZE,
                    Control.USE_COMPUTED_SIZE,
                    Control.USE_COMPUTED_SIZE,
                    Priority.SOMETIMES,
                    HPos.CENTER,
                    true));
        }

        grid.getRowConstraints().add(new RowConstraints(
                Control.USE_COMPUTED_SIZE,
                Control.USE_COMPUTED_SIZE,
                Control.USE_COMPUTED_SIZE,
                Priority.SOMETIMES,
                VPos.CENTER,
                true));

        for (int i = 0; i < headlines.length; i++) {
            Label label = new Label(headlines[i]);
            label.setMaxWidth(Double.MAX_VALUE);
            label.getStyleClass().add("grid-cell");
            label.getStyleClass().add("grid-cell-headline");

            if (i == 0) {
                label.getStyleClass().add("grid-cell-firstCol");
            }

            grid.add(label, i, grid.getRowCount() - 1);
        }
    }

    private void AddRow(GridPane grid, String... contents) {
        grid.getRowConstraints().add(new RowConstraints(
                Control.USE_COMPUTED_SIZE,
                Control.USE_COMPUTED_SIZE,
                Control.USE_COMPUTED_SIZE,
                Priority.SOMETIMES,
                VPos.CENTER,
                true));

        for (int i = 0; i < contents.length; i++) {
            Label label = new Label(contents[i]);
            label.setMaxWidth(Double.MAX_VALUE);
            label.getStyleClass().add("grid-cell");

            if (i == 0) {
                label.getStyleClass().add("grid-cell-firstCol");
            }

            grid.add(label, i, grid.getRowCount() - 1);
        }
    }
}
