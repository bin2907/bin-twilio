package com.bin.twilio.voice;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.twiml.TwiMLException;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

@Controller
public class TwilioVoiceController {

    private final static String SAY_MESSAGE = "Thanks for contacting our sales department. Our " +
            "next available representative will take your call.";

    @Autowired
    private TwilioRestClient restClient;

    @Autowired
    private TwilioRequestValidator requestValidator;

    @Value("${twilio.number}")
    private String twilioNumber;

    @RequestMapping("voice")
    public String index() {
        return "voice";
    }

    @RequestMapping("call")
    public ResponseEntity<String> call(HttpServletRequest request) {
        String userPhone = request.getParameter("userPhone");
        String salesPhone = request.getParameter("salesPhone");

        if (isBlank(userPhone) || isBlank(salesPhone)) {
            return new ResponseEntity<>("Both user and sales phones must be provided", HttpStatus.BAD_REQUEST);
        } else {
            return tryToCallTwilioUsing(userPhone, buildResponseUrl(salesPhone, request));
        }
    }

    @RequestMapping("connect/{salesPhone}")
    public ResponseEntity<String> connect(@PathVariable String salesPhone, HttpServletRequest request) throws TwiMLException {
        if (requestValidator.validate(request)) {
            return new ResponseEntity<>(TwiMLUtil.buildVoiceResponseAndDial(SAY_MESSAGE, salesPhone), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid twilio request", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isBlank(String userPhone) {
        return userPhone == null || userPhone.isEmpty();
    }

    private ResponseEntity<String> tryToCallTwilioUsing(String userPhone, String responseUrl) {
        ResponseEntity<String> response = new ResponseEntity<>("Phone call incoming!", HttpStatus.ACCEPTED);

        try {
            CallCreator callCreator = new CallCreator(new PhoneNumber(userPhone), new PhoneNumber(twilioNumber), new URI(responseUrl));
            callCreator.create(restClient);
        } catch (Exception e) {
            String errorMessage = "Problem while processing request: "+
                    e.getMessage();
            response = new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }

        return response;
    }

    private String buildResponseUrl(String salesPhone, HttpServletRequest request) {
        String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        try {
            return host + "/connect/" + URLEncoder.encode(salesPhone, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new TwilioCallException(e);
        }
    }

}
