package sigma.Spring_backend.imageUtil.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.imageUtil.entity.Image;
import sigma.Spring_backend.imageUtil.repository.ImageRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	private final AwsService awsService;

	@Transactional
	public String uploadImageFile(MultipartFile imageFile) {

		String imagePathUrl = awsService.imageUploadToS3("/common", imageFile);

		Image image = Image.builder()
				.uuid(UUID.randomUUID().toString())
				.createDate(new DateConfig().getNowDate())
				.imagePathUrl(imagePathUrl)
				.build();

		try {
			imageRepository.save(image);
		} catch (Exception e) {
			throw new BussinessException(ExMessage.DB_ERROR_SAVE);
		}

		return image.getUuid();
	}

	@Transactional(readOnly = true)
	public String requestImageUrl(String uuid) {
		return imageRepository.findByUuid(uuid)
				.orElseThrow(() -> new BussinessException(ExMessage.IMAGE_ERROR_NOT_FOUND))
				.getImagePathUrl();
	}
}
