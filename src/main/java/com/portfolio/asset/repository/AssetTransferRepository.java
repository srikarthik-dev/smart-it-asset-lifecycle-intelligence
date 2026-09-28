package com.portfolio.asset.repository;
import com.portfolio.asset.domain.AssetTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetTransferRepository extends JpaRepository<AssetTransfer, Long> {
}
