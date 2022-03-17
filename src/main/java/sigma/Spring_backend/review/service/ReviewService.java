package sigma.Spring_backend.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;
import sigma.Spring_backend.reservation.repository.ReservationRepo;
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

		try {
			coordinator.addReview(reviewReq.toEntity());
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
							}else if (R.getReviewedYn().equals("Y")) {
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

		reply.setReview(review);
		reply.setCrdiEmail(crdiEmail);
		reply.setReplyContent(replyContent);
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
				.map(Review::toDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ReviewRes> getAllReportedReviews() {
		return reviewRepo.findAll()
				.stream().filter(R -> R.getReportedYn().equals("Y"))
				.map(Review::toDto)
				.collect(Collectors.toList());
	}
}
