package sigma.Spring_backend.payment.dto;

import lombok.Data;

@Data
public class PaymentResHandleCancelDto {
	Long cancelAmount;           // 결제를 취소한 금액입니다.
	String cancelReason;            // 결제를 취소한 이유입니다.
	Integer taxFreeAmount;          // 면세 처리된 금액입니다.
	Integer taxAmount;              // 과세 처리된 금액입니다.
	Integer refundableAmount;       // 결제 취소 후 환불 가능한 잔액입니다.
	String canceledAt;              // 결제 취소가 일어난 날짜와 시간 정보입니다.
}									// ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss±hh:mm입니다.
									// (e.g. 2022-01-01T00:00:00+09:00)

