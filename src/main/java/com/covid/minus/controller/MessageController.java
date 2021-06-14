package com.covid.minus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.covid.minus.entity.ChatHolder;
import com.covid.minus.entity.Message;
import com.covid.minus.repo.MessageRepo;
import com.covid.minus.util.NumberUtil;

@RestController
public class MessageController {
	
	@Autowired
	MessageRepo repo;
	
	@Autowired
	NumberUtil util;

	@PostMapping("/all-messages")
	public List<Message> getAllChatsForUser(ChatHolder req){
		Long user = util.decryptMobileNumber(req.getC(), req.getI());
		if(user == null || user <1) {
			return null;
		}
		return repo.findAllMessagesForUsers(user);
	}
	
	@PostMapping("/message/unread-count")
	public List<ChatHolder> getUnreadCount(ChatHolder req){
		Long user = util.decryptMobileNumber(req.getC(), req.getI());
		if(user == null || user <1) {
			return null;
		}
	
		List<ChatHolder> group1= repo.findUnReadMessageCountForUser1(user, req.getLastRead());
		List<ChatHolder> group2=repo.findUnReadMessageCountForUser2(user, req.getLastRead());
		if(group1 != null && group1.size()>0 && group2 != null && group2.size()>0) {
			group1.addAll(group2);
			return group1;
		}else if(group1 != null && group1.size()>0) {
			return group1;
		}else if(group2 != null && group2.size()>0) {
			return group2;
		}
		return null;
	}
	
	@PostMapping("/message/add")
	public void addMessage(Message m) {
		repo.save(m);
	}
}
