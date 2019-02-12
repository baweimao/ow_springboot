package com.dongzhi.ow.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="people")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class People {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
    private int id;
    private String name;
    private String info;
    private int peopleOrder;

	@ManyToOne
    @JoinColumn(name="tid")
    private Type type;
    
    @Transient
    private List<Social> ss;

    public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

    public List<Social> getSs() {
		return ss;
	}

	public void setSs(List<Social> ss) {
		this.ss = ss;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public int getPeopleOrder() {
        return peopleOrder;
    }

    public void setPeopleOrder(int peopleOrder) {
        this.peopleOrder = peopleOrder;
    }

}