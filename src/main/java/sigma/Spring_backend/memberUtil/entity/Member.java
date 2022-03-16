package sigma.Spring_backend.memberUtil.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import sigma.Spring_backend.chat.entity.MemberChatRoomConnection;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;
import sigma.Spring_backend.clientLook.entity.ClientLookPage;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;
import sigma.Spring_backend.memberReport.entity.MemberReport;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;
import sigma.Spring_backend.reservation.entity.MemberReservation;
import sigma.Spring_backend.review.entity.Review;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@Column(unique = true, nullable = false)
	private String userId;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column
	private String signupType;

	@Column
	private String registDate;

	@Column
	private String updateDate;

	@Column
	private String deviceToken;

	@Setter
	@Column
	private String activateYn;

	@Setter
	@Column
	private String reportedYn;

	@Setter
	@Column
	private String crdiYn;

	@OneToOne
	@JoinColumn(name = "MYPAGE_SEQ", nullable = false)
	private CommonMypage mypage;

	public void setMypage(CommonMypage mypage) {
		this.mypage = mypage;
		mypage.setMember(this);
	}

	@OneToOne
	@JoinColumn(name = "AUTHORIZE_USER_SEQ", nullable = false)
	private AuthorizeMember authorizeUser;

	public void setAuthorizeUser(AuthorizeMember authorizeUser) {
		this.authorizeUser = authorizeUser;
		authorizeUser.setMember(this);
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Builder.Default
	private List<ClientLookPage> pages = new ArrayList<>();

	public void addLookPage(ClientLookPage clientLookPage) {
		pages.add(clientLookPage);
		clientLookPage.setMember(this);
	}

	public void removeLookPage(ClientLookPage clientLookPage) {
		clientLookPage.setActivateYn("N");
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<CrdiWork> work = new ArrayList<>();

	public void addWork(CrdiWork crdiWork) {
		work.add(crdiWork);
		crdiWork.setMember(this);
	}

	public void removeWork(CrdiWork crdiWork) {
		work.remove(crdiWork);
		crdiWork.setMember(null);
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	@Builder.Default
	private List<MemberChatRoomConnection> MemberChatRoomConnections = new ArrayList<>();

	public void enterChatRoom(MemberChatRoomConnection memberChatRoomConnection) {
		MemberChatRoomConnections.add(memberChatRoomConnection);
		memberChatRoomConnection.setMember(this);
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Builder.Default
	private List<MemberReservation> memberReservations = new ArrayList<>();

	public void registReservation(MemberReservation memberReservation) {
		this.memberReservations.add(memberReservation);
		memberReservation.setMember(this);
	}

	@OneToMany(mappedBy = "coordinator", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Review> reviews = new ArrayList<>();

	public void addReview(Review review) {
		this.reviews.add(review);
		review.setCoordinator(this);
	}

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@Builder.Default
	private List<MemberReport> reports = new ArrayList<>();

	public void addReport(MemberReport report) {
		this.reports.add(report);
		this.setReportedYn("Y");
		report.setMember(this);
	}

	public MemberResponseDto toDto() {
		return MemberResponseDto.builder()
				.userSeq(seq)
				.userId(userId)
				.email(email)
				.password(password)
				.signupType(signupType)
				.activateYn(activateYn)
				.crdiYn(crdiYn)
				.registDate(registDate)
				.updateDate(updateDate)
				.build();
	}
}
