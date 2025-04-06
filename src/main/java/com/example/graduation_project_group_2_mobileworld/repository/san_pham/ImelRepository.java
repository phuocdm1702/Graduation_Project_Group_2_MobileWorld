package com.example.graduation_project_group_2_mobileworld.repository.san_pham;

import com.example.graduation_project_group_2_mobileworld.entity.Imel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImelRepository extends JpaRepository<Imel, Integer> {

    List<Imel> findByDeletedFalse();

    Page<Imel> findByDeletedFalse(Pageable pageable);

    Optional<Imel> findByIdAndDeletedFalse(Integer id);

    Optional<Imel> findByImel(String imel);

    @Query("SELECT COUNT(i) > 0 FROM Imel i WHERE i.ma = :ma AND i.deleted = false")
    boolean existsByMaAndDeletedFalse(@Param("ma") String ma);

    @Query("SELECT COUNT(i) > 0 FROM Imel i WHERE i.imel = :imel AND i.deleted = false")
    boolean existsByImelAndDeletedFalse(@Param("imel") String imel);

    @Query("SELECT COUNT(i) > 0 FROM Imel i WHERE i.ma = :ma AND i.deleted = false AND i.id != :excludeId")
    boolean existsByMaAndDeletedFalse(@Param("ma") String ma, @Param("excludeId") Integer excludeId);

    @Query("SELECT COUNT(i) > 0 FROM Imel i WHERE i.imel = :imel AND i.deleted = false AND i.id != :excludeId")
    boolean existsByImelAndDeletedFalse(@Param("imel") String imel, @Param("excludeId") Integer excludeId);

    @Modifying
    @Query("UPDATE Imel i SET i.deleted = true WHERE i.id IN :ids AND i.deleted = false")
    int softDeleteByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT COUNT(i) > 0 FROM Imel i WHERE i.ma = :ma AND i.deleted = true")
    boolean existsByMaAndDeletedTrue(@Param("ma") String ma);

    @Query("SELECT COUNT(i) > 0 FROM Imel i WHERE i.imel = :imel AND i.deleted = true")
    boolean existsByImelAndDeletedTrue(@Param("imel") String imel);

    @Query("SELECT i FROM Imel i WHERE i.ma = :ma AND i.deleted = true")
    Optional<Imel> findByMaAndDeletedTrue(@Param("ma") String ma);

    @Query("SELECT i FROM Imel i WHERE i.imel = :imel AND i.deleted = true")
    Optional<Imel> findByImelAndDeletedTrue(@Param("imel") String imel);
}