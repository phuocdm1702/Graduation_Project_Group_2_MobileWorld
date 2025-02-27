package com.example.graduation_project_group_2_mobileworld.repository.san_pham.imel;

import com.example.graduation_project_group_2_mobileworld.entity.Imel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImelRepository extends JpaRepository<Imel, Integer> {

    List<Imel> findByDeletedFalse();

    Page<Imel> findByDeletedFalse(Pageable pageable);

    @Query("SELECT i FROM Imel i " +
            "WHERE i.deleted = false AND " +
            "(LOWER(REPLACE(i.ma, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')) OR " +
            "LOWER(REPLACE(i.imel, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', ''), '%')))")
    Page<Imel> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(i) > 0 FROM Imel i WHERE i.ma = :ma AND i.deleted = false")
    boolean existsByMa(@Param("ma") String ma);

    @Query("SELECT COUNT(i) > 0 FROM Imel i WHERE i.imel = :imel AND i.deleted = false")
    boolean existsByImel(@Param("imel") String imel);

    @Modifying
    @Query("UPDATE Imel i SET i.deleted = true WHERE i.id IN :ids AND i.deleted = false")
    int softDeleteByIds(@Param("ids") List<Integer> ids);
}