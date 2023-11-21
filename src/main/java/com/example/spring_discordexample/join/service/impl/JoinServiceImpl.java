package com.example.spring_discordexample.join.service.impl;


import com.example.spring_discordexample.join.dto.JoinRequestDto;
import com.example.spring_discordexample.join.dto.JoinResponseDto;
import com.example.spring_discordexample.join.entity.JoinInfo;
import com.example.spring_discordexample.join.repository.JoinRepository;
import com.example.spring_discordexample.join.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService {

    private final JoinRepository joinRepository;

    @Override
    @Transactional
    public JoinResponseDto save(JoinRequestDto requestDto) {
        // TODO DB에 저장하는 로직을 구현한다.
        log.info("JOIN SAVE");

        JoinInfo joinInfo = JoinRequestDto.toEntity(requestDto);
        return new JoinResponseDto(joinRepository.save(joinInfo));
    }

    @Override
    public JoinResponseDto read(String discordId) {
        // TODO 특정 사용자에 데이터가 있는지 식별한다.
        return null;
    }

    @Override
    @Transactional
    public JoinResponseDto delete(String discordId) {
        // TODO 특정 사용자에 데이터를 삭제한다.
        return null;
    }

    @Override
    public Boolean alreadyExist(String discordId) {
        return joinRepository.existsByDiscordId(discordId);
    }

}
