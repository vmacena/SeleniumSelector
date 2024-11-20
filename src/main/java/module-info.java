module org.macena.seleniumselector {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.macena.seleniumselector to javafx.fxml;
    exports org.macena.seleniumselector;
}