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
@Table(name="ranks")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Ranks {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
    private int id;

    private String name;

    private int ranksOrder;
    
    @ManyToOne
    @JoinColumn(name="gid")
    private Game game;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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

    public int getRanksOrder() {
        return ranksOrder;
    }

    public void setRanksOrder(int ranksOrder) {
        this.ranksOrder = ranksOrder;
    }
}