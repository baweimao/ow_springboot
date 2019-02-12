package com.dongzhi.ow.pojo;

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
@Table(name="web")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Web {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
    private int id;
	
	@Column(name = "name")  
    private String name;

	@Column(name = "url")  
    private String url;

	@Column(name = "webOrder")  
    private int webOrder;

    @ManyToOne
    @JoinColumn(name="cid")
    private Category category;
    
    public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public int getWebOrder() {
        return webOrder;
    }

    public void setWebOrder(int webOrder) {
        this.webOrder = webOrder;
    }
}