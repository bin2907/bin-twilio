package com.bin.twilio;

import com.bin.twilio.voice.TwilioRequestValidator;
import com.twilio.http.TwilioRestClient;
import com.twilio.security.RequestValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BinTwilioApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BinTwilioApplication.class);
	}

	@Bean
	public TwilioRestClient twilioRestClient(@Value("${twilio.account.sid}") String accountSid,
											 @Value("${twilio.auth.token}") String authToken){
		return new TwilioRestClient.Builder(accountSid, authToken).build();
	}

	@Bean
	public TwilioRequestValidator twilioRequestValidator(@Value("${twilio.auth.token}") String authToken) {
		return new TwilioRequestValidator(new RequestValidator(authToken));
	}

	public static void main(String[] args) {
		SpringApplication.run(BinTwilioApplication.class, args);
	}
}
