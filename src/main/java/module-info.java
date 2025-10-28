module com.midoribank.atm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;

    opens com.midoribank.atm to javafx.fxml;
    exports com.midoribank.atm;
}
