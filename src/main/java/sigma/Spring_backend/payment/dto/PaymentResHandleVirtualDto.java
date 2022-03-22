package sigma.Spring_backend.payment.dto;

import lombok.Data;

@Data
public class PaymentResHandleVirtualDto {
	String accountNumber;				// X6505636518308",
	String accountType;					// 일반",
	String bank;						// 우리",
	String customerName;				// 박토스",
	String dueDate;						// 2021-02-05T21:05:09+09:00",
	String expired;						//
	String settlementStatus;			// INCOMPLETED",
	String refundStatus;				// NONE"
}