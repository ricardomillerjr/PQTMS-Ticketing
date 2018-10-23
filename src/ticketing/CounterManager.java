
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ticketing;

import ticketing.dao.Counterr;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.io.IOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;

/**
 *
 * @author melrose27
 */
public class CounterManager {

    static Counterr bean = new Counterr();
    private static Connection connection = ConnectionManager.getInstance().getConnection();
    static ResultSet resultSet = null;

    public static void _Print() {
        PrinterJob pj = PrinterJob.getPrinterJob();

        pj.setPrintable(new ticketNativePrintable(), getPageFormat(pj));

        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    protected static double convert_CM_To_PPI(double cm) {
        return toPPI(cm * 0.393600787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }

    public static Counterr getNumber(String counter_Lane) throws SQLException, IOException {
        try {
            CallableStatement callableStatement = connection.prepareCall("{call counter_number(?)}",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            callableStatement.setString(1, counter_Lane);
            resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                bean.setCounter(resultSet.getString(1));
                bean.setType(resultSet.getString(4));
                bean.setDescription(resultSet.getString(6));
                bean.setDate(resultSet.getString(8));

                System.out.println("Ticket No: " + bean.getCounter());
                System.out.println("Now :" + bean.getDate());
                System.out.println("Lane Description: " + bean.getDescription());
                System.out.println("Lane type: " + bean.getType());

                return bean;
            } else {
                System.err.println("No Rows Found");

                return null;
            }
        } catch (SQLException sqlex) {
            System.err.println(sqlex.getLocalizedMessage());

            return null;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    public static PageFormat getPageFormat(PrinterJob pj) {
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();
        double middleHeight = 8.0;
        double headerHeight = 2.0;
        double footerHeight = 2.0;
        double width = convert_CM_To_PPI(8);    // printer know only point per inch.default value is 72ppi
        double height = convert_CM_To_PPI(headerHeight + middleHeight + footerHeight);

        paper.setSize(width, height);
        paper.setImageableArea(0, 10, width, height - convert_CM_To_PPI(1));    // define boarder size    after that print area width is about 180 points
        pf.setOrientation(PageFormat.PORTRAIT);    // select orientation portrait or landscape but for this time portrait
        pf.setPaper(paper);

        return pf;
    }

    static class ticketNativePrintable implements Printable {

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            int result = NO_SUCH_PAGE;

            if (pageIndex == 0) {
                Graphics2D g2d = (Graphics2D) graphics;
                double width = pageFormat.getImageableWidth();

                g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

                try {

                    /* Draw Header */
                    int y = 20;
                    int yShift = 10;
                    int headerRectHeight = 15;
                    int headerRectHeighta = 40;

                    ///////////////// Ticket info Get ///////////
                    String pn1a = bean.getCounter();
                    String pn2a = bean.getType();
                    String pn3a = bean.getDate();

                    ///////////////// tICKET ///////////
                    g2d.setFont(new Font("Consolas", Font.PLAIN, 12));
                    g2d.drawString("PHILHEALTH REGIONAL OFFICE XI", 12, y);
                    y += headerRectHeight;
                    g2d.setFont(new Font("Consolas", Font.PLAIN, 9));
                    y += yShift;
                    g2d.drawString("---------------------------", 10, y);
                    y += yShift;
                    g2d.drawString("", 10, y);
                    y += yShift;
                    g2d.setFont(new Font("Consolas", Font.PLAIN, 25));
                    g2d.drawString("  " + pn1a + "", 10, y);
                    y += yShift;
                    g2d.setFont(new Font("Consolas", Font.PLAIN, 9));
                    y += yShift;
                    g2d.drawString("---------------------------", 10, y);
                    y += yShift;
                    g2d.setFont(new Font("Consolas", Font.PLAIN, 12));
                    g2d.drawString("   " + pn2a + "", 10, y);
                    y += yShift;
                    g2d.setFont(new Font("Consolas", Font.PLAIN, 9));
                    y += yShift;
                    g2d.drawString("---------------------------", 10, y);
                    y += yShift;
                } catch (Exception r) {
                    r.printStackTrace();
                }

                result = PAGE_EXISTS;
            }

            return result;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
