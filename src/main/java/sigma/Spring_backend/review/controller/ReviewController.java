package sigma.Spring_backend.review.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.review.dto.ReplyRes;
import sigma.Spring_backend.review.dto.ReviewReq;
import sigma.Spring_backend.review.dto.ReviewRes;
import sigma.Spring_backend.review.service.ReviewService;

@Api(tags = "08. 리뷰")
@RestController
@RequestMapping("/v1/api/review")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	private final ResponseService responseService;

	@GetMapping
	@ApiOperation(value = "코디네이터 별 리뷰 & 댓글 조회", notes = "코디네이터에게 달린 모든 리뷰와 댓글을 가져옵니다.")
	public ListResult<ReviewRes> getAllReviews(
			@ApiParam(value = "코디네이터 이메일", required = true) @RequestParam String email
	) {
		try {
			return responseService.getListResult(reviewService.getAllReviewsOfCrdi(email));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping
	@ApiOperation(value = "리뷰 요청", notes = "완료된 코디 예약 건에 대해서 리뷰를 남깁니다.")
	public CommonResult requestReview(
			@ApiParam(value = "리뷰 요청", required = true) @ModelAttribute ReviewReq reviewReq
	) {
		try {
			reviewService.registReview(reviewReq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@DeleteMapping
	@ApiOperation(value = "리뷰 삭제", notes = "리뷰를 삭제합니다.")
	public CommonResult removeReview(
			@ApiParam(value = "리뷰 번호", required = true) @RequestParam Long reviewSeq
	) {
		try {
			reviewService.deActivateReview(reviewSeq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@PutMapping
	@ApiOperation(value = "리뷰 수정", notes = "고객이 리뷰를 수정합니다.")
	public CommonResult updateReview(
			@ApiParam(value = "리뷰 번호", required = true) @RequestParam Long reviewSeq,
			@ApiParam(value = "코디네이터 이메일", required = true) @RequestParam String crdiEmail,
			@ApiParam(value = "수정 내용", required = true) @RequestParam String content
	) {
		try {
			reviewService.updateReview(reviewSeq, crdiEmail, content);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@GetMapping("/report")
	@ApiOperation(value = "신고 된 리뷰 전체 조회", notes = "신고 받은 모든 리뷰를 가져옵니다.")
	public ListResult<ReviewRes> getAllReportedReviews() {
		try {
			return responseService.getListResult(reviewService.getAllReportedReviews());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/report")
	@ApiOperation(value = "리뷰 신고", notes = "작성된 리뷰를 신고합니다.\n(현재 구현은 리뷰를 신고하여도 삭제하지는 않고 신고 리포트만 쌓임)")
	public CommonResult reportReview(
			@ApiParam(value = "리뷰 번호", required = true) @RequestParam Long reviewSeq,
			@ApiParam(value = "리뷰 신고 사유") @RequestParam String reason,
			@ApiParam(value = "리뷰 신고 내용") @RequestParam String content
	) {
		try {
			reviewService.reportReview(reviewSeq, reason, content);
			return responseService.getSuccessResult();
		}  catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/reply")
	@ApiOperation(value = "답글", notes = "리뷰에 대한 답글 작성")
	public CommonResult writeReply(
			@ApiParam(value = "리뷰 번호", required = true) @RequestParam Long reviewSeq,
			@ApiParam(value = "코디이메일") @RequestParam String crdiEmail,
			@ApiParam(value = "답글 내용") @RequestParam String replyContent
	) {
		try {
			reviewService.writeReply(reviewSeq, crdiEmail, replyContent);
			return responseService.getSuccessResult();
		}  catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@DeleteMapping("/reply")
	@ApiOperation(value = "답글 삭제", notes = "답글을 삭제합니다.")
	public CommonResult removeReply(
			@ApiParam(value = "답글 번호", required = true) @RequestParam Long replySeq
	) {
		try {
			reviewService.deActivateReply(replySeq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@PutMapping("/reply")
	@ApiOperation(value = "답글 수정", notes = "답글을 수정합니다.")
	public CommonResult updateRelpy(
			@ApiParam(value = "답글 번호", required = true) @RequestParam Long replySeq,
			@ApiParam(value = "수정 내용", required = true) @RequestParam String content
	) {
		try {
			reviewService.updateReply(replySeq, content);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@GetMapping("/reply")
	@ApiOperation(value = "리뷰별 답글 조회", notes = "답글 조회를 합니다.")
	public SingleResult<ReplyRes> getReply(
			@ApiParam(value = "리뷰 번호", required = true) @RequestParam Long reviewSeq
	) {
		try {
			return responseService.getSingleResult(reviewService.getReply(reviewSeq));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
