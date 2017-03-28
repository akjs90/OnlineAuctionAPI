package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entity.ItemPicture;

public interface ItemPictureRepository extends JpaRepository<ItemPicture, Integer> {
	@Query("SELECT i.pictureUrl FROM ItemPicture i WHERE i.item.itemId=:item_id")
	String getPicURL(@Param("item_id") int id);
}
