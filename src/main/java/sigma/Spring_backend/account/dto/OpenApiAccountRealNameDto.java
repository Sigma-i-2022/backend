package sigma.Spring_backend.account.dto;

import lombok.Data;

@Data
public class OpenApiAccountRealNameDto {
	String api_tran_id;							// 2ffd133a-d17a-431d-a6a5",
	String api_tran_dtm;						// 20190910101921567",
	String rsp_code;							// A0000",
	String rsp_message;							// ",
	String bank_tran_id;						// F123456789U4BC34239Z",
	String bank_tran_date;						// 20190910",
	String bank_code_tran;						// 097",
	String bank_rsp_code;						// 000",
	String bank_rsp_message;					// ",
	String bank_code_std;						// 097",
	String bank_code_sub;						// 1230001",
	String bank_name;							// 오픈은행",
	String account_num;							// 1101230000678",
	String account_holder_info_type;			//  ",
	String account_holder_info;					// 880101",
	String account_holder_name;					// 홍길동",
	String account_type;						// 1"
}