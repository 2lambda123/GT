package com.bifrost.sokovia.repository;

import com.bifrost.common.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UserData, String> {}
