
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.controlsfx.control.Notifications;
import ticketing.model.pacd_user;

/**
 *
 * @author itmu03
 */
public class TicketRecipt implements Initializable {

    private Image img;
    private final FXMLLoader loader = new FXMLLoader();
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        img = new Image("/ticketing/img/logo2.png");
        loader.setLocation(getClass().getResource("/ticketing/fxml/UserPage.fxml"));
    }

    @FXML
    @SuppressWarnings({"unchecked", "unchecked"})
    private void onPrint(ActionEvent event) throws IOException, JRException {
        disableWarning();
        Notify(img);
        if (JasperPrintManager.printReport(print, false)) {
            SceneLoad(loader);
        } else {
            System.err.println("Error in Printing Ticket...");
        }
    }

    protected void Notify(Image img) {
        Notifications notificationBuilder = Notifications.create();
        notificationBuilder.title("Printing...");
        notificationBuilder.text(lanename.getText() + "\n" + lblcounternumber.getText()).darkStyle();
        notificationBuilder.graphic(new ImageView(img));
        notificationBuilder.hideAfter(Duration.seconds(2.0));
        notificationBuilder.position(Pos.CENTER);
        notificationBuilder.hideCloseButton();
        notificationBuilder.show();
    }

    protected void SceneLoad(FXMLLoader loader) throws IOException {
        AnchorPane pane = loader.load();
        UserPageController userpage = loader.getController();
        userpage.getP(puser.getUserid(), puser.getFirstname(), puser.getMiddlename(), puser.getLastname(), lblcounternumber.getText(), lanename.getText());
        main_root_anchorPane.getChildren().setAll(pane);
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
        SceneLoad(loader);
    }
}
