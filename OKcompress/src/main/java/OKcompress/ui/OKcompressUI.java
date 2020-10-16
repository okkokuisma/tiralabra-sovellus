
package OKcompress.ui;

import OKcompress.utils.Service;
import java.io.File;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class OKcompressUI extends Application {
    Service service;
    String filePath;
    
    @Override
    public void init() {
        service = new Service();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
        Label label = new Label("Drag a file to me.");
        VBox dragTarget = new VBox();
        dragTarget.getChildren().add(label);
        dragTarget.setAlignment(Pos.CENTER);

        GridPane toggles = new GridPane();
        Label fileName = new Label("");
        
        ToggleGroup algorithms = new ToggleGroup();
        RadioButton huffmanButton = new RadioButton("Huffman");
        huffmanButton.setToggleGroup(algorithms);
        RadioButton lzssButton = new RadioButton("LZSS");
        lzssButton.setToggleGroup(algorithms);
        RadioButton deflateButton = new RadioButton("DeflateLite");
        deflateButton.setToggleGroup(algorithms);
        deflateButton.setSelected(true);
        
        Button encodeButton = new Button("Encode");
        encodeButton.setOnMouseClicked((event) -> {
            if (algorithms.getSelectedToggle() == huffmanButton) {
                service.huffmanEncode(filePath);
            } else if (algorithms.getSelectedToggle() == lzssButton) {
                service.lzssEncode(filePath);
            } else {
                service.deflateLiteEncode(filePath);
            }
            root.getChildren().remove(toggles);
            root.getChildren().add(dragTarget);
        });
        
        Button decodeButton = new Button("Decode");
        decodeButton.setOnMouseClicked((event) -> {
            service.decode(filePath);
            root.getChildren().remove(toggles);
            root.getChildren().add(dragTarget);
        });
        
        Button backButton = new Button("Change file");
        backButton.setOnMouseClicked((event) -> {
            root.getChildren().remove(toggles);
            root.getChildren().add(dragTarget);
        });
        
        toggles.add(fileName, 1, 0);
        toggles.addRow(1, new Label("1"), deflateButton, huffmanButton, lzssButton, encodeButton);
        toggles.addRow(2, new Label("2"), decodeButton);
        toggles.addRow(3, new Label("3"), backButton);
        toggles.setVgap(10);
        toggles.setHgap(10);
        toggles.setAlignment(Pos.CENTER);
        
        dragTarget.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != dragTarget && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        dragTarget.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    File selectedFile = db.getFiles().get(0);
                    filePath = selectedFile.getAbsolutePath();
                    fileName.setText(selectedFile.getName());
                    root.getChildren().remove(dragTarget);
                    root.getChildren().add(toggles);
                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            }
        });


        root.getChildren().add(dragTarget);

        Scene scene = new Scene(root, 500, 250);

        stage.setTitle("OKcompress");
        stage.setScene(scene);
        stage.show();
    }
    
}
