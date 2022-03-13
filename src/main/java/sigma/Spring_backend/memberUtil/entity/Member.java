package sigma.Spring_backend.memberUtil.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import sigma.Spring_backend.chat.entity.MemberChatRoomConnection;
import sigma.Spring_backend.crdiPage.entity.CrdiMypage;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;
import sigma.Spring_backend.memberLook.entity.MemberLookPage;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberReport.entity.MemberReport;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;
import sigma.Spring_backend.memberSignup.entity.JoinCrdi;
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

	@Setter
	@Column
	private String activateYn;

	@Setter
	@Column
	private String reportedYn;

	@Setter
	@Column
	private String crdiYn;

	@Setter
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MYPAGE_SEQ")
	private MemberMypage mypage;

	public void removeMyPage() {
		this.mypage.setEmail("");
		this.mypage = null;
	}

	@Setter
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "AUTHORIZE_USER_SEQ")
	private AuthorizeMember authorizeUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CRDIMYPAGE_SEQ")
    private CrdiMypage crdiMypage;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "seq", referencedColumnName = "JOIN_SEQ")
	private JoinCrdi joinCrdi;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Builder.Default
	private List<MemberLookPage> pages = new ArrayList<>();

	public void addLookPage(MemberLookPage memberLookPage) {
		pages.add(memberLookPage);
		memberLookPage.setMember(this);
	}

	public void removeLookPage(MemberLookPage memberLookPage) {
		memberLookPage.setActivateYn("N");
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
