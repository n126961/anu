package com.covid.minus.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.covid.minus.entity.ChatHolder;
import com.covid.minus.entity.Chats;

public interface ChatsRepo extends JpaRepository<Chats, Long>{

	List<Chats> findAllByRequestId(Long requestId, Sort sort);
	
	@Query("SELECT new com.covid.minus.entity.ChatHolder (c.requestId , COUNT(c.chatId) as chatCount) FROM Chats c INNER JOIN REQUESTS r ON r.requestId = c.requestId WHERE r.userId =:userId AND r.isOpen=TRUE AND c.chatId > :lastReadId GROUP BY c.requestId")
	List<ChatHolder> findChatsCountByChatId(@Param("userId") Long userId, @Param("lastReadId") Long lastReadId);
}
