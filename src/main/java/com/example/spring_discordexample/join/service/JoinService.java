package com.example.spring_discordexample.join.service;

import com.example.spring_discordexample.join.dto.JoinRequestDto;
import com.example.spring_discordexample.join.dto.JoinResponseDto;

public interface JoinService {

    // 저장
    JoinResponseDto save(JoinRequestDto requestDto);

    // 읽기
    JoinResponseDto read(String discordId);

    // 삭제
    void delete(String discordId);

    boolean alreadyExist(String discordId);


}
