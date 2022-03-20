package sigma.Spring_backend.payment.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.payment.dto.OrderNameType;

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
	private String orderId;

	@Column(nullable = false)
	private String paymentKey;

	@Column(nullable = false)
	private String orderName;

	@Column(nullable = false)
	private String requestedAt;

	@Column(nullable = false)
	private String approvedAt;

	@Column(nullable = false)
	private String cardCompany;

	@Column(nullable = false)
	private String cardNumber;

	@Column(nullable = false)
	private String receiptUrl;

	@Column(nullable = false)
	private String cancelReason;

	@Column(nullable = false)
	private String cancelDate;

	@Column(nullable = false)
	private Long cancelAmount;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Member customer;
}
