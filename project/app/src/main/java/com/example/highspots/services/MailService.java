package com.example.highspots.services;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {

    private Context parentContext;
    private AlertDialog parentDialog;

    public MailService(Context parent, AlertDialog dialog) {
        this.parentContext = parent;
        this.parentDialog = dialog;
    }

    public void sendEmail(String emailSubjectSTR, String emailBodySTR) {
        // Send email in the background
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    submitForm(emailSubjectSTR, emailBodySTR);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    return;
                }

                ContextCompat.getMainExecutor(parentContext).execute(()  -> {
                    Toast.makeText(parentContext, "Form submitted successfully!", Toast.LENGTH_SHORT).show();
                    if (parentDialog != null) {
                        parentDialog.dismiss();
                    }
                });
            }
        }).start();
    }

    /**
     * This email is responsible for sending email from highspots.sender@gmail.com to
     * team.highspots@gmail.com.
     * @param emailSubjectSTR - the subject of the email
     * @param emailBodySTR - the body of the email
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    private void submitForm(String emailSubjectSTR, String emailBodySTR) throws UnsupportedEncodingException, MessagingException {
        final String emailPort = "587"; // gmail's smtp port
        final String smtpAuth = "true";
        final String starttls = "true";
        final String emailHost = "smtp.gmail.com";

        final String fromEmail = "highspots.sender@gmail.com";
        final String fromPass = "igxdoxsilkwjumlk";
        List<String> toEmail = new ArrayList<String>() { { add("team.highspots@gmail.com"); } };

        Properties emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);

        // Create message
        Session mailSession = Session.getDefaultInstance(emailProperties, null);
        MimeMessage emailMessage = new MimeMessage(mailSession);
        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail.get(0)));
        emailMessage.setSubject(emailSubjectSTR);
        emailMessage.setText(emailBodySTR);

        // Send message
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPass);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
    }
}
