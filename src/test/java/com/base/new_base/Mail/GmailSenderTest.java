package com.base.new_base.Mail;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GmailSenderTest {
    GmailSender sender = new GmailSender();

    @Test
    void sendEmail() throws Exception {
        sender.sendEmail("Код подтверждение на сайте БЧ.com","123321","harasukb@gmail.com");
    }
}