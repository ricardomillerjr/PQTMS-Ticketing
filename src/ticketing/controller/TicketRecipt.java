
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing.controller;

import com.github.daytron.simpledialogfx.data.DialogStyle;
import com.github.daytron.simpledialogfx.data.HeaderColorStyle;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.controlsfx.control.Notifications;
import ticketing.dao.pacd_user;

/**
 *
 * @author itmu03
 */
public class TicketRecipt implements Initializable {

    private pacd_user puser;
    private JasperPrint print;
    @FXML
    private AnchorPane main_root_anchorPane;
    @FXML
    private Label lblcounternumber;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    @SuppressWarnings({"unchecked", "unchecked"})
    private void onPrint(ActionEvent event) throws IOException, JRException {
        if (JasperPrintManager.printReport(print, false)) {
            Image img = new Image("/ticketing/img/logo2.png");
            Notifications notificationBuilder = Notifications.create();
            notificationBuilder.title("Call Supervisor");
            notificationBuilder.text("Submited");
            notificationBuilder.graphic(new ImageView(img));
            notificationBuilder.hideAfter(Duration.seconds(2.0));
            notificationBuilder.position(Pos.CENTER);
            notificationBuilder.hideCloseButton();
            notificationBuilder.show();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ticketing/fxml/UserPage.fxml"));
            AnchorPane pane = loader.load();
            UserPageController userpage = loader.getController();
            userpage.getP(puser.getUserid(), puser.getFirstname(), puser.getMiddlename(), puser.getLastname());
            main_root_anchorPane.getChildren().setAll(pane);
            disableWarning();
        } else {
            System.err.println("Error in Printing Ticket...");
        }
    }

    protected static void disableWarning() {
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
        lbluid.setText(puser.getUserid());
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
        Parent pane = loader.load();
        UserPageController userpage = loader.getController();
        //where do this value go? - for inspections
        userpage.getP(puser.getUserid(), puser.getFirstname(), puser.getMiddlename(), puser.getLastname());
        main_root_anchorPane.getChildren().setAll(pane);
//        sub_main_root.getChildren().setAll(pane);
    }
}
