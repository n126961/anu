package com.covid.minus.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ChatHolder {
	
	private long requestId;
	private int chatCount;
	private long c;
	private int i;
	private long o;
	private long lastRead;
	private String message;
	
	public ChatHolder(long requestId, long chatCount) {
		this.requestId = requestId;
		this.chatCount = (int) chatCount;
	}
}
