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

	@Column(nullable = false)
	private Long reservationSeq;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PAY_TYPE payType;

	@Column(nullable = false)
	private Long amount;

	@Setter
	@Column
	private String cardCompany;

	@Setter
	@Column
	private String cardNumber;			// "949129******7058"

	@Setter
	@Column
	private String cardReceiptUrl;

	@Setter
	@Column
	private String virtualAccountNumber;

	@Setter
	@Column
	private String virtualBank;

	@Setter
	@Column
	private String virtualDueDate;		// 입금기한: 2021-02-05T21:05:09+09:00

	@Setter
	@Column
	private String virtualRefundStatus;

	@Setter
	@Column
	private String virtualSecret;

	@Column(nullable = false)
	private String orderId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ORDER_NAME_TYPE orderName;

	@Column(nullable = false)
	private String customerEmail;

	@Column(nullable = false)
	private String customerName;

	@Setter
	@Column
	private String paymentKey;

	@Setter
	@Column(nullable = false)
	private String paySuccessYn;

	@Setter
	@Column
	private String payFailReason;

	@Column(nullable = false)
	private String createDate;

	@Setter
	@Column
	private String cancelYn;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Member customer;

	public PaymentRes toRes() {
		return PaymentRes.builder()
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
