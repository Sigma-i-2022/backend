package sigma.Spring_backend.imageUtil.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.imageUtil.service.ImageService;

@Api(tags = "15. 이미지 업로드")
@RestController
@RequestMapping("/v1/api/image")
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;
	private final ResponseService responseService;

	@ApiOperation(value = "이미지 업로드", notes = "이미지를 업로드한 후 UUID를 반환받습니다.")
	@PostMapping
	public SingleResult<String> uploadImageFile(
			@ApiParam(value = "이미지 파일", required = true) @RequestParam MultipartFile imageFile
	) {
		try {
			return responseService.getSingleResult(
					imageService.uploadImageFile(imageFile)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@ApiOperation(value = "이미지 요청", notes = "UUID를 통해 이미지 URL을 받습니다.")
	@GetMapping
	public SingleResult<String> getImageUrl(
			@ApiParam(value = "이미지 UUID", required = true) @RequestParam String uuid
	) {
		try {
			return responseService.getSingleResult(
					imageService.requestImageUrl(uuid)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
