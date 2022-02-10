package sigma.Spring_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

	@Value("${email.id}")
	private String id;

	@Value("${email.password}")
	private String password;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		Properties props = new Properties();

		props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.enable", "true");

		javaMailSender.setHost("smtp.gmail.com");
		javaMailSender.setPort(465);
		javaMailSender.setUsername(id);
		javaMailSender.setPassword(password);
		javaMailSender.setJavaMailProperties(props);

		return javaMailSender;
	}
}
