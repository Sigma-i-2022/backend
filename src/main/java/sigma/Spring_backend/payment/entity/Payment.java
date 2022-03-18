package sigma.Spring_backend.payment.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.payment.dto.OrderNameType;
import sigma.Spring_backend.payment.dto.PayType;
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
	@Enumerated(EnumType.STRING)
	private PayType payType;

	@Column(nullable = false)
	private Long amount;

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

	@Column(nullable = false)
	private String paySuccessYn;

	@Column(nullable = false)
	private String createDate;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Member customer;

	public PaymentRes toDto() {
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
}
