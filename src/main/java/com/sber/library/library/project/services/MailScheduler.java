package com.sber.library.library.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MailScheduler {

    private UserService userService;
    private JavaMailSender emailSender;

    public MailScheduler(UserService userService, JavaMailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void sendMailsToDebtors() {
        SimpleMailMessage message = new SimpleMailMessage();
        List<String> emails = userService.getUserEmailsWithDelayedRentDate();
        if (emails.size() > 0) {
            message.setTo(emails.toArray(new String[0]));
            message.setSubject("Напоминание о просрочке книги.");
            message.setText("Добрый день!\nВы получили это письмо, так как одна из ваших книг просрочена.\nПожалуйста, верните ее в библиотеку.");
            emailSender.send(message);
        }
        log.info("Планировщик работает!");
    }
}
