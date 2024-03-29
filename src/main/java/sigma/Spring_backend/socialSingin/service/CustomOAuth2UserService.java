package sigma.Spring_backend.socialSingin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;
import sigma.Spring_backend.memberMypage.repository.CommonMypageRepository;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;
import sigma.Spring_backend.memberSignup.repository.AuthorizeCodeRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;
import sigma.Spring_backend.socialSingin.domain.User;
import sigma.Spring_backend.socialSingin.repository.UserRepository;
import sigma.Spring_backend.socialSingin.dto.OAuthAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommonMypageRepository commonMypageRepository;

    @Autowired
    private AuthorizeCodeRepository authorizeCodeRepository;

    @Autowired
    private HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        // 현재 진행중인 서비스를 구분하기 위해 문자열로 받음. oAuth2UserRequest.getClientRegistration().getRegistrationId()에 값이 들어있다. {registrationId='naver'} 이런식으로
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 시 키 값이 된다. 구글은 키 값이 "sub"이고, 네이버는 "response"이고, 카카오는 "id"이다. 각각 다르므로 이렇게 따로 변수로 받아서 넣어줘야함.
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2 로그인을 통해 가져온 OAuth2User의 attribute를 담아주는 of 메소드.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        if (memberRepository.findByEmailFJ(attributes.getEmail()).isEmpty()) {
            this.saveOrUpdate(attributes);
        }
        //httpSession.setAttribute("user", new SessionUser(user));

        System.out.println(attributes.getAttributes());
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
                , attributes.getAttributes()
                , attributes.getNameAttributeKey());
    }

    private void saveOrUpdate(OAuthAttributes attributes) {
        String userId  = attributes.getEmail().substring(0,attributes.getEmail().indexOf("@"));

            Member member = Member.builder()
                    .email(attributes.getEmail())
                    .userId(userId)
                    .password("")
                    .signupType("S")
                    .registDate(new DateConfig().getNowDate())
                    .updateDate(new DateConfig().getNowDate())
                    .activateYn("Y")
                    .reportedYn("N")
                    .crdiYn("N")
                    .build();

            CommonMypage commonMypage = commonMypageRepository.save(CommonMypage.builder()
                    .email(attributes.getEmail())
                    .intro("")
                    .userId(userId)
                    .profileImgUrl("")
                    .build());

            AuthorizeMember authorize = authorizeCodeRepository.save(AuthorizeMember.builder()
                    .email(attributes.getEmail())
                    .code("000000")
                    .expired(true)
                    .build());


            member.setMypage(commonMypage);
            member.setAuthorizeUser(authorize);

            memberRepository.save(member);
        }

    }
