package sigma.Spring_backend.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.reservation.dto.CancelReasonDto;
import sigma.Spring_backend.reservation.entity.CancelReason;
import sigma.Spring_backend.reservation.repository.CancelReasonRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CancelReasonService {

	private final CancelReasonRepo cancelReasonRepo;

	@Transactional(readOnly = true)
	public List<CancelReasonDto> getAllReason(String condition) {
		if (condition.equals("ALL")) {
			return cancelReasonRepo.findAll()
					.stream().map(CancelReason::toDto)
					.collect(Collectors.toList());
		} else if (condition.equals("CLIENT") || condition.equals("CRDI")){
			return cancelReasonRepo.findAll()
					.stream()
					.filter(R -> R.getByWho().equals(condition))
					.map(CancelReason::toDto)
					.collect(Collectors.toList());
		}
		throw new BussinessException("검색 조건이 잘못되었습니다.");
	}
}
