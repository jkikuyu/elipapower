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
}
*/
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SERIALNUMBER")
@NamedQueries({
    @NamedQuery(name = "SerialNumber.findByName", query = "SELECT s FROM SerialNumber s WHERE s.name=:name")})

public class SerialNumber implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", nullable=false)
    private String name;

	@Column(name = "VALUE",nullable=false )
    private Long value =(long) 0;
	public SerialNumber() {
		
	}
	public long getId() {
		return id;
	}

    public String getName() {
		return name;
	}
	public Long getValue() {
		return value;
	}
	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setValue(Long value) {
		this.value = value;
	}

}
