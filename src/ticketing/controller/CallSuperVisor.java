
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing.controller;

import java.io.IOException;

import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;

import javafx.util.Duration;

import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXTextArea;

import ticketing.ConnectionManager;
import ticketing.dao.pacd_user;

/**
 *
 * @author millerr
 */
public class CallSuperVisor implements Initializable {

	private pacd_user puser = new pacd_user();
	Button button_sub;
	@FXML
	private Label lblusername;
	@FXML
	public Label lbllane;
	@FXML
	private TextField txtissues;
	@FXML
	private Label lblclosewin;
	@FXML
	private Label lblsubmit;
	@FXML
	private AnchorPane main_root_anchorPane;

	@FXML
	public void clickSub(MouseEvent event) throws SQLException, IOException {
		Connection connection = ConnectionManager.getInstance().getConnection();
		PreparedStatement preparedStatement
				= connection.prepareStatement(
						"insert into call_supervisor (frontliner,counter,number,lane,flag,remarKs,entry_date) values(?,?,?,?,?,?,NOW())");

		preparedStatement.setString(1, lblusername.getText());
		preparedStatement.setString(2, "PACD");
		preparedStatement.setString(3, "0");
		preparedStatement.setString(4, "PACD");
		preparedStatement.setInt(5, 0);
		preparedStatement.setString(6, txtissues.getText().toUpperCase());

		if (preparedStatement.executeUpdate() == 1) {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(getClass().getResource("/ticketing/fxml/UserPage.fxml"));

			AnchorPane pane = loader.load();
			UserPageController userpage = loader.getController();

			userpage.getP(puser.getUserid(), puser.getFirstname(), puser.getMiddlename(), puser.getLastname());
			main_root_anchorPane.getChildren().add(pane);

			Image img = new Image("/ticketing/img/like-flat-128x128.png");
			Notifications notificationBuilder = Notifications.create();

			notificationBuilder.title("Call Supervisor");
			notificationBuilder.text("Submited");
			notificationBuilder.graphic(new ImageView(img));
			notificationBuilder.hideAfter(Duration.seconds(2.0));
			notificationBuilder.position(Pos.BOTTOM_RIGHT);
			notificationBuilder.hideCloseButton();
			notificationBuilder.show();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
//      txtissues.setStyle("-fx-text-inner-color: black;");
//      txtissues.setStyle("-fx-text-inner-color: black;");
	}

	@FXML
	public void lblclosewin_mouseClick(MouseEvent event) throws IOException {
		if (event.getClickCount() == 1) {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(getClass().getResource("/ticketing/fxml/UserPage.fxml"));

			AnchorPane pane = loader.load();
			UserPageController userpage = loader.getController();

			userpage.getP(puser.getUserid(), puser.getFirstname(), puser.getMiddlename(), puser.getLastname());
			main_root_anchorPane.getChildren().add(pane);
		}
	}

	public void setP(String userid, String FirstName, String Middalename, String LastName) {
		puser.setUserid(userid);
		puser.setFirstname(FirstName);
		puser.setMiddlename(Middalename);
		puser.setLastname(LastName);
		lblusername.setText(puser.getFirstname() + " " + puser.getMiddlename() + " " + puser.getLastname());
	}
}

