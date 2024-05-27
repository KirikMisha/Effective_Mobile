package org.example.effectivemobile.repositories;

import org.example.effectivemobile.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByLogin(String login);
    Optional<Users> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByFullName(String fullName);
    boolean existsByLogin(String login);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmailOrFullNameOrLoginOrPhoneNumber(String email, String fullName, String login, String phoneNumber);


    Optional<Users> findByPhoneNumber(String phoneNumber);


    @Query("SELECT u FROM Users u " +
            "WHERE (:fullName IS NULL OR u.fullName LIKE CONCAT(:fullName, '%')) " +
            "AND (:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber) " +
            "AND (:email IS NULL OR u.email = :email) " +
            "AND (:dateOfBirth IS NULL OR u.dateOfBirth > :dateOfBirth)"
    )

    Page<Users> searchUsers(@Param("fullName") String fullName,
                            @Param("phoneNumber") String phoneNumber,
                            @Param("email") String email,
                            @Param("dateOfBirth") String dateOfBirth,
                            Pageable pageable);

}
