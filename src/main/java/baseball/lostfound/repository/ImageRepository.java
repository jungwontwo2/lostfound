package baseball.lostfound.repository;

import baseball.lostfound.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query("select c.image from Content c where c.id=:id")
    Image findByContentId(@Param("id") Long id);

}
