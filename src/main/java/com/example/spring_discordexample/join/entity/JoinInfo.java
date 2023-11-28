package com.example.spring_discordexample.join.entity;


import com.example.spring_discordexample.join.enums.Tier;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate birth;

    @Column(unique = true)
    private String phone;

    @Column(name="steam_id")
    private String steamId;

    @Column(name = "battleground_id")
    private String battleGroundId;

    @Column(name = "discord_id", unique = true)
    private String discordId;

    private String mode;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Column(name = "favorite_streamer")
    private String favoriteStreamer;


    @Builder
    public JoinInfo(String name, LocalDate birth, String phone, String steamId, String battleGroundId, String discordId, String mode, Tier tier, String favoriteStreamer) {
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.steamId = steamId;
        this.battleGroundId = battleGroundId;
        this.discordId = discordId;
        this.mode = mode;
        this.tier = tier;
        this.favoriteStreamer = favoriteStreamer;
    }
}
