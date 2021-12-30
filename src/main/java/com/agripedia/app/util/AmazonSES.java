package com.agripedia.app.util;

import com.agripedia.app.shared.dto.UserDto;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

public class AmazonSES {
    final String FROM = "hapidun@gmail.com";
    final String SUBJECT = "One last step to complete registration";
    final String HTML_BODY = "<h1>Please Verify Your Email Address</h1>"
            + "<a href='http://localhost:8080/verification'";
    final String TEXT_BODY = "<h1>Please Verify Your Email Address</h1>";

    public void verifyEmail(UserDto userDto) {
        AmazonSimpleEmailService emailService = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_1).build();
        SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(userDto.getEmail()))
                .withMessage(new Message()
                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTML_BODY))
                                .withText(new Content().withCharset("UTF-8").withData(TEXT_BODY)))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM);
        emailService.sendEmail(sendEmailRequest);
        System.out.println("Email Send");
    }
}
