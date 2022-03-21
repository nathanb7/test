module testt {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
