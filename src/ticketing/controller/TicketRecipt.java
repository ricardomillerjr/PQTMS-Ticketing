
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

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import com.jfoenix.controls.JFXButton;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

import ticketing.ConnectionManager;
import ticketing.Notification;
import ticketing.pacd_user;

/**
 *
 * @author itmu03
 */
public class TicketRecipt implements Initializable {

    HashMap parameters = new HashMap();
    pacd_user puser;
    private Connection _connection = ConnectionManager.getInstance().getConnection();
    String descrip;
    @FXML
    private AnchorPane main_root_anchorPane;
    @FXML
    private Button button_sub;
    @FXML
    private Label lblcounternumber;
    @FXML
    private JFXButton button_print;
    @FXML
    private Label lblpacduser;
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
    private Label label_date;
    @FXML
    private Label lbltype;
    @FXML
    private AnchorPane sub_main_root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    @SuppressWarnings({"unchecked", "unchecked"})
    public void onPrint(ActionEvent event) throws IOException, JRException {
        DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
        JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
                "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
        JasperPrint print = JasperFillManager.fillReport("report/ticketrcp3.jasper",
                parameters,
                new JREmptyDataSource());
        print.setJasperReportsContext(context);
        if (JasperPrintManager.printReport(print, false)) {
            disableWarning();
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
    public void onTicket(String USERID,
            String TokenNumber,
            String DateNow,
            String Type,
            String Lane_Desciption,
            String FirstName,
            String Middelename,
            String LastName,
            String LHIOName,
            String SOAddress) {
        
        puser = new pacd_user();
        parameters.put("queue_number", TokenNumber);
        parameters.put("lane_descrip", Lane_Desciption);
        parameters.put("lhioname", LHIOName);
        parameters.put("dateNow", DateNow);
        parameters.put("puserid", USERID);
        disableWarning();
        
        puser.setUserid(USERID);
        puser.setFirstname(FirstName);
        puser.setMiddlename(Middelename);
        puser.setLastname(LastName);
        puser.setLane(Lane_Desciption);
        lbllhioname.setText(LHIOName);
        lbluid.setText(puser.getUserid());
        lbldate.setText(DateNow);
        label_date.setText(DateNow);
        lblcounternumber.setText(TokenNumber);
        lanename.setText(puser.getLane());
        lbltype.setText(Type);
        lblpacduser.setText(puser.getFirstname() + " " + puser.getMiddlename()+ " " + " " + puser.getLastname());
        lblsoaddress.setText(SOAddress);
    }

    @FXML
    public void oncut(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ticketing/fxml/UserPage.fxml"));
        AnchorPane pane = loader.load();
        UserPageController userpage = loader.getController();
        userpage.getP(puser.getUserid(), puser.getFirstname(), puser.getMiddlename(), puser.getLastname());
        main_root_anchorPane.getChildren().setAll(pane);
    }

//  JRViewer _JasperViewer = new JRViewer(print);
//  _JasperViewer.setZoomRatio(1.25F);
//  JasperViewer.viewReport(jasperPrint, false);
//  JasperViewer.viewReport(print, false);
}
