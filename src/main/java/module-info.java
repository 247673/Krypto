module com.example.des {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.des to javafx.fxml;
    exports com.example.des;
}