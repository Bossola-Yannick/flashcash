package com.orkblan.FlashCash.repositories;

import com.orkblan.FlashCash.domain.Link;
import com.orkblan.FlashCash.dto.UserLinkDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Integer>, JpaSpecificationExecutor<Link> {
    @Query(value="SELECT new com.orkblan.FlashCash.dto.UserLinkDto(u2.user_id, u2.email, u2.name) FROM User u JOIN Link l ON l.user1 = u.user_id JOIN User u2 ON u2.user_id = l.user2 WHERE u.user_id = :id")
    List<UserLinkDto> findLinkByUser1(Long id);
}
// requête sql pour lister les amis =>
//SELECT u.email AS u1_mail, u.name AS u1_name, u2.email AS u2_mail, u2.name AS u2_name FROM user u
//JOIN link l ON l.user1 = u.user_id
//JOIN user u2 ON u2.user_id = l.user2
//WHERE u.user_id = :id