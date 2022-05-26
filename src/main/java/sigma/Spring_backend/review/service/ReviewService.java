package sigma.Spring_backend.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;
import sigma.Spring_backend.reservation.repository.ReservationRepo;
import sigma.Spring_backend.review.dto.ReplyRes;
import sigma.Spring_backend.review.dto.ReviewReq;
import sigma.Spring_backend.review.dto.ReviewRes;
import sigma.Spring_backend.review.entity.Reply;
import sigma.Spring_backend.review.entity.Review;
import sigma.Spring_backend.review.repository.ReplyRepository;
import sigma.Spring_backend.review.repository.ReviewRepo;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReservationRepo reservationRepo;
	private final MemberRepository memberRepository;
	private final ReviewRepo reviewRepo;
	private final ReplyRepository replyRepository;

	@Transactional
	public void registReview(ReviewReq reviewReq) {
		boolean verify = verifyReq(reviewReq);
		if (!verify) throw new BussinessException(ExMessage.REVIEW_ERROR_FORMAT);

		Member coordinator = memberRepository.findByEmailFJ(reviewReq.getCoordinatorEmail())
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		Member reviewer = memberRepository.findByEmailFJ(reviewReq.getReviewerEmail())
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

		try {
			coordinator.addReview(reviewReq.toEntity(reviewer.getUserId(), coordinator.getUserId()));
		} catch (Exception e) {
			throw new BussinessException(ExMessage.DB_ERROR_SAVE);
		}
	}

	private boolean verifyReq(ReviewReq reviewReq) {
		if (memberRepository.findByEmailFJ(reviewReq.getCoordinatorEmail()).isEmpty())
			return false;
		if (memberRepository.findByEmailFJ(reviewReq.getReviewerEmail()).isEmpty())
			return false;
		reservationRepo.findById(reviewReq.getReservationSeq())
				.ifPresentOrElse(
						R -> {
							if (R.getConfirmPayYn().equals("N")) {
								throw new BussinessException("구매확정이 되지 않았습니다.");
							} else if (R.getReviewedYn().equals("Y")) {
								throw new BussinessException(ExMessage.REVIEW_ERROR_ALREADY_REVIEWED);
							} else R.setReviewedYn("Y");
						}
						, () -> {
							throw new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND);
						}
				);
		return true;
	}

	@Transactional
	public void reportReview(Long reviewSeq, String reason, String content) {
		Review review = reviewRepo.findById(reviewSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.REVIEW_ERROR_NOT_FOUND));
		review.setReportReason(reason);
		review.setReportContent(content);
		/*
			TODO : 차단 관련 기능 구현 필요
		 	현재 진짜로 차단하지는 않음. (Y: 리뷰 활성화 , N : 리뷰 비활성화)
		 	추후 신고 카운트 후 일정 개수 이상이면 차단하는 등의 기능 필요
		 */
		review.setActivateYn("Y");
		review.setReportedYn("Y");
	}

	@Transactional
	public void writeReply(Long reviewSeq, String crdiEmail, String replyContent) {
		Review review = reviewRepo.findById(reviewSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.REVIEW_ERROR_NOT_FOUND));
		Reply reply = new Reply();

		reply.setCrdiEmail(crdiEmail);
		reply.setReplyContent(replyContent);
		reply.setActiveYN("Y");
		review.setReply(reply);
		try {
			replyRepository.save(reply);
		} catch (Exception e) {
			throw new BussinessException(ExMessage.DB_ERROR_SAVE);
		}
	}

	@Transactional(readOnly = true)
	public List<ReviewRes> getAllReviewsOfCrdi(String email) {
		Member crdi = memberRepository.findByEmailFJ(email)
				.filter(M -> M.getCrdiYn().equals("Y"))
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

		return crdi.getReviews()
				.stream().filter(R -> R.getActivateYn().equals("Y"))
				.map(R ->
						R.toDto(memberRepository.findByEmailFJ(R.getReviewerEmail())
								.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND))
								.getMypage()
								.getProfileImgUrl())
				)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ReviewRes> getAllReportedReviews() {
		return reviewRepo.findAll()
				.stream().filter(R -> R.getReportedYn().equals("Y"))
				.map(R -> R.toDto(
						memberRepository.findByEmailFJ(R.getReviewerEmail())
								.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND))
								.getMypage()
								.getProfileImgUrl())
				)
				.collect(Collectors.toList());
	}

	@Transactional
	public void deActivateReview(Long reviewSeq) {
		reviewRepo.findById(reviewSeq)
				.ifPresentOrElse(
						R -> R.setActivateYn("N")
						, () -> {
							throw new BussinessException(ExMessage.REVIEW_ERROR_NOT_FOUND);
						}
				);
	}

	@Transactional
	public void updateReview(Long reviewSeq, String crdiEmail, String content) {
		memberRepository.findByEmailFJ(crdiEmail)
				.filter(M -> M.getCrdiYn().equals("Y"))
				.ifPresentOrElse(
						M -> M.getReviews()
								.stream()
								.filter(R -> R.getSeq().equals(reviewSeq))
								.findFirst()
								.orElseThrow(() -> new BussinessException(ExMessage.REVIEW_ERROR_NOT_FOUND))
								.setContent(content)
						, () -> {
							throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);
						});
	}

	@Transactional
	public void deActivateReply(Long replySeq) {
		replyRepository.findById(replySeq)
				.ifPresentOrElse(
						R -> R.setActiveYN("N")
						, () -> {
							throw new BussinessException(ExMessage.REPLY_ERROR_NOT_FOUND);
						}
				);
	}

	@Transactional
	public void updateReply(Long replySeq, String content) {
		if (replySeq == null) throw new BussinessException("키 입력이 잘못되었습니다.");

		replyRepository.findById(replySeq)
				.ifPresentOrElse(
						R -> R.setReplyContent(content)
						, () -> {
							throw new BussinessException(ExMessage.REPLY_ERROR_NOT_FOUND);
						}
				);
	}

	@Transactional
	public ReplyRes getReply(Long reviewSeq) {
		Reply reply = replyRepository.findByReviewSeq(reviewSeq)
				.orElseThrow(() -> new BussinessException("해당하는 답글이 없습니다."));
		return reply.toDto();
	}
}
