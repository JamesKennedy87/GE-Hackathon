package com.hackathon.nku.model;

import java.util.Date;
import java.util.List;

public class Email {
    String sender;
    List<String> recipient;
    String subject;
    String body;
    Date sendDate;
    String attachment;

    public Email(String sndr, List<String> recip, String sbjct,
                 String bdy, Date date, String attch){
        sender = sndr;
        recipient = recip;
        subject = sbjct;
        body = bdy;
        sendDate = date;
        attachment = attch;

    }

}
