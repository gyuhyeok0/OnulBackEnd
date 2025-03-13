package onul.restapi.inquiry.service;

import onul.restapi.inquiry.dto.InquiryDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendInquiryEmail(InquiryDTO inquiryDTO) {

        try {
            String adminEmail = "onulbusiness@gmail.com";
            String subject = "[문의 접수] " + inquiryDTO.getTitle();
            String content = String.format(
                    "📅 문의 날짜: %s\n👤 회원 ID: %s\n📧 이메일: %s\n📌 제목: %s\n\n📝 내용:\n%s\n",
                    inquiryDTO.getCreatedAt(),
                    (inquiryDTO.getMemberId() != null ? inquiryDTO.getMemberId() : "비회원"),
                    inquiryDTO.getEmail(),
                    inquiryDTO.getTitle(),
                    inquiryDTO.getContent()
            );


            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("🚨 이메일 전송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
