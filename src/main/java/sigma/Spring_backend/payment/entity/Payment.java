package sigma.Spring_backend.payment.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.payment.dto.OrderNameType;
import sigma.Spring_backend.payment.dto.PayType;
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
	private PayType payType;

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

	@Column(nullable = false)
	private String orderId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderNameType orderName;

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
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Member customer;

	public PaymentRes toRes() {
		return PaymentRes.builder()
				.payType(payType.getName())
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
				.payFailReason(payFailReason)
				.createDate(createDate)
				.build();
	}
}
