module org.example.shopping_cart {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires java.desktop;
    requires javafx.graphics;

    opens org.example.shopping_cart to javafx.fxml;

    opens application to javafx.graphics, javafx.fxml;
    exports application;
    opens view to javafx.graphics, javafx.fxml;
    exports view;
}

