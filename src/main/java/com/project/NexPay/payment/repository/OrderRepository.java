package com.project.NexPay.payment.repository;

import com.project.NexPay.payment.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {

    boolean existsByReceiptAndMerchantId(String receipt, UUID merchantId);

    Optional<OrderRecord> findByIdAndMerchantId(UUID orderId, UUID merchantId);

}
