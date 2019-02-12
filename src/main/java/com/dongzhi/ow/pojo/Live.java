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
@Table(name="live")
@JsonIgnoreProperties({"handler","hibernateLazyInititalizer"})
public class Live {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
    private int id;
    private String url;
    private int liveOrder;

    @ManyToOne
    @JoinColumn(name="gid")
    private Game game;

    @ManyToOne
    @JoinColumn(name="wid")
    private Web web;
    
    public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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

    public int getLiveOrder() {
        return liveOrder;
    }

    public void setLiveOrder(int liveOrder) {
        this.liveOrder = liveOrder;
    }
}