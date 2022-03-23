package sigma.Spring_backend.payment.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.payment.dto.CancelPaymentRes;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq", nullable = false)
	private Long seq;

	@Column(nullable = false)
	private String orderId;					// 우리 결제 고유 번호

	@Column(nullable = false)
	private String paymentKey;				// 토스 결제 고유 번호

	@Column(nullable = false)
	private String orderName;				// 상품명

	@Column(nullable = false)
	private String requestedAt;

	@Column
	private String approvedAt;

	@Setter
	@Column
	private String refundBank;				// 가상계좌 취소 - 고객 환불 은행

	@Setter
	@Column
	private String refundAccount;			// 가상계좌 취소 - 고객 환불 계좌

	@Column
	private String cardCompany;				// 카드결제 취소 - 고객 결제취소 카드회사

	@Column
	private String cardNumber;				// 카드결제 취소 - 고객 결제취소 카드번호

	@Column
	private String cardReceiptUrl;			// 카드결제 취소 - 고객 결제취소 영수증

	@Column(nullable = false)
	private String cancelReason;			// 결제 취소 이유

	@Column(nullable = false)
	private String cancelDate;				// 결제 취소 날짜

	@Column(nullable = false)
	private Long cancelAmount;				// 결제 취소 금액

	@Setter
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "CUSTOMER_SEQ")
	private Member customer;

	public CancelPaymentRes toDto() {
		return CancelPaymentRes.builder()
				.seq(seq)
				.orderId(orderId)
				.paymentKey(paymentKey)
				.orderName(orderName)
				.requestedAt(requestedAt)
				.approvedAt(approvedAt)
				.cardCompany(cardCompany)
				.cardNumber(cardNumber)
				.receiptUrl(cardReceiptUrl)
				.cancelReason(cancelReason)
				.cancelDate(cancelDate)
				.cancelAmount(cancelAmount)
				.build();
	}
}
