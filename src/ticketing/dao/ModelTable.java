/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketing.dao;

/**
 *
 * @author JamesUriel
 */
public class ModelTable {

    private String TicketNumber;
    private String Type;
    private String datenow;

    public ModelTable(String TicketNumber, String Type, String datenow) {
        this.TicketNumber = TicketNumber;
        this.Type = Type;
        this.datenow = datenow;
    }

    /**
     * @return the TicketNumber
     */
    public String getTicketNumber() {
        return TicketNumber;
    }

    /**
     * @param TicketNumber the TicketNumber to set
     */
    public void setTicketNumber(String TicketNumber) {
        this.TicketNumber = TicketNumber;
    }

    /**
     * @return the Type
     */
    public String getType() {
        return Type;
    }

    /**
     * @param Type the Type to set
     */
    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * @return the datenow
     */
    public String getDatenow() {
        return datenow;
    }

    /**
     * @param datenow the datenow to set
     */
    public void setDatenow(String datenow) {
        this.datenow = datenow;
    }

}
