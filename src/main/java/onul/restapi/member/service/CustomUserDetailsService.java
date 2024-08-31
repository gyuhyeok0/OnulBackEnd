package onul.restapi.member.service;

import onul.restapi.member.dto.MemberDTO;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomUserDetailsService(MemberRepository memberRepository, ModelMapper modelMapper){
        this.memberRepository=memberRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        System.out.println("member 아디디 " + memberId);

        Members member = memberRepository.findByMemberId(memberId);

        System.out.println(member);
        MemberDTO memberDTO = modelMapper.map(member,MemberDTO.class);

        System.out.println(memberDTO);


        return memberDTO;
    }
}
