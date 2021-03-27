package com.gt.sokovia.repository;

import com.gt.common.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UserData, String> {}
