package org.hameister.javafx.puzzle;


import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;

public class PuzzleApplication extends Application {

	private static final int SIZE = 4;
	private static final int FIELD_SIZE_PIXELS = 50;

	private final StringProperty counter = new SimpleStringProperty("0");
	private final Text youWinText = TextBuilder.create().text("Y o u   w i n!        ").visible(false).styleClass("text-big-puzzle").build();
	
	@Override
	public void start(Stage primaryStage) {
		final Label counterLabel = LabelBuilder.create().text(String.valueOf(counter.get())).styleClass("text-puzzle").layoutX(120).layoutY(20).build();
		counterLabel.textProperty().bind(counter);
		
		BorderPane borderPane = new BorderPane();

		Pane headerPane = new Pane();
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.getChildren().add(TextBuilder.create().text("Counter:").styleClass("text-puzzle").x(50).y(20).build());
		hbox.getChildren().add(counterLabel);
		headerPane.getChildren().add(hbox);

		VBox vBoxLeft = new VBox();
		vBoxLeft.setPadding(new Insets(15, 20, 15, 20));
		vBoxLeft.setSpacing(10);
		VBox vBoxRight = new VBox();
		vBoxRight.setPadding(new Insets(15, 20, 15, 20));
		vBoxRight.setSpacing(10);

		final Pane gamePane = new Pane();
		init(gamePane, new PuzzleModel(SIZE));

		AnchorPane anchorpane = new AnchorPane();
	    Button buttonReset = ButtonBuilder.create()
	    		.text("Reset")
	    		.styleClass("puzzle-reset-button")
	    		.onAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						gamePane.getChildren().clear();
						counter.set("0");
						init(gamePane, new PuzzleModel(SIZE));
						youWinText.setVisible(false);
					}
				})
	    		.build();
	
	    HBox buttonBox = new HBox();
	    buttonBox.setPadding(new Insets(0, 10, 10, 10));
	    buttonBox.setSpacing(10);
	    buttonBox.getChildren().add(youWinText);
	    buttonBox.getChildren().add(buttonReset);
	    
	    AnchorPane.setBottomAnchor(buttonBox, 8.0);
	    AnchorPane.setRightAnchor(buttonBox, 5.0);
		anchorpane.getChildren().add(buttonBox);	    
		
		borderPane.setTop(headerPane);
		borderPane.setCenter(gamePane);
		borderPane.setLeft(vBoxLeft);
		borderPane.setRight(vBoxRight);
		borderPane.setBottom(anchorpane);

		Scene scene = new Scene(borderPane, 400*1.4, 260*1.4);
		
		primaryStage.setTitle("JavaFX - Puzzle");
		primaryStage.setScene(scene);
		primaryStage.getScene().getStylesheets().add("puzzle");
		primaryStage.show();

	}

	public void init(Pane pane, final PuzzleModel model) {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				PuzzleFieldStyle expectedColor = model.getExpectedColorAt(x, y);
				if (expectedColor != null) {
					final Rectangle recExpected = RectangleBuilder.create().x(FIELD_SIZE_PIXELS * x + 280).y(FIELD_SIZE_PIXELS * y).width(FIELD_SIZE_PIXELS).height(FIELD_SIZE_PIXELS).styleClass(expectedColor.getStyle()).build();
					pane.getChildren().add(recExpected);
				}

				PuzzleFieldStyle color = model.getColorAt(x, y);
				if (color != null) {
					final Rectangle rec = RectangleBuilder.create().x(FIELD_SIZE_PIXELS * x).y(FIELD_SIZE_PIXELS * y).width(FIELD_SIZE_PIXELS).height(FIELD_SIZE_PIXELS).styleClass(color.getStyle()).build();
					pane.getChildren().add(rec);

					rec.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							Point moveFromPoint = new Point((int) rec.getX() / FIELD_SIZE_PIXELS, (int) rec.getY() / FIELD_SIZE_PIXELS);
							Point moveToPoint = model.moveToEmptyField(moveFromPoint);
							if (moveToPoint != null) {
								// Increase the counter
								counter.set(String.valueOf(Integer.parseInt(counter.get())+1));
								moveRectangle(moveToPoint, rec);
								model.printBoards();
								if (model.areBoardsEqual()) {
									youWinText.setVisible(true);
								}
							}
						}
					});
				}
			}

		}
		
	}
	
	private void moveRectangle(final Point moveToPoint, final Rectangle rec) {
		rec.setX(moveToPoint.getX() * FIELD_SIZE_PIXELS);
		rec.setY(moveToPoint.getY() * FIELD_SIZE_PIXELS);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
