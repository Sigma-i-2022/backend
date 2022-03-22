package sigma.Spring_backend.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRes {
	private Long reservationSeq;		// 예약번호
	private String payType;				// 지불방법
	private Long amount;				// 지불금액
	private String orderId;				// 주문 고유 ID
	private String orderName;			// 주문 상품 이름
	private String customerEmail;		// 구매자 이메일
	private String customerName;		// 구매자 이름
	private String successUrl;			// 성공시 콜백 주소
	private String failUrl;				// 실패시 콜백 주소
	private String createDate;			// 결제 날짜
	private String paySuccessYn;		// 결제 성공 여부
	private Integer validHours;			// 유효 시간
	private String cashReceiptType;		// 현금영수증 타입
	private Boolean useEscrow;			// 에스크로 사용 여부
}
