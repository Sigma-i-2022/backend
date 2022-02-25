package sigma.Spring_backend.awsUtil.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.exception.BussinessException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class AwsService {

	@Value("${cloud.aws.credentials.access-key}")
	private String awsS3AccessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String awsS3SecretKey;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Value("${aws.bucket.name}")
	private String bucketName;

	@Value("${aws.bucket.url}")
	private String bucketUrl;

	private AmazonS3 amazonS3;

	public AmazonS3 createAwsCredentials() {
		AWSCredentials credentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
		return AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region)
				.build();
	}

	public String imageUploadToS3(String uploadPath, MultipartFile multipartFile) {
		StringBuilder imagePathUrl = new StringBuilder();

		amazonS3 = createAwsCredentials();

		String fileName = createFileName(multipartFile);
		String path = "/upload" + uploadPath;

		imagePathUrl.append(bucketUrl)
				.append(path)
				.append("/").append(fileName);

		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(multipartFile.getContentType());
			metadata.setContentLength(multipartFile.getSize());
			amazonS3.putObject(
					new PutObjectRequest(bucketName + path, fileName, multipartFile.getInputStream(), metadata)
							.withCannedAcl(CannedAccessControlList.PublicRead)
			);
			log.info("AWS 이미지 경로 : " + imagePathUrl);
			return imagePathUrl.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException("AWS S3 이미지 업로드 실패");
		}
	}

	private String createFileName(MultipartFile file) {
		String name = file.getOriginalFilename();
		String ext = name.substring(name.lastIndexOf("."));
		String onlyName = name.substring(0, name.lastIndexOf("."));

		ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		String date = nowSeoul.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss"));

		onlyName = onlyName.replaceAll(" ", "_");
		return onlyName + "_" + date + ext;
	}
}
