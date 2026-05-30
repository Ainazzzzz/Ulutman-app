package com.ulutman.exception;

import org.springframework.mail.MailException;

public class MailSendingException extends RuntimeException {
    public MailSendingException(String string, MailException e) {
    }
}
