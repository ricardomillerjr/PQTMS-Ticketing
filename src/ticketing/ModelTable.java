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
	private String UID;
	private String FirstName;
	private String MiddleName;
	private String LastName;

	public ModelTable(String TicketNumber, String Type, String UID, String FirstName, String MiddleName, String LastName) {
		this.TicketNumber = TicketNumber;
		this.Type = Type;
		this.UID = UID;
		this.FirstName = FirstName;
		this.MiddleName = MiddleName;
		this.LastName = LastName;
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
	 * @return the UID
	 */
	public String getUID() {
		return UID;
	}

	/**
	 * @param UID the UID to set
	 */
	public void setUID(String UID) {
		this.UID = UID;
	}

	/**
	 * @return the FirstName
	 */
	public String getFirstName() {
		return FirstName;
	}

	/**
	 * @param FirstName the FirstName to set
	 */
	public void setFirstName(String FirstName) {
		this.FirstName = FirstName;
	}

	/**
	 * @return the MiddleName
	 */
	public String getMiddleName() {
		return MiddleName;
	}

	/**
	 * @param MiddleName the MiddleName to set
	 */
	public void setMiddleName(String MiddleName) {
		this.MiddleName = MiddleName;
	}

	/**
	 * @return the LastName
	 */
	public String getLastName() {
		return LastName;
	}

	/**
	 * @param LastName the LastName to set
	 */
	public void setLastName(String LastName) {
		this.LastName = LastName;
	}

}
