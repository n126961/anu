package com.covid.minus.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.covid.minus.cache.RuntimeCache;
import com.covid.minus.entity.Event;
import com.covid.minus.repo.EventRepo;
import com.covid.minus.util.NumberUtil;

@ExtendWith(MockitoExtension.class)
public class TestEventController {

	@Mock
	EventRepo eventsRepo;
	
	@Mock
	NumberUtil util;
	@Mock
	RuntimeCache cache;
	
	@Test
	public void shouldFindAll() {
		List<Event> events = eventsRepo.findAll();
		assertEquals(Boolean.FALSE, events.isEmpty());
	}

}
