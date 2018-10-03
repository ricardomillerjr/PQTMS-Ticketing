
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing.controller;

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
import org.controlsfx.control.Notifications;
import ticketing.ConnectionManager;
import ticketing.CounterManager;
import ticketing.Counterr;
import ticketing.ModelTable;
import ticketing.pacd_user;

/**
 * FXML Controller class
 *
 * @author millerr
 */
public class UserPageController implements Initializable {

	private double xOffset = 0;
	private double yOffset = 0;

	private static final Connection _connection = ConnectionManager.getInstance().getConnection();
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
	@FXML
	private Label lbladdress;
	@FXML
	private TableView<ModelTable> table;
	@FXML
	private TableColumn<ModelTable, String> ticketno;
	@FXML
	private TableColumn<ModelTable, String> type;
	@FXML
	private TableColumn<ModelTable, String> uid;
	@FXML
	private TableColumn<ModelTable, String> fname;
	@FXML
	private TableColumn<ModelTable, String> mname;
	@FXML
	private TableColumn<ModelTable, String> lname;

	ObservableList<ModelTable> oblist = FXCollections.observableArrayList();

	protected String Now() {
		SimpleDateFormat SimpleDateFormmatter = new SimpleDateFormat("hh:mm:ss a");
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		return SimpleDateFormmatter.format(sqlDate);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		lbldate.setText(getDateNow());
		try {
			Statement statement = ConnectionManager.getInstance().getConnection().createStatement();
			ResultSet rs = statement.executeQuery("select so_name.so_name,so_name.so_address from so_name");
			if (rs.next()) {
				lhioname.setText(rs.getString(1));
				lbladdress.setText(rs.getString(2));
				puser.setLane(lhioname.getText());
			}
		} catch (SQLException ex) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	protected void load_dd(String ftable, String puserid) {
		try {
			Counterr bean = CounterManager.getNumber(ftable);
			if (bean == null) {
				System.err.println("No Rows Found");
			} else {
				try {
					PreparedStatement preparedStatement = _connection.prepareStatement("insert into ttable(fcnum,fttype,fpdate,flogg,uid) values(?,?,now(),now(),?)");
					preparedStatement.setString(1, bean.getCounter());
					preparedStatement.setString(2, bean.getType());
					preparedStatement.setString(3, puserid);
					if (preparedStatement.executeUpdate() == 1) {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/ticketing/fxml/ticket_recipt.fxml"));
						AnchorPane pane = loader.load();
						TicketRecipt ticketrecipt = loader.getController();
						ticketrecipt.onTicket(
								puser.getUserid(),
								bean.getCounter(),
								bean.getDate(),
								bean.getType(),
								bean.getDescription(),
								puser.getFirstname(),
								puser.getMiddlename(),
								puser.getLastname(),
								lhioname.getText(),
								lbladdress.getText());
						root_pane.getChildren().setAll(pane);
					}
				} catch (SQLException ex) {
					System.err.println(ex.getMessage());
					Logger.getLogger(TicketRecipt.class.getName()).log(Level.SEVERE, null, ex);
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
		lblpacduser.setText(FirstName + " " + Middalename + " " + LastName);

		try {
			Statement statement = ConnectionManager.getInstance().getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT Count(tblog.findex) as 'Ticket Count', tblog.fttype, tblog.uid, acctlist.fname, acctlist.lname, acctlist.mname FROM tblog, acctlist WHERE tblog.uid = acctlist.userid AND tblog.uid = '" + puser.getUserid() + "' GROUP BY tblog.fttype;");
			while (rs.next()) {
				oblist.add(new ModelTable(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
			}
		} catch (SQLException ex) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
		}
		ticketno.setCellValueFactory(new PropertyValueFactory<>("TicketNumber"));
		type.setCellValueFactory(new PropertyValueFactory<>("Type"));
		uid.setCellValueFactory(new PropertyValueFactory<>("UID"));
		fname.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
		mname.setCellValueFactory(new PropertyValueFactory<>("MiddleName"));
		lname.setCellValueFactory(new PropertyValueFactory<>("LastName"));
		
		table.setItems(oblist);

	}

	@FXML
	private void OnClickOR(ActionEvent event) {
		load_dd("ORR", puser.getUserid());
	}

	@FXML
	private void OnClickOP(ActionEvent event) {
		load_dd("OP", puser.getUserid());
	}

	@FXML
	private void onClickPP(ActionEvent event) {
		load_dd("PP", puser.getUserid());
	}

	@FXML
	private void onClickPR(ActionEvent event) {
		load_dd("PR", puser.getUserid());
	}

	@FXML
	private void OnClickBlk(ActionEvent event) {
		load_dd("BULK", puser.getUserid());
	}

	@FXML
	private void OnClckCallSuperVisor(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/ticketing/fxml/callspvr.fxml"));
		AnchorPane pane = loader.load();
		CallSuperVisor callspvr = loader.getController();
		callspvr.setP(puser.getUserid(), puser.getFirstname(), puser.getMiddlename(), puser.getLastname());
		System.out.println(puser.getUserid());
		System.out.println(puser.getFirstname());
		System.out.println(puser.getMiddlename());
		System.out.println(puser.getLastname());
		root_pane.getChildren().setAll(pane);
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
