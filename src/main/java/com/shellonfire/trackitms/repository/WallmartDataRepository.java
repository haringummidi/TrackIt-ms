package com.shellonfire.trackitms.repository;

import com.shellonfire.trackitms.entity.WallmartData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WallmartDataRepository extends JpaRepository<WallmartData, String> {
    WallmartData findByProductUrl(String productUrl);
}