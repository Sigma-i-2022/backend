package sigma.Spring_backend.baseUtil.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DateConfig {
	@Bean
	public String getNowDate() {
		return new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
	}
}
