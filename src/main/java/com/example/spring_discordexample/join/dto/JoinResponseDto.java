package com.example.spring_discordexample.join.dto;

import com.example.spring_discordexample.join.entity.JoinInfo;
import com.example.spring_discordexample.join.enums.Tier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinResponseDto {

    private String name;
    private LocalDate birth;
    private String phone;
    private String steamId;
    private String battleGroundId;
    private String discordId;
    private String mode;
    private Tier tier;
    private String favoriteStreamer;

    public JoinResponseDto(JoinInfo joinInfo) {
        this.name = joinInfo.getName();
        this.birth = joinInfo.getBirth();
        this.phone = joinInfo.getPhone();
        this.steamId = joinInfo.getSteamId();
        this.battleGroundId = joinInfo.getBattleGroundId();
        this.discordId = joinInfo.getDiscordId();
        this.mode = joinInfo.getMode();
        this.tier = joinInfo.getTier();
        this.favoriteStreamer = joinInfo.getFavoriteStreamer();
    }
}
