// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Controller {
    public TextArea GrammarBox;
    public Button BtnCalc;
    public Label LblGrammar;
    public StackPane LayoutStack;
    public ScrollPane ResultScrollPane;
    public GridPane FirstFollowGrid;

    public void CalculateSets(ActionEvent actionEvent) {
        ResultScrollPane.setVisible(true);
    }
}
