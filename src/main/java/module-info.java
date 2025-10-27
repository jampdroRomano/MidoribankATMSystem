module com.midoribank.atm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    opens com.midoribank.atm to javafx.fxml;
    exports com.midoribank.atm;
}
