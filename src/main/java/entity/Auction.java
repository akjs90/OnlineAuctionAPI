package entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the auction database table.
 * 
 */
@Entity
@Table(name="auction")
@NamedQuery(name="Auction.findAll", query="SELECT a FROM Auction a")
public class Auction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="item_id")
	private int itemId;

	@Temporal(TemporalType.DATE)
	@Column(name="end_date")
	private Date endDate;

	@Temporal(TemporalType.DATE)
	@Column(name="start_date")
	private Date startDate;

	private char verified;

	//bi-directional one-to-one association to Item
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="item_id")
	private Item item;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="owner_id")
	private User user;

	//bi-directional many-to-one association to Bid
	@OneToMany(mappedBy="auction",fetch=FetchType.LAZY)
	private List<Bid> bids;

	public Auction() {
	}

	public int getItemId() {
		return this.itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public char getVerified() {
		return this.verified;
	}

	public void setVerified(char verified) {
		this.verified = verified;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Bid> getBids() {
		return this.bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public Bid addBid(Bid bid) {
		getBids().add(bid);
		bid.setAuction(this);

		return bid;
	}

	public Bid removeBid(Bid bid) {
		getBids().remove(bid);
		bid.setAuction(null);

		return bid;
	}

}