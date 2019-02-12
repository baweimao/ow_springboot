package com.dongzhi.ow.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="game")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Game {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
    private int id;
    private String name;
    private String url;
    private int gameOrder;
    private String color;
    private int up;
    private String info;
    
    @Transient
    private List<Ranks> rs;
    @Transient
    private List<GameTable> gts;
    @Transient
    private List<Live> ls;
    
    public List<Live> getLs() {
		return ls;
	}

	public void setLs(List<Live> ls) {
		this.ls = ls;
	}

	public List<GameTable> getGts() {
		return gts;
	}

	public void setGts(List<GameTable> gts) {
		this.gts = gts;
	}

	public List<Ranks> getRs() {
		return rs;
	}

	public void setRs(List<Ranks> rs) {
		this.rs = rs;
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

    public int getGameOrder() {
        return gameOrder;
    }

    public void setGameOrder(int gameOrder) {
        this.gameOrder = gameOrder;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }
}