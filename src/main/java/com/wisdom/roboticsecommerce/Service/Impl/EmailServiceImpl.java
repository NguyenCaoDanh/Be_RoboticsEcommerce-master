package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Dto.ResetPassword;
import com.wisdom.roboticsecommerce.Entities.Account;
import com.wisdom.roboticsecommerce.Repositories.AccountRepository;
import com.wisdom.roboticsecommerce.Service.EmailService;
import com.wisdom.roboticsecommerce.Utils.Utility;
import com.wisdom.roboticsecommerce.Utils.Utils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;
    @Override
    public void sendMail(String email, String link) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("admin@gmail.com", "hỗ trợ Email");
            helper.setTo(email);
            String subject = "Đây là liên kết để đặt lại mật khẩu của bạn";
//            String htmlContent = readFileContent("");
//
//            // Tìm vị trí của link trong nội dung HTML
//            int startIndex = htmlContent.indexOf("href=\"") + 6;
//            int endIndex = htmlContent.indexOf("\"", startIndex);
//            String html = htmlContent.substring(startIndex, endIndex);
            String content = "<p>Xin chào,</p>"
                    + "<p>Bạn đã yêu cầu đặt lại mật khẩu của mình.</p>"
                    + "<p>Nhấp vào liên kết bên dưới để thay đổi mật khẩu của bạn:</p>"
                    +"<button> <a style=\"text-decoration: none\" href=http://localhost:63342/Be_RoboticsEcommerce/src/main/resources/template/changepassword.html?" +link +">" +"Đổi mật khẩu" +"</a></button>"
                    + "<br>"
                    + "<p>Bỏ qua email này nếu bạn nhớ mật khẩu của mình,"
                    + "hoặc bạn chưa thực hiện yêu cầu.</p>";
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String checkEmail(String email) {
        try {
            Utils.validateEmail(email);
            Account account = accountRepository.findByEmail(email);
            if (account == null){
                throw new MessageDescriptorFormatException("Email chưa được đăng ký ");
            }
            account.setToken(Utility.generateRandomToken());
            account.setExpiredToken(LocalDateTime.now());
            accountRepository.save(account);
            return account.getToken();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void resetPassword(ResetPassword resetPassword) {
        Optional<Account> userOptional = Optional.ofNullable(accountRepository.findByToken(resetPassword.getToken()));
        if (userOptional.isEmpty()) {
            throw new MessageDescriptorFormatException("token không hợp lệ");
        }
        LocalDateTime tokenCreationDate = userOptional.get().getExpiredToken();
        if (isTokenExpired(tokenCreationDate)) {
            throw new MessageDescriptorFormatException("token hết hạn");
        }
        Account user = userOptional.get();
        user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
        accountRepository.save(user);

    }
    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }
}
