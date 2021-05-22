package com.covid.minus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.covid.minus.entity.ChatHolder;
import com.covid.minus.entity.Chats;
import com.covid.minus.repo.ChatsRepo;
import com.covid.minus.util.NumberUtil;

@RestController
public class ChatsController {

	@Autowired
	ChatsRepo repo;
	@Autowired
	NumberUtil util;
	
	@PostMapping("/add-response")
	public Long addChats(ChatHolder holder) {
		Chats chat = new Chats();
		chat.setMessage(holder.getMessage());
		Long sender = util.decryptMobileNumber(holder.getC(), holder.getI());
		chat.setMessageBy(sender);
		chat.setRequestId(holder.getRequestId());
		Chats c = repo.save(chat);
		return c.getChatId();
	}
	
	@GetMapping("/get-responses")
	public List<Chats> getAllChatsForRequest(@RequestParam(name = "requestId") Long requestId){
		Sort sort = Sort.by(Sort.Direction.DESC, "chatId");
		return repo.findAllByRequestId(requestId, sort);
	}
	
	@PostMapping("/unread-chats")
	public List<ChatHolder> findUnreadChatCounts(ChatHolder holder){
		Long userId = util.decryptMobileNumber(holder.getC(), holder.getI());
		if(userId ==0 || userId == null) 
			return null;
		return repo.findChatsCountByChatId(userId, holder.getLastRead());
	}
}
