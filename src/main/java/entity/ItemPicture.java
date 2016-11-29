package entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the item_pictures database table.
 * 
 */
@Entity
@Table(name="item_pictures")
@NamedQuery(name="ItemPicture.findAll", query="SELECT i FROM ItemPicture i")
public class ItemPicture implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="picture_url")
	private String pictureUrl;

	//bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name="item_id")
	private Item item;

	public ItemPicture() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPictureUrl() {
		return this.pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}