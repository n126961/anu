package com.covid.minus.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.covid.minus.entity.ChatHolder;
import com.covid.minus.entity.Message;

public interface MessageRepo extends JpaRepository<Message, Long>{

	@Query("SELECT m from Message m where m.user1 =:user OR m.user2 = :user ORDER by m.messageId")
	List<Message> findAllMessagesForUsers(@Param("user") Long user);
	
	@Query("SELECT new com.covid.minus.entity.ChatHolder (m.user2 as requestId, count(m.messageId) as chatCount) from Message m where m.user1=:user and m.messageId>:lastRead group by m.user2")
	List<ChatHolder> findUnReadMessageCountForUser1(@Param("user") Long user, @Param("lastRead") Long lastRead);
	
	@Query("SELECT new com.covid.minus.entity.ChatHolder (m.user1 as requestId, count(m.messageId) as chatCount) from Message m where m.user2=:user and m.messageId>:lastRead group by m.user1")
	List<ChatHolder> findUnReadMessageCountForUser2(@Param("user") Long user, @Param("lastRead") Long lastRead);
}
