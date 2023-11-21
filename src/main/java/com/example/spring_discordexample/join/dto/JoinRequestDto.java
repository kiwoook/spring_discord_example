package com.example.spring_discordexample.join.dto;

import com.example.spring_discordexample.join.entity.JoinInfo;
import com.example.spring_discordexample.join.enums.Tier;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDto {

    private String name;
    private LocalDate birth;
    private String phone;
    private String steamId;
    private String battleGroundId;
    private String discordId;
    private String mode;
    private Tier tier;
    private String favoriteStreamer;


    public static JoinInfo toEntity(JoinRequestDto requestDto){
        return JoinInfo.builder()
                .name(requestDto.getName())
                .birth(requestDto.getBirth())
                .phone(requestDto.getPhone())
                .steamId(requestDto.getSteamId())
                .battleGroundId(requestDto.getBattleGroundId())
                .discordId(requestDto.getDiscordId())
                .mode(requestDto.getMode())
                .tier(requestDto.getTier())
                .favoriteStreamer(requestDto.getFavoriteStreamer())
                .build();
    }

    public JoinRequestDto(String name, LocalDate birth, String phone, String steamId, String battleGroundId) {
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.steamId = steamId;
        this.battleGroundId = battleGroundId;
    }
}
