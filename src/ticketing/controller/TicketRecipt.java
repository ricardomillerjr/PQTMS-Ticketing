
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing.controller;

import java.io.IOException;

import java.net.URL;

import java.sql.Connection;

import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import com.jfoenix.controls.JFXButton;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

import ticketing.ConnectionManager;
import ticketing.Notification;
import ticketing.dao.pacd_user;

/**
 *
 * @author itmu03
 */
public class TicketRecipt implements Initializable {

    HashMap parameters = new HashMap();
    pacd_user puser;
    private Connection connection = ConnectionManager.getInstance().getConnection();
    private JasperPrint print;
    String descrip;
    @FXML
    private AnchorPane main_root_anchorPane;
    @FXML
    private Label lblcounternumber;
    @FXML
    private JFXButton button_print;
    @FXML
    private Label lbldate;
    @FXML
    private Label lbluid;
    @FXML
    private Label lanename;
    @FXML
    private Label lbllhioname;
    @FXML
    private Label lblsoaddress;
    @FXML
    private AnchorPane sub_main_root;
    @FXML
    private JFXButton buttonCancel;
    @FXML
    private Label lbltype;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    @SuppressWarnings({"unchecked", "unchecked"})
    public void onPrint(ActionEvent event) throws IOException, JRException {
        disableWarning();
        if (JasperPrintManager.printReport(print, false)) {
            Notification.Notifier.INSTANCE.notifySuccess("Success", "Printing...");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ticketing/fxml/UserPage.fxml"));
            AnchorPane pane = loader.load();
            UserPageController userpage = loader.getController();
            userpage.getP(puser.getUserid(), puser.getFirstname(), puser.getMiddlename(), puser.getLastname());
            main_root_anchorPane.getChildren().setAll(pane);
        } else {
            Notification.Notifier.INSTANCE.notifyError("Error", "Something Went Wrong");
        }
    }

    public static void disableWarning() {
        System.err.close();
        System.setErr(System.out);
    }

    @SuppressWarnings("unchecked")
    public void onTicket(
            String USERID,
            String TokenNumber,
            String DateNow,
            String Lane_Desciption,
            String FirstName,
            String Middelename,
            String LastName,
            String LHIOName,
            String SOAddress,
            JasperPrint print) {
        this.print = print;
        puser = new pacd_user();

        puser.setUserid(USERID);
        puser.setFirstname(FirstName);
        puser.setMiddlename(Middelename);
        puser.setLastname(LastName);
        puser.setLane(Lane_Desciption);

        lbluid.setText(USERID);
        lbldate.setText(DateNow);
        lblcounternumber.setText(TokenNumber);
        lanename.setText(Lane_Desciption);
        lbllhioname.setText(LHIOName);
        lblsoaddress.setText(SOAddress);
    }

    @FXML
    private void onCancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ticketing/fxml/UserPage.fxml"));
        AnchorPane pane = loader.load();
        UserPageController userpage = loader.getController();
        userpage.getP(puser.getUserid(), puser.getFirstname(), puser.getMiddlename(), puser.getLastname());
        main_root_anchorPane.getChildren().setAll(pane);
    }
}
