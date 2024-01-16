package com.replicacia.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.replicacia.model.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByUsername(String userName);
	Optional<AppUser> findByEmail(String email);


	// find user by username and active status
	AppUser findByUsernameAndActiveTrue(String userName);

	List<AppUser> findByActiveIs(Boolean active);

	@Query("update AppUser u set u.loginAttempts = u.loginAttempts - 1 where u.loginAttempts > 0 and  u.username = :username")
	int updateMaxRetryCount(@Param("username") String username);
}
