package animal_shop.tools.abandoned_animal.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    // 이메일을 HTML 형식으로 발송
    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);  // 수신자 이메일
            helper.setSubject(subject);  // 이메일 제목
            helper.setText(body, true);  // HTML 이메일 본문 내용
            // 이메일 발송
            javaMailSender.send(message);


        } catch (MessagingException e) {
            // 이메일 전송 실패 시, 예외를 던져서 호출자에게 전달할 수 있음
        } catch (Exception e) {
            // 일반적인 예외 처리
        }
    }
}
