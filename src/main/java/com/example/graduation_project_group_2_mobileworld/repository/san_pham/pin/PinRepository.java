package com.example.graduation_project_group_2_mobileworld.repository.san_pham.pin;

import com.example.graduation_project_group_2_mobileworld.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends JpaRepository<Pin, Integer> {
}
