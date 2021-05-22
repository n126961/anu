package com.covid.minus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.covid.minus.cache.RuntimeCache;
import com.covid.minus.entity.OTP;
import com.covid.minus.entity.Session;
import com.covid.minus.util.NumberUtil;

@RestController
public class UserController {
	
	@Autowired
	NumberUtil util;
	
	@Autowired
	RuntimeCache cache;
	
	@PostMapping("/send-otp")
	public boolean sendOtp(Session s) {
		RestTemplate rest = new RestTemplate();
		int otp = util.generateOTP();
		System.out.println("OTP - "+otp);
		System.out.println("C - "+s.getC());
		cache.addOTP(s.getC(), otp);
		/**
		String url = "https://www.fast2sms.com/dev/bulkV2?authorization=dC5IUpGjQ1xFtL7loqJTb2nPD8fyRegZcHOzX0SNAV6k3MBmYrdfzaJHqZ3buXiBNGrgh8FRWOL6DT1w&route=v3&sender_id=TXTIND&message=Your%20OTP%20for%20Minus%20is%20%"+otp+"%22&language=english&flash=0&numbers="+s.getC();
		OTP res = rest.getForObject(url, OTP.class);
		if(res.getMessage() != null && res.getMessage().size()==1) {
			cache.addOTP(s.getC(), otp);
			return res.getMessage().get(0).equals("Message sent successfully");
		}
		return false;
				// https://www.fast2sms.com/dev/bulkV2?authorization=dC5IUpGjQ1xFtL7loqJTb2nPD8fyRegZcHOzX0SNAV6k3MBmYrdfzaJHqZ3buXiBNGrgh8FRWOL6DT1w&route=v3&sender_id=TXTIND&message=Your%20OTP%20for%20Minus%20is%20%22476463%22&language=english&flash=0&numbers=9920476463
				 * 
				 **/
		return true;		 
	}
	
	/**
	 * If OTP is valid then send back following items which will act as a truth of source for all post actions i.e. add new request and add message
	 * 1) c - contact number - its a mobile number * seed + (6745567873)
	 * 2) o - otp number - its a otp number * seed + (644539)
	 * 3) i - initial seed - its a seed position under consideration
	 * @param mobileNumber
	 * @param otp
	 * @return
	 */
	@PostMapping("/validate-otp")
	public String validateOtp(Session s) {
		int cacheOtp = cache.fetchOtp(s.getC());
		if(cacheOtp == s.getO()) {
			return util.encryptUserInfo(s.getC(),cacheOtp);			
		}else{
			return "{\"m\":\"Invalid user/credentials, please login again with your mobile number.\"}";
		}
	}
	
	@PostMapping("/logout")
	public void logout(Session s) {
		cache.clearOtp(util.decryptMobileNumber(s.getC(), s.getI()));
	}

}
