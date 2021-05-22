package com.covid.minus.util;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.covid.minus.cache.RuntimeCache;

@Component
public class NumberUtil {
	
	@Autowired
	RuntimeCache cache;
	
	public int[] seeds = new int[] {7,14,21,17,12,6,32,22,64,72};
	
	public int generateOTP(){
		Random r = new Random();
		return r.nextInt(999999);
	}
	
	private long encryptMobileNumber(long mobileNumber, int seed) {
		return ( mobileNumber * seeds[seed] ) + 96745567873L;
	}
	
	private int encryptOtp(int otp, int seed) {
		return ( otp * seeds[seed] ) + 9674556;
	}
	
	private int decryptOtp(int otp, int seed) {
		return ((otp - 9674556) / seeds[seed]);
	}
	
	public long decryptMobileNumber(long mobileNumber, int seed) {
		return ((mobileNumber - 96745567873L) / seeds[seed]);
	}
	
	public String encryptUserInfo(long mobileNumber, int cacheOtp) {
		int seed = cacheOtp%10;
		return "{\"c\":"+encryptMobileNumber(mobileNumber, seed)+",\"o\":"+encryptOtp(cacheOtp, seed)+",\"i\":"+seed+"}";
	}
	
	public boolean validateHeader(long u, int o, int i) {
		int otp = decryptOtp(o, i);
		long mob = decryptMobileNumber(u, i);
		int cacheOtp = cache.fetchOtp(mob);
		return cacheOtp == otp;
	}

}
