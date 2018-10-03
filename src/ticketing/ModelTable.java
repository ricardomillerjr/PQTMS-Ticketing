/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketing;

/**
 *
 * @author JamesUriel
 */
public class ModelTable {

	private String TicketNumber;
	private String Type;

	public ModelTable(String TicketNumber, String Type) {
		this.TicketNumber = TicketNumber;
		this.Type = Type;
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
}
