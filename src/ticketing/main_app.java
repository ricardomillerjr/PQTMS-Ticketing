
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class main_app extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        validate();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ticketing/fxml/login.fxml"));

        root.setOnMousePressed(
                (MouseEvent event) -> {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                });
        root.setOnMouseDragged(
                (MouseEvent event) -> {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                });
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        stage.show();
    }

    static void validate() {
        try {
            ConnectionManager.getInstance().getConnection().createStatement().addBatch("insert into tblog (select * from ttable where substr(fpdate,1,10)!=substr(now(),1,10))");
            ConnectionManager.getInstance().getConnection().createStatement().addBatch("delete from ttable where substr(fpdate,1,10)!=substr(now(),1,10)");
            int[] executeBatch = ConnectionManager.getInstance().getConnection().createStatement().executeBatch();
            for (int count = 0; count < executeBatch.length; count++) {
                System.out.println("Success [" + count + "] :---: Execute Query Batch [" + executeBatch[count] + "]");
            }
            ConnectionManager.getInstance().getConnection().createStatement().close();
        } catch (SQLException sqlex) {
            System.out.println(sqlex.getMessage());
        }
    }

}
