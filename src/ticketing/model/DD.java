/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketing.model;

/**
 *
 * @author millerr
 */
public class DD {

    /**
     * @return the ftable
     */
    public String getFtable() {
        return ftable;
    }

    /**
     * @param ftable the ftable to set
     */
    public void setFtable(String ftable) {
        this.ftable = ftable;
    }

    /**
     * @return the puserid
     */
    public String getPuserid() {
        return puserid;
    }

    /**
     * @param puserid the puserid to set
     */
    public void setPuserid(String puserid) {
        this.puserid = puserid;
    }

    /**
     * @return the lane_name
     */
    public String getLane_name() {
        return lane_name;
    }

    /**
     * @param lane_name the lane_name to set
     */
    public void setLane_name(String lane_name) {
        this.lane_name = lane_name;
    }
    private String ftable;
    private String puserid;
    private String lane_name;
}
