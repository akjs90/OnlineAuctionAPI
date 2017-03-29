package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import entity.ItemPicture;
@Repository
public interface ItemPictureRepository extends JpaRepository<ItemPicture, Integer> {
	ItemPicture findTop1ByItem_ItemId(int id);
}
