package com.example.des;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static com.example.des.DES.*;

public class DESController {

    @FXML
    private TextArea EncryptionText;

    @FXML
    private TextArea DecryptionText;

    private boolean textFile = false;

    private final FileChooser fileChooser = new FileChooser();

    byte[] fileData;

    @FXML
    protected void onTextClick() {
        textFile = false;
    }

    @FXML
    protected void onFileClick() {
        textFile = true;
    }

    @FXML
    protected void onEncryptButtonClick() {
        if (textFile) {
            fileData = encrypt(fileData); // Szyfrowanie danych binarnych
            DES des = new DES();
            DecryptionText.setText(des.bytesToHex(fileData));
        } else if(!textFile) {
            fileData = encrypt(EncryptionText.getText().getBytes()); // Szyfrowanie tekstu po konwersji na surowe dane binarne
            DES des = new DES();
            DecryptionText.setText(des.bytesToHex(fileData));
        }
    }

    @FXML
    protected void onDecryptButtonClick() {
            fileData = decrypt(fileData); // Odszyfrowanie tekstu po konwersji na surowe dane binarne
            DES des = new DES();
            EncryptionText.setText(des.hexToString(des.bytesToHex(fileData)));
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOfDot = name.lastIndexOf('.');
        if (lastIndexOfDot == -1 || lastIndexOfDot == 0 || lastIndexOfDot == name.length() - 1) {
            return null; // Plik nie ma rozszerzenia lub jest ukryty
        }
        return name.substring(lastIndexOfDot + 1);
    }

    @FXML
    protected void onLoadFileButtonClickE() {
        try {
            fileChooser.getExtensionFilters().addAll();
            File file = fileChooser.showOpenDialog(new Stage());
            if(file == null){
                throw new Exception("File");
            }
            Dao<String> daoFile = DaoFactory.getFileDao(file.getPath());
            fileData = daoFile.read();
            EncryptionText.setText(EncryptionText.getText());
        } catch (Exception e) {
            throw new NullPointerException("Nie wybrano pliku");
        }
    }

    @FXML
    protected void onSaveFileButtonClickE() {
        try {
            if (DecryptionText.getText().isEmpty()) {
                throw new NullPointerException("Pole do zapisu jest puste");
            } else {
                fileChooser.getExtensionFilters().addAll();
                File file = fileChooser.showSaveDialog(new Stage());
                if(file == null){
                    throw new Exception("File");
                }
                Dao<String> daoFile = DaoFactory.getFileDao(file.getPath());
                daoFile.write(fileData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @FXML
    protected void onLoadFileButtonClickD() {
        String plain;
        try {
            fileChooser.getExtensionFilters().addAll();
            File file = fileChooser.showOpenDialog(new Stage());
            if(file == null){
                throw new Exception("File");
            }
            Dao<String> daoFile = DaoFactory.getFileDao(file.getPath());
            plain = daoFile.readCipher();
            DecryptionText.setText(plain);
        } catch (Exception e) {
            throw new NullPointerException("Nie wybrano pliku");
        }
    }

    @FXML
    protected void onSaveFileButtonClickD() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        // Dodajemy filtry rozszerzeń dla różnych formatów
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki tekstowe (*.txt)", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki binarne (*.exe)", "*.exe"));

        // Wybierz plik do zapisu
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                // Pobierz tekst do zapisania
                String textToSave = DecryptionText.getText();

                // Sprawdź wybrany filtr
                String selectedExtension = fileChooser.getSelectedExtensionFilter().getDescription();
                boolean isBinary = selectedExtension.equals("Pliki binarne (*.exe)");

                // Zapisz dane do pliku
                if (isBinary) {
                    // Zapisz jako plik binarny
                    byte[] data = hexToBytes(stringToHex(textToSave));
                    Files.write(file.toPath(), data);
                } else {
                    // Zapisz jako plik tekstowy
                    Files.write(file.toPath(), textToSave.getBytes());
                }

                System.out.println("Plik został zapisany.");
            } catch (IOException e) {
                System.err.println("Nie udało się zapisać pliku: " + e.getMessage());
            }
        }
    }
}