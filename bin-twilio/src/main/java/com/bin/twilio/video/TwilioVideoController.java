/*
 * Copyright 2011-2017 Nakivo Inc.
 * ALL RIGHTS RESERVED.
 *
 * PROPRIETARY/CONFIDENTIAL.
 *
 * This software is the confidential and proprietary information
 * of Company Inc.
 *
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Company Inc.
 */
package com.bin.twilio.video;

import com.google.gson.Gson;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

/**
 * Author: Binh
 * Date: 14.03.2017
 * Time: 23:07
 */
@Controller
public class TwilioVideoController {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.api.key}")
    private String apiKey;

    @Value("${twilio.api.secret}")
    private String apiSecret;

    @Value("${twilio.configuration.sid}")
    private String configurationSid;


    @RequestMapping("video")
    public String index(Model model) {

        // Generate a random username for the connecting client
        String identity = RandomStringUtils.randomAlphanumeric(5) + RandomStringUtils.randomAlphanumeric(5) + RandomStringUtils.randomAlphanumeric(6);

        // Create Video grant
        VideoGrant grant = new VideoGrant();
        grant.configurationProfileSid = configurationSid;

        // Create access token
        AccessToken token = new AccessToken.Builder(
                accountSid,
                apiKey,
                apiSecret
        ).identity(identity).grant(grant).build();

        // create JSON response payload
        HashMap<String, String> json = new HashMap<>();
        json.put("identity", identity);
        json.put("token", token.toJwt());

        // Render JSON response
        Gson gson = new Gson();

        model.addAttribute("data", gson.toJson(json));

        return "video";
    }

    @RequestMapping("token")
    @ResponseBody
    public String token() {

        // Generate a random username for the connecting client
        String identity = RandomStringUtils.randomAlphanumeric(5) + RandomStringUtils.randomAlphanumeric(5) + RandomStringUtils.randomAlphanumeric(6);

        // Create Video grant
        VideoGrant grant = new VideoGrant();
        grant.configurationProfileSid = configurationSid;

        // Create access token
        AccessToken token = new AccessToken.Builder(
                accountSid,
                apiKey,
                apiSecret
        ).identity(identity).grant(grant).build();

        // create JSON response payload
        HashMap<String, String> json = new HashMap<>();
        json.put("identity", identity);
        json.put("token", token.toJwt());

        // Render JSON response
        Gson gson = new Gson();
        return gson.toJson(json);
    }
}
