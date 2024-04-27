module com.example.schnorr {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.schnorr to javafx.fxml;
    exports com.example.schnorr;
}