package sigma.Spring_backend.memberReport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberReport.dto.MemberReportReq;
import sigma.Spring_backend.memberReport.dto.MemberReportRes;
import sigma.Spring_backend.memberReport.entity.MemberReport;
import sigma.Spring_backend.memberReport.repository.MemberReportRepo;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberReportService {

	private final MemberReportRepo memberReportRepo;
	private final MemberRepository memberRepository;

	@Transactional
	public void reportMember(MemberReportReq memberReportReq) {
		try {
			memberRepository.findBySeqFJ(memberReportReq.getMemberSeq())
					.ifPresentOrElse(
							M -> M.addReport(memberReportReq.toEntity())
							, () -> {
								throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);
							}
					);
		} catch (Exception e) {
			throw new BussinessException(ExMessage.DB_ERROR_SAVE);
		}
	}

	@Transactional
	public List<MemberReportRes> getAllReportedHistoryByMember(Long memberSeq) {
		Member member = memberRepository.findBySeqFJ(memberSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

		return member.getReports()
				.stream()
				.map(MemberReport::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public List<MemberReportRes> getAllReportedHistory() {
		return memberReportRepo.findAll()
				.stream()
				.map(MemberReport::toDto)
				.collect(Collectors.toList());
	}
}
