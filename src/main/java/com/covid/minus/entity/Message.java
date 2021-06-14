package com.covid.minus.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Message {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long messageId;
	private long user1;
	private long user2;
	private String message;
}
