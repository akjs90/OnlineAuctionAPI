package entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the item database table.
 * 
 */
@Entity
@Table(name="item")
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="item_id")
	private int itemId;

	@Column(name="base_price")
	private BigDecimal basePrice;

	private String name;
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name="registration_date")
	private Date registrationDate;

	@Temporal(TemporalType.DATE)
	@Column(name="sold_date")
	private Date soldDate;

	@Column(name="sold_price")
	private BigDecimal soldPrice;

	//bi-directional one-to-one association to Auction
	@OneToOne(mappedBy="item")
	@Fetch(FetchMode.JOIN)
	private Auction auction;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="sold_to")
	private User user;

	//bi-directional many-to-one association to ItemPicture
	@OneToMany(mappedBy="item")
	private List<ItemPicture> itemPictures;

	public Item() {
	}

	public int getItemId() {
		return this.itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getBasePrice() {
		return this.basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getRegistrationDate() {
		return this.registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getSoldDate() {
		return this.soldDate;
	}

	public void setSoldDate(Date soldDate) {
		this.soldDate = soldDate;
	}

	public BigDecimal getSoldPrice() {
		return this.soldPrice;
	}

	public void setSoldPrice(BigDecimal soldPrice) {
		this.soldPrice = soldPrice;
	}

	public Auction getAuction() {
		return this.auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<ItemPicture> getItemPictures() {
		return this.itemPictures;
	}

	public void setItemPictures(List<ItemPicture> itemPictures) {
		this.itemPictures = itemPictures;
	}

	public ItemPicture addItemPicture(ItemPicture itemPicture) {
		getItemPictures().add(itemPicture);
		itemPicture.setItem(this);

		return itemPicture;
	}

	public ItemPicture removeItemPicture(ItemPicture itemPicture) {
		getItemPictures().remove(itemPicture);
		itemPicture.setItem(null);

		return itemPicture;
	}

}