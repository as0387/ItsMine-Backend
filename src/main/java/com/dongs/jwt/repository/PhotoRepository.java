package com.dongs.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dongs.jwt.domain.product.Photo;
import com.dongs.jwt.domain.user.User;

public interface PhotoRepository extends JpaRepository<Photo, Long>{
}
