package com.dongzhi.ow.pojo;

import java.util.Date;

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
@Table(name="gametable")
@JsonIgnoreProperties(value= {"handler","hibernateLazyInitializer"})
public class GameTable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
    private int id;
    private Date gameDate;
    
    @ManyToOne
    @JoinColumn(name="rid_a")
    private Ranks ranks_a;
    
    @ManyToOne
    @JoinColumn(name="rid_b")
    private Ranks ranks_b;

    @ManyToOne
    @JoinColumn(name="gid")
    private Game game;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Ranks getRanks_a() {
		return ranks_a;
	}

	public void setRanks_a(Ranks ranks_a) {
		this.ranks_a = ranks_a;
	}

	public Ranks getRanks_b() {
		return ranks_b;
	}

	public void setRanks_b(Ranks ranks_b) {
		this.ranks_b = ranks_b;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

}