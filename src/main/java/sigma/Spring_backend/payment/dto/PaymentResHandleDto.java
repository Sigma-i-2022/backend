package sigma.Spring_backend.payment.dto;

import lombok.Data;
import sigma.Spring_backend.payment.entity.CancelPayment;

@Data
public class PaymentResHandleDto {
	String mId;                     // : "tosspayments", 가맹점 ID
	String version;                 // : "1.3", Payment 객체 응답 버전
	String paymentKey;              // : "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6",
	String orderId;                 // : "IBboL1BJjaYHW6FA4nRjm",
	String orderName;               // : "토스 티셔츠 외 2건",
	String currency;                // : "KRW",
	String method;                  // : "카드", 결제수단
	String totalAmount;             // : 15000,
	String balanceAmount;           // : 15000,
	String suppliedAmount;          // : 13636,
	String vat;                     // : 1364,
	String status;                  // : "DONE", 결제 처리 상태
	String requestedAt;             // : "2021-01-01T10:01:30+09:00",
	String approvedAt;              // : "2021-01-01T10:05:40+09:00",
	String useEscrow;               // : false,
	String cultureExpense;          // : false,
	PaymentResHandleCardDto card;	// : 카드 결제,
	PaymentResHandleCancelDto[] cancels;	// : 결제 취소 이력 관련 객체
	String type;                    // : "NORMAL",	결제 타입 정보 (NOMAL, BILLING, CONNECTPAY)
	PaymentResHandleVirtualDto virtualAccount;          // : 가상 계좌 결제 시 관련 객체
	String secret;                  // : null,		가상 계좌로 결제 시 입금 콜백 검증 값
/*
	String transfer;                // : null,		계좌이체 결제 시 관련 객체
	String mobilePhone;             // : null,		휴대폰 결제 시 관련 객체
	String giftCertificate;         // : null,		상품권 결제 시 관련 객체
	String cashReceipt;             // : null,		현금 영수증 관련 객체
	String discount;                // : null,		카드사 할인 정보
	String easyPay;                 // : null,		간편결제 결제시 정보
	String taxFreeAmount;			// : 0			면세금액
*/

	public CancelPayment toCancelPaymentByCard() {
		return CancelPayment.builder()
				.orderId(orderId)
				.orderName(orderName)
				.paymentKey(paymentKey)
				.requestedAt(requestedAt)
				.approvedAt(approvedAt)
				.cancelAmount(cancels[0].getCancelAmount())
				.cancelDate(cancels[0].getCanceledAt())
				.cancelReason(cancels[0].getCancelReason())
				.cardCompany(card.getCompany())					// 카드 취소의 경우에만 들어감
				.cardNumber(card.getNumber())					// 카드 취소의 경우에만 들어감
				.cardReceiptUrl(card.getReceiptUrl())				// 카드 취소의 경우에만 들어감
				.build();
	}

	public CancelPayment toCancelPaymentByVirtual(String refundBank, String refundAccount) {
		return CancelPayment.builder()
				.orderId(orderId)
				.orderName(orderName)
				.paymentKey(paymentKey)
				.requestedAt(requestedAt)
				.cancelAmount(cancels[0].getCancelAmount())
				.cancelDate(cancels[0].getCanceledAt())
				.cancelReason(cancels[0].getCancelReason())
				.refundBank(refundBank)							// 가상계좌 결제시 고객 환불 계좌 은행
				.refundAccount(refundAccount)					// 가상계좌 결제시 고객 환불 계좌 번호
				.build();
	}
}