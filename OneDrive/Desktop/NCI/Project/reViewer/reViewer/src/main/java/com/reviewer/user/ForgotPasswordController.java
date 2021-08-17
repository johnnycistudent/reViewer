package com.reviewer.user;

import com.reviewer.Utility;
import net.bytebuddy.utility.RandomString;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Controller
public class ForgotPasswordController {

    @Autowired
    private CustomDetailsService userService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("pageTitle", "Forgot Password");
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPasswordForm(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        String token = RandomString.make(45);

        try {
            userService.updateResetPasswordToken(token, email);
            // generate reset password link
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            // send email
            sendEmail(email, resetPasswordLink);

            model.addAttribute("message", "We have sent a reset password link to your email. Please check your inbox " +
                    "and your spam folder.");


        } catch (UserNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            System.out.println(ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        // set page title
        model.addAttribute("pageTitle", "Forgot Password");

        return "forgot_password_form";
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@ireviewer.com", "re:Viewer Support");
        helper.setTo(email);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello, </p>"
                + "<p>You have requested to reset your password</p>"
                + "<p>Click the link below to change your password</p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\">Change my password</a></b></p>"
                + "<p>Ignore this email if you do no need your password reset or if you have not made this request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model){
        User user = userService.getByResetPasswordToken(token);
        if (user == null) {
            model.addAttribute("pageTitle", "Reset your Password");
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        // add page title + reset password token
        model.addAttribute("pageTitle", "Reset Your Password");
        model.addAttribute("token", token);
        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getByResetPasswordToken(token);
        System.out.println("TOKEN" + token);

        if (user == null) {
            model.addAttribute("pageTitle", "Reset your Password");
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "message";
    }
}
