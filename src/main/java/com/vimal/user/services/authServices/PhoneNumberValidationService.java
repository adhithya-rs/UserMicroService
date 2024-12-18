package com.vimal.user.services.authServices;

import java.util.regex.Pattern;

import com.vimal.user.customEceptions.PhoneVerificationCodeException;
import org.springframework.beans.factory.annotation.Value;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class PhoneNumberValidationService {

    @Value("${twilio.accountSid}")
    private String twilioAccountSid;

    @Value("${twilio.authToken}")
    private String twilioAuthToken;
    @Value("${twilio.number}")
    private String twilioPhoneNumber;

    public boolean isPhoneNumberValid(String phoneNumber) {
        String regex = "^(?:(?:\\+\\d{1,3})?(\\d{10}))$";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(phoneNumber).matches();
    }

    public boolean sendVerificationPhoneNumber(String phoneNumber, String code) {
        System.out.println("Checking twiloi acc");
        Twilio.init(twilioAccountSid, twilioAuthToken);
        System.out.println("twilio account created");
        try {
            System.out.println("Phone Number checking");
            if(!isPhoneNumberValid(phoneNumber)){
                throw new PhoneVerificationCodeException("Error sending verification code to Phone Number, retry again later");
            }
            System.out.println("Phone Number is valid");
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    "Your tracker.com mobile verification code is: " + code+" (Valid for 5 mins)"
            ).create();

            System.out.println(message.getSid());
            System.out.println("code sent successfully to mobile phone number");
            return true;
        } catch(Exception e) {
            throw new PhoneVerificationCodeException("Error sending verification code to Phone Number, retry again later");
        }
    }

}
