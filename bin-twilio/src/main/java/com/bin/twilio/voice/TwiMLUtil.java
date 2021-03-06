package com.bin.twilio.voice;

import com.twilio.twiml.*;
import com.twilio.twiml.Number;

public class TwiMLUtil {
    public static String buildVoiceResponseAndDial(String say, String salesPhone) throws TwiMLException {
        Number number = new Number.Builder(salesPhone).build();
        return new VoiceResponse.Builder()
                .say(new Say.Builder(say).build())
                .dial(new Dial.Builder().number(number).build())
                .build()
                .toXml();
    }
}
