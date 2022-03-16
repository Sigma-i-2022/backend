package sigma.Spring_backend.baseUtil.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfig {

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
		multipartConfigFactory.setMaxFileSize(DataSize.ofMegabytes(10)); // 10MB
		multipartConfigFactory.setMaxRequestSize(DataSize.ofMegabytes(30)); // 30 MB
		return multipartConfigFactory.createMultipartConfig();
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(10000000); // 10MB
		return multipartResolver;
	}
}
