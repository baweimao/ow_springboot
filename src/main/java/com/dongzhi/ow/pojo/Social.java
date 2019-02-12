package com.dongzhi.ow.pojo;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="social")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Social {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
    private int id;

    private String url;

    private int socialOrder;

    @ManyToOne
    @JoinColumn(name="pid")
    private People people;

    @ManyToOne
    @JoinColumn(name="wid")
    private Web web;

    public People getPeople() {
		return people;
	}

	public void setPeople(People people) {
		this.people = people;
	}

	public Web getWeb() {
		return web;
	}

	public void setWeb(Web web) {
		this.web = web;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public int getSocialOrder() {
        return socialOrder;
    }

    public void setSocialOrder(int socialOrder) {
        this.socialOrder = socialOrder;
    }
}