package com.example.userservice.Repository;

import com.example.userservice.Dto.UserRegistrationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserRegistrationRecord,Long> {
}
