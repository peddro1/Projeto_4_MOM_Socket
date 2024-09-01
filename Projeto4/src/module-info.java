module Projeto4 {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	requires activemq.client;
	requires jakarta.jms.api;
	
	opens application to javafx.graphics, javafx.fxml;
}
