/**
*
* @author Jude Kikuyu
* created on 18/04/2018
* 
*/
package com.ipayafrica.elipapower.model;

/**
*
* @author Jude Kikuyu
* created on 18/04/2018
* 

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SERIALNUMBER")
@NamedQueries({
    @NamedQuery(name = "Serialnumber.findAll", query = "SELECT s FROM Serialnumber s")})
public class Serialnumber implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SERIALID")
    private Long serialid;

    public Serialnumber(Long serialid) {
        this.serialid = serialid;
    }

    public Long getSerialid() {
        return serialid;
    }

    public void setSerialid(Long serialid) {
        this.serialid = serialid;
    }

    @Override
    public String toString() {
        return "com.ipayafrica.elipapower.model.Serialnumber[serialid=" + serialid + "]";
    }

}
*/
