
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import ticketing.dao.Counterr;
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
    private Counterr counnterr;
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
    @FXML
    private Button button_print;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonProceed;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        img = new Image("/ticketing/img/logo2.png");
        loader.setLocation(getClass().getResource("/ticketing/fxml/UserPage4.fxml"));
    }

    private void OnProcess(int exec) throws SQLException, IOException {
        CallableStatement callableStatement = ConnectionManager.getInstance().getConnection().prepareCall("{call create_ticket_no(?,?,?)}",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        callableStatement.setString(1, counnterr.getCounter());
        callableStatement.setString(2, counnterr.getType());
        callableStatement.setString(3, counnterr.getUserID());
        if (callableStatement.executeUpdate() == exec) {
            SceneLoad(loader);
        }
    }

    @FXML
    @SuppressWarnings({"unchecked", "unchecked"})
    private void onPrint(ActionEvent event) throws IOException, JRException, SQLException {
        disableWarning();
        Notify(img);
        if (JasperPrintManager.printReport(print, false)) {
            OnProcess(1);
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
            String FType,
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

        counnterr = new Counterr();
        counnterr.setCounter(TokenNumber);
        counnterr.setDate(DateNow);
        counnterr.setDescription(Lane_Desciption);
        counnterr.setType(FType);
        counnterr.setUserID(USERID);

        lbluid.setText(counnterr.getUserID());
        lbldate.setText(counnterr.getDate());
        lblcounternumber.setText(counnterr.getCounter());
        lanename.setText(counnterr.getDescription());
        lbllhioname.setText(LHIOName);
        lblsoaddress.setText(SOAddress);
    }

    @FXML
    private void onClose(ActionEvent event) throws IOException {
        if (event.getSource() == buttonCancel) {
            SceneLoad(loader);
        }
    }

    @FXML
    private void lblcounternumberOnMouseEnter(MouseEvent event) {
        onPop(lblcounternumber);
    }

    protected void onPop(Label label) {
        Label lblticketname = new Label("TicketNumber");
        Label lblticketname2 = new Label("TicketNumber");
        Label lblticketname3 = new Label("TicketNumber");
        VBox vBox = new VBox(lblticketname, lblticketname2, lblticketname3);
        vBox.setPrefHeight(80.0);
        vBox.setPrefWidth(300.0);
        vBox.setStyle("-fx-background-color :#004227");
        PopOver popOver = new PopOver(vBox);
        popOver.show(label);
    }

    @FXML
    private void lanenameOnMouseEnter(MouseEvent event) {
    }

    @FXML
    private void lbluidOnMouseEnter(MouseEvent event) {
    }

    @FXML
    private void lbldateOnMouseEnter(MouseEvent event) {
    }

    @FXML
    private void onProceed(ActionEvent event) throws SQLException, IOException {
        if (event.getSource() == buttonProceed) {
            OnProcess(1);
            SceneLoad(loader);
        }
    }
}
