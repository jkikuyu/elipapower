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
    @NamedQuery(name = "SerialNumber.findOneByName", query = "SELECT s FROM SerialNumber s WHERE s.name=:name")})

public class SerialNumber implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")

    private Integer id;
    
    @Column(name = "NAME", nullable=false)
    private String name;

	@Column(name = "VALUE",nullable=false )
    private Integer value = 0;
	public SerialNumber() {
		
	}
	public Integer getId() {
		return id;
	}

    public String getName() {
		return name;
	}
	public Integer getValue() {
		return value;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setValue(Integer value) {
		this.value = value;
	}

}
