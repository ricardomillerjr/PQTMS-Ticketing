
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing.controller;

import com.github.daytron.simpledialogfx.data.DialogResponse;
import com.github.daytron.simpledialogfx.data.DialogStyle;
import com.github.daytron.simpledialogfx.data.HeaderColorStyle;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;
import java.awt.HeadlessException;

import java.io.IOException;

import java.net.URL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.collections.FastHashMap;
import org.controlsfx.control.Notifications;
import ticketing.ConnectionManager;
import ticketing.CounterManager;
import ticketing.dao.Counterr;
import ticketing.dao.ModelTable;
import ticketing.dao.pacd_user;

/**
 * FXML Controller class
 *
 * @author millerr
 */
public class UserPageController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    private static final Connection connection = ConnectionManager.getInstance().getConnection();
    private final pacd_user puser = new pacd_user();
    public AnchorPane inner_archpane;
    @FXML
    private FontAwesomeIconView powerbtn_ico;
    @FXML
    private FontAwesomeIconView call_sepico;
    @FXML
    private Label lblpacduser;
    @FXML
    private Label lbldate;
    @FXML
    private AnchorPane root_pane;
    @FXML
    private AnchorPane root_anchorpane;
    @FXML
    private Label lhioname;
    @FXML
    private Button orbtn;
    @FXML
    private Button payment_regular;
    @FXML
    private Button blkbtn;
    @FXML
    private Button opbtn;
    @FXML
    private Button call_supervisor;
    @FXML
    private Button payment_priority;
    @FXML
    private FontAwesomeIconView homebtn_ico;
    private Label lbladdress;
    @FXML
    private TableView<ModelTable> table;
    @FXML
    private TableColumn<ModelTable, String> ticketno;
    @FXML
    private TableColumn<ModelTable, String> type;
    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
    @FXML
    private TableColumn<ModelTable, String> date_Now;
    @FXML
    private Label lblsoaddress;

    protected String Now() {
        SimpleDateFormat SimpleDateFormmatter = new SimpleDateFormat("hh:mm:ss a");
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        return SimpleDateFormmatter.format(sqlDate);
    }

    public void validate_table(String userid) {
        try {
            oblist.clear();
            CallableStatement callableStatement = connection.prepareCall("{call count_ticket(?)}",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            callableStatement.setString(1, userid);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                oblist.add(new ModelTable(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ticketno.setCellValueFactory(new PropertyValueFactory<>("TicketNumber"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        date_Now.setCellValueFactory(new PropertyValueFactory<>("datenow"));
        table.setItems(oblist);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        lbldate.setText(getDateNow());
        try {
            Statement statement = ConnectionManager.getInstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery("select so_name.pro_name,so_name.so_address from so_name");
            if (rs.next()) {
                lhioname.setText(rs.getString(1).toUpperCase());
                lblsoaddress.setText(rs.getString(2));
                puser.setLane(lhioname.getText());
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void load_dd(String ftable, String puserid, String lane_name) throws JRException {
        try {
            Counterr bean = CounterManager.getNumber(ftable);
            if (bean == null) {
                System.err.println("No Rows Found");
            } else {
                try {
                    FastHashMap parameters = new FastHashMap();
                    parameters.put("logo", getClass().getClassLoader().getResource("ticketing/img/logo.png"));
                    parameters.put("queue_number", bean.getCounter());
                    parameters.put("lane_descrip", bean.getDescription());
                    parameters.put("lhioname", lhioname.getText());
                    parameters.put("dateNow", bean.getDate());
                    parameters.put("puserid", puser.getUserid());

                    DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
                    JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
                            "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
                    @SuppressWarnings("unchecked")
                    JasperPrint print = JasperFillManager.fillReport("report/ticketrcp5.jasper", parameters, new JREmptyDataSource());

                    CallableStatement callableStatement = connection.prepareCall("{call create_ticket_no(?,?,?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, bean.getCounter());
                    callableStatement.setString(2, bean.getType());
                    callableStatement.setString(3, puserid);

                    if (callableStatement.executeUpdate() == 1) {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/ticketing/fxml/ticket_recipt.fxml"));
                        AnchorPane pane = loader.load();
                        TicketRecipt ticketrecipt = loader.getController();
                        ticketrecipt.onTicket(
                                puser.getUserid(),
                                bean.getCounter(),
                                bean.getDate(),
                                bean.getDescription(),
                                puser.getFirstname(),
                                puser.getMiddlename(),
                                puser.getLastname(),
                                lhioname.getText(),
                                lblsoaddress.getText(),
                                print);
                        root_pane.getChildren().setAll(pane);
                    }
                } catch (SQLException | HeadlessException ex) {
                    System.err.println(ex.getLocalizedMessage());
                }
            }
        } catch (SQLException | HeadlessException | IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    protected String getCurrentTime() {
        SimpleDateFormat SimpleTimeFormatter = new SimpleDateFormat("hh:mm:ss a");
        Time sqlTime = new Time(new java.util.Date().getTime());
        return SimpleTimeFormatter.format(sqlTime);
    }

    protected String getDateNow() {
        SimpleDateFormat SimpleDateFormmatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        c.setTime(sqlDate);
        c.add(Calendar.DAY_OF_MONTH, 3);
        Date sqlDateCurrent = c.getTime();
        return SimpleDateFormmatter.format(sqlDateCurrent);
    }

    public void getP(String userid, String FirstName, String Middalename, String LastName) {
        puser.setUserid(userid);
        puser.setFirstname(FirstName);
        puser.setMiddlename(Middalename);
        puser.setLastname(LastName);
        lblpacduser.setText(FirstName.toUpperCase() + " " + Middalename.toUpperCase() + " " + LastName.toUpperCase());
        validate_table(userid);
    }

    @FXML
    private void OnClickOR(ActionEvent event) throws JRException {
        load_dd("ORR", puser.getUserid(), "OTHER'S REGULAR");
    }

    @FXML
    private void OnClickOP(ActionEvent event) throws JRException {
        load_dd("OP", puser.getUserid(), "OTHER'S PRIORITY");
    }

    @FXML
    private void onClickPP(ActionEvent event) throws JRException {
        load_dd("PP", puser.getUserid(), "PAYMENT PRIORITY");
    }

    @FXML
    private void onClickPR(ActionEvent event) throws JRException {
        load_dd("PR", puser.getUserid(), "PAYMENT REGULAR");
    }

    @FXML
    private void OnClickBlk(ActionEvent event) throws JRException {
        load_dd("BULK", puser.getUserid(), "BULK TRANSACTION");
    }

    @FXML
    private void OnClckCallSuperVisor(ActionEvent event) throws IOException, SQLException {
        String fullname = puser.getFirstname() + " " + puser.getMiddlename() + " " + puser.getLastname();
        Dialog dialog = new Dialog(
                DialogType.INPUT_TEXT,
                DialogStyle.UNDECORATED,
                "title",
                "Issues And Concern in (PACD)\n " + fullname,
                HeaderColorStyle.GLOSS_MALACHITE,
                "Type Here",
                new SQLException().getNextException());

        dialog.showAndWait();
        if (dialog.getResponse() == DialogResponse.SEND) {
            CallableStatement callableStatement = connection.prepareCall("{call supervisor()}", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            callableStatement.setString(1, fullname);
            callableStatement.setString(2, "PACD");
            callableStatement.setString(3, "0");
            callableStatement.setString(4, "PACD");
            callableStatement.setInt(5, 0);
            callableStatement.setString(6, dialog.getTextEntry());
            if (callableStatement.executeUpdate() == 1) {
                Image img = new Image("/ticketing/img/like-flat-128x128.png");
                Notifications notificationBuilder = Notifications.create();
                notificationBuilder.title("Call Supervisor");
                notificationBuilder.text("Submited");
                notificationBuilder.graphic(new ImageView(img));
                notificationBuilder.hideAfter(Duration.seconds(2.0));
                notificationBuilder.position(Pos.CENTER);
                notificationBuilder.hideCloseButton();
                notificationBuilder.show();
            }
        }
    }

    @FXML
    private void OnClickHomeBtn(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ticketing/fxml/login.fxml"));
            Parent root = loader.load();
            LoginController appController = loader.getController();

            Scene scene = new Scene(root);
            Stage stage2 = new Stage();

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage2.setX(event.getScreenX() - xOffset);
                    stage2.setY(event.getScreenY() - yOffset);
                }
            });

            stage2.initStyle(StageStyle.TRANSPARENT);
            stage2.setScene(scene);
            stage2.centerOnScreen();
            stage2.show();

            Stage stage = new Stage();
            stage = (Stage) ((de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView) event.getSource()).getScene().getWindow();
            stage.close();

            if (stage2.isShowing()) {
                Notifications notificationBuilder = Notifications.create()
                        .title("(PACD) User:")
                        .text("Bye")
                        .graphic(new ImageView(new Image("ticketing/img/logo2.png")))
                        .hideAfter(Duration.seconds(2.0))
                        .position(Pos.TOP_LEFT)
                        .hideCloseButton();
                notificationBuilder.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(UserPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void on_ClickPower(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Stage stage = (Stage) powerbtn_ico.getScene().getWindow();
            stage.close();
            System.exit(0);
        }
    }

}

/*
   String SQl = "insert into ttable(fcnum,fttype,fpdate,flogg,uid) values(?,?,now(),now(),?)";
                PreparedStatement preparedStatement = _connection.prepareStatement(SQl);
                preparedStatement.setString(1, bean.getCounter());
                preparedStatement.setString(2, bean.getType());
                preparedStatement.setString(3, "0");
                if (preparedStatement.executeUpdate() == 1) {
//                    Image img = new Image("/ticketing/img/Add-ticket-icon.png");
//                    Notifications notificationBuilder = Notifications.create()
//                            .title("Ticket Number: " + bean.getCounter())
//                            .text(lane_name)
//                            .graphic(new ImageView(img))
//                            .hideAfter(Duration.seconds(7.0))
//                            .position(Pos.BOTTOM_RIGHT)
//                            .hideCloseButton();
//                    notificationBuilder.show();
 */


//~ Formatted by Jindent --- http://www.jindent.com
