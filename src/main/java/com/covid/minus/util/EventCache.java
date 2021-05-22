package com.covid.minus.util;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.covid.minus.cache.RuntimeCache;
import com.covid.minus.entity.Event;

public class EventCache {
	@Autowired
	RuntimeCache cache;

}
