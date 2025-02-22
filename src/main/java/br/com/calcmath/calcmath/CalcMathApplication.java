package br.com.calcmath.calcmath;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CalcMathApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CalcMathApplication.class.getResource("calcMath.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 345.0, 554.0);
        stage.setMinWidth(320.0);
        stage.setMinHeight(495.5);
        stage.setResizable(false);
        stage.setTitle("Calc Math");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}