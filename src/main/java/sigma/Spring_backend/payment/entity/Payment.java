package sigma.Spring_backend.payment.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.payment.dto.ORDER_NAME_TYPE;
import sigma.Spring_backend.payment.dto.PAY_TYPE;
import sigma.Spring_backend.payment.dto.PaymentDto;
import sigma.Spring_backend.payment.dto.PaymentRes;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq", nullable = false, unique = true)
	private Long seq;

	@Column(nullable = false, unique = true)
	private Long reservationSeq;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PAY_TYPE payType;

	@Column(nullable = false)
	private Long amount;

	@Setter
	@Column
	private String cardCompany;			// 카드회사

	@Setter
	@Column
	private String cardNumber;			// "949129******7058"

	@Setter
	@Column
	private String cardReceiptUrl;		// 영수증 링크

	@Setter
	@Column
	private String virtualAccountNumber;	// 가상계좌번호

	@Setter
	@Column
	private String virtualBank;				// 가상계좌 입금 은행

	@Setter
	@Column
	private String virtualDueDate;			// 입금기한: 2021-02-05T21:05:09+09:00

	@Setter
	@Column
	private String virtualRefundStatus;		// 환불상태

	@Setter
	@Column
	private String virtualSecret;			// 가상계좌 검증용 시크릿 값

	@Column(nullable = false)
	private String orderId;					// 주문고유번호 uuid

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ORDER_NAME_TYPE orderName;		// 상품명

	@Column(nullable = false)
	private String customerEmail;			// 주문자 이메일

	@Column(nullable = false)
	private String customerName;			// 주문자 성함

	@Setter
	@Column
	private String paymentKey;				// 결제 고유 번호

	@Setter
	@Column(nullable = false)
	private String paySuccessYn;			// 결제 성공 여부

	@Setter
	@Column
	private String payFailReason;			// 결제 실패 이유

	@Column(nullable = false)
	private String createDate;				// 생성시기

	@Setter
	@Column(nullable = false)
	private String cancelYn;				// 결제 취소 여부

	@Setter
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Member customer;

	public PaymentRes toRes() {
		return PaymentRes.builder()
				.reservationSeq(reservationSeq)
				.payType(payType.getName())
				.paySuccessYn(paySuccessYn)
				.amount(amount)
				.orderId(orderId)
				.orderName(orderName.getName())
				.customerEmail(customerEmail)
				.customerName(customerName)
				.createDate(createDate)
				.build();
	}

	public PaymentDto toDto() {
		return PaymentDto.builder()
				.seq(seq)
				.reservationSeq(reservationSeq)
				.payType(payType.getName())
				.amount(amount)
				.cardCompany(cardCompany)
				.cardNumber(cardNumber)
				.cardReceiptUrl(cardReceiptUrl)
				.orderId(orderId)
				.orderName(orderName.getName())
				.customerEmail(customerEmail)
				.customerName(customerName)
				.paymentKey(paymentKey)
				.paySuccessYn(paySuccessYn)
				.cancelYn(cancelYn)
				.payFailReason(payFailReason)
				.createDate(createDate)
				.build();
	}
}
