package com.unir.socialhabits.repositories;

import com.unir.socialhabits.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Page<User> findByProfessionalId(
            Long professionalId,
            Pageable pageable
    );

    Page<User>
    findByProfessionalIdAndFirstNameContainingIgnoreCase(

            Long professionalId,

            String search,

            Pageable pageable

    );
}