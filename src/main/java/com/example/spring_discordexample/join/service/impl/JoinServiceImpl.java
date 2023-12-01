package com.example.spring_discordexample.join.service.impl;


import com.example.spring_discordexample.join.dto.JoinRequestDto;
import com.example.spring_discordexample.join.dto.JoinResponseDto;
import com.example.spring_discordexample.join.entity.JoinInfo;
import com.example.spring_discordexample.join.repository.JoinRepository;
import com.example.spring_discordexample.join.service.JoinService;
import jakarta.persistence.EntityNotFoundException;
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
        log.info("JOIN SAVE");

        JoinInfo joinInfo = JoinRequestDto.toEntity(requestDto);
        return new JoinResponseDto(joinRepository.save(joinInfo));
    }

    @Override
    public JoinResponseDto read(String discordId) {

        JoinInfo joinInfo = joinRepository.findByDiscordId(discordId).orElseThrow(EntityNotFoundException::new);

        return new JoinResponseDto(joinInfo);
    }

    @Override
    @Transactional
    public void delete(String discordId) {

        log.info("DELETE discord_id = {}" , discordId);
        try{
            joinRepository.deleteByDiscordId(discordId);
        }catch (Exception e){
            throw new EntityNotFoundException(e);
        }
    }

    @Override
    public boolean alreadyExist(String discordId) {
        return joinRepository.existsByDiscordId(discordId);
    }

}
