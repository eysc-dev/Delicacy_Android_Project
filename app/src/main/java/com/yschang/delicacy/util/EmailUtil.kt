package com.yschang.delicacy.util

import com.teprinciple.mailsender.Mail //Import for creating mail objects
import com.teprinciple.mailsender.MailSender //Import for sending emails
import java.util.Random //Import for generating random numbers

/***
 * ref documentation:
 * https://support.google.com/a/answer/176600?hl=en
 * https://support.google.com/accounts/answer/185833?hl=en (使用應用程式密碼登入帳戶)
 * https://support.google.com/accounts/answer/185833?hl=zh-Hant (Chinese Version)
 ***/
object EmailUtil {
    fun sendEmail(
        address: String, //Recipient email address
        subject: String, //Email subject line
        content: String //Email body content
    ) {
        val mail = Mail().apply { //Create a new Mail object
            openSSL = true //Enable SSL encryption

            mailServerHost = "smtp.gmail.com" //Gmail server host
            mailServerPort = "465" //Gmail server port
            fromAddress = "paypal90user@gmail.com" //Sender's email address
            password = "yapj hhcr uate deim" //Password for sender's email account

            toAddress = arrayListOf(address) //List of recipient addresses
            this.subject = subject //Set the email subject
            this.content = content //Set the email content
        }
        MailSender.getInstance().sendMail(mail) //Sends the email using the MailSender instance
    }
    fun generateVerificationCode(): String { //Generate a random verification code
        val chars = "0123456789" //Characters to use for the code (digits)
        val random = Random() //Random number generator
        val code = StringBuilder() //Use StringBuilder for using append()
        for (i in 0 until 4) { //Generate a 4-digit code
            code.append(chars[random.nextInt(chars.length)])
            //append() 允許在不重新創建對象的情況下，迅速地構建字符串
        }
        return code.toString() //Return the generated code
    }
}