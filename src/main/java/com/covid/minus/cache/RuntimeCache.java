package com.covid.minus.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.covid.minus.config.Configs;
import com.covid.minus.entity.Event;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class RuntimeCache {
	
	@Autowired
	Configs configs;
	
	IMap<Long, Integer> otpCache = null;
	IMap<String, List<Event>> eventsCache = null;
	
	private void initCache() {
		if(otpCache == null) {
			final HazelcastInstance otpCacheInstance = Hazelcast.newHazelcastInstance(configs.createOtpConfig());
			otpCache = otpCacheInstance.getMap("OTP");
		}
		if(eventsCache == null) {
			final HazelcastInstance otpCacheInstance = Hazelcast.newHazelcastInstance(configs.createOtpConfig());
			eventsCache = otpCacheInstance.getMap("REQUEST");
		}
	}
	
	public void addOTP(Long mobile, Integer otp) {
		if(otpCache == null) {
			initCache();
		}
		otpCache.put(mobile, otp);
	}

	public Integer fetchOtp(Long mobile) {
		if(otpCache == null) {
			initCache();
		}
		return otpCache.get(mobile);

	}

	public void clearOtp(Long mobile) {
		otpCache.remove(mobile);
	}

	public List<Event> getEventsForLocation(String location){
		if(eventsCache == null) {
			initCache();
		}
		return eventsCache.get(location);
	}
	
	public void addEventForLocation(String location, List<Event> events) {
		if(eventsCache == null) {
			initCache();
		}
		eventsCache.set(location, events);
	}

}
