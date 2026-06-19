package com.project.NexPay.merchant.repository;

import com.project.NexPay.merchant.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserRespository extends JpaRepository<AppUser, UUID> {
}
