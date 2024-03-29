package sigma.Spring_backend.crdiPage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.crdiPage.dto.CrdiRes;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkReq;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkRes;
import sigma.Spring_backend.crdiPage.service.CrdiPageService;

@Slf4j
@Api(tags = "05. 코디네이터 작품")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/crdi/work")
public class CrdiPageController {

	private final ResponseService responseService;
	private final CrdiPageService crdiPageService;
	private final int FAIL = -1;

	@PostMapping
	@ApiOperation(value = "코디네이터 작품 등록", notes = "코디네이터의 작품을 등록합니다.")
	public CommonResult registCrdiWork(
			@ApiParam(value = "코데네이터 작품 요청 객체") @ModelAttribute CrdiWorkReq crdiWorkReq,
			@ApiParam(value = "이미지 파일", required = true) @RequestParam String uuid
	) {
		try {
			crdiPageService.registCrdiWork(crdiWorkReq, uuid);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@PutMapping("/update")
	@ApiOperation(value = "코디 작품 수정", notes = "코디 작품 정보를 수정합니다.")
	public CommonResult updateCrdiWork(
			@ApiParam(value = "작품 페이지 번호") @RequestParam Long workSeq,
			@ApiParam(value = "작품 요청 객체") @ModelAttribute CrdiWorkReq crdiWorkReq
	){
		try {
			crdiPageService.updateCrdiWork(workSeq,crdiWorkReq);
			return responseService.getSuccessResult();
		}catch (Exception e){
			e.printStackTrace();
			return  responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@PutMapping("/updateImg")
	@ApiOperation(value = "코디 작품 이미지 수정", notes = "코디 작품 이미지를 수정합니다.")
	public CommonResult updateCrdiWorkImg(
			@ApiParam(value = "작품 페이지 번호") @RequestParam Long workSeq,
			@ApiParam(value = "이미지 UUID") @RequestBody String uuid
	){
		try {
			crdiPageService.updateCrdiWorkImg(workSeq,uuid);
			return responseService.getSuccessResult();
		}catch (Exception e){
			e.printStackTrace();
			return  responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@DeleteMapping
	@ApiOperation(value = "코디 작품 삭제", notes = "코디의 작품 삭제합니다.")
	public CommonResult deleteCrdiWork(
			@ApiParam(value = "작품 페이지 번호") @RequestParam Long workSeq
	) {
		try {
			crdiPageService.deleteCrdiWork(workSeq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@GetMapping
	@ApiOperation(value = "코디네이터 작품 조회", notes = "코디네이터의 작품을 조회합니다.")
	public CommonResult getCrdiWork(
			@ApiParam(value = "코데네이터 작품 번호") @RequestParam Long workSeq
	) {
		try {
			return responseService.getSingleResult(crdiPageService.getWork(workSeq));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/all")
	@ApiOperation(value = "코디네이터 작품 전체 조회", notes = "코디네이터 작품을 전체 조회합니다.")
	public ListResult<CrdiWorkRes> getCrdiWorks(
			@ApiParam(value = "코디 이메일") @RequestParam String crdiEmail,
			@ApiParam(value = "PAGE 번호 (0부터)", required = true) @RequestParam(defaultValue = "0") int page,
			@ApiParam(value = "PAGE 크기", required = true) @RequestParam(defaultValue = "20") int size
	) {
		PageRequest pageRequest = PageRequest.of(page,size, Sort.by("updateDate").descending());
		try {
			return responseService.getListResult(crdiPageService.getWorks(crdiEmail,pageRequest));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/list")
	@ApiOperation(value="코디 리스트 조회", notes ="코디 리스트를 조회합니다.")
	public ListResult<CrdiRes> getCrdiList(@ApiParam(value = "고객 이메일") @RequestParam String email){
		try{
			return responseService.getListResult(crdiPageService.getCrdiList(email));
		}catch (Exception e){
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
