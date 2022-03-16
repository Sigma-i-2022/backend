package sigma.Spring_backend.memberMypage.service;

import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.memberMypage.dto.*;

public interface CommonMypageServiceInterface {
	/*
	회원 마이페이지 조회
	 */
	ClientMypageRes getClientMypage(String email);

	/*
	회원 마이페이지 소개란 수정
	 */
	void updateClientMypageInfo(CommonUpdateInfoReq commonUpdateInfoReq);

	/*
	코디네이터 마이페이지 조회
	 */
	CrdiMypageRes getCrdiMypage(String email);

	/*
	코디네이터 마이페이지 등록
	 */
	void registCrdiMypage(CrdiMypageReq crdiProfileReq, MultipartFile profileImg);

	/*
	코디네이터 마이페이지 소개란 수정
	 */
	void updateCrdiMypageInfo(CommonUpdateInfoReq crdiProfileReq);

	/*
	마이페이지 프로필 이미지 수정
	 */
	void updateProfileImg(String email, MultipartFile imageFile);
}
