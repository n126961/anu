package com.covid.minus.config;

import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;

@Configuration
public class Configs {
	
	public Config createOtpConfig() {
		Config config = new Config();

		MapConfig mapConfig = new MapConfig("OTP");
		mapConfig.setTimeToLiveSeconds(600);
		//mapConfig.setMaxIdleSeconds(60);

		config.addMapConfig(mapConfig);
		return config;
	}
	
	public Config createRequestsConfig() {
		Config config = new Config();

		MapConfig mapConfig = new MapConfig("REQUEST");
		mapConfig.setTimeToLiveSeconds(600);
		//mapConfig.setMaxIdleSeconds(60);

		config.addMapConfig(mapConfig);
		return config;
	}

}
