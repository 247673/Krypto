package com.example.des;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DESController {

    @FXML
    private TextArea WriteText;

    @FXML
    private TextArea ReadText;

    private final DESHelper desHelp = new DESHelper();

    @FXML
    protected void onEncryptButtonClick() {
        String plainText = WriteText.getText();
        String encryptedText = desHelp.encrypt(plainText);
        ReadText.setText(encryptedText);
    }

    @FXML
    protected void onDecryptButtonClick() {
        String encryptedText = WriteText.getText();
        String decryptedText = desHelp.decrypt(encryptedText);
        ReadText.setText(decryptedText);
    }

    @FXML
    protected void onLoadFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                WriteText.setText(content);
            } catch (Exception e) {
                System.err.println("Nie udało się załadować pliku: " + e.getMessage());
            }
        }
    }
}