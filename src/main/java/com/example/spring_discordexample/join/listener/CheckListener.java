package com.example.spring_discordexample.join.listener;

import com.example.spring_discordexample.join.dto.JoinResponseDto;
import com.example.spring_discordexample.join.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@Slf4j
@RequiredArgsConstructor
public class CheckListener extends ListenerAdapter {

    private final JoinService joinService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("check")) {
            String discordId = event.getUser().getId();
            if (joinService.alreadyExist(discordId)) {
                JoinResponseDto joinResponseDto = joinService.read(discordId);
                MessageEmbed embed = getEmbedBuilder(discordId, joinResponseDto).build();

                event.deferReply()
                        .setEmbeds(embed)
                        .setEphemeral(true)
                        .queue();

            } else {
                event.reply("신청하지 않은 회원입니다.")
                        .setEphemeral(true)
                        .queue();
            }
        }
    }


    @NotNull
    private EmbedBuilder getEmbedBuilder(String discordName, JoinResponseDto joinResponseDto) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(221, 8, 23));
        eb.setAuthor("확인");
        eb.addField("이름", joinResponseDto.getName(), true);
        eb.addField("생년월일", String.valueOf(joinResponseDto.getBirth()), true);
        eb.addField("전화번호", joinResponseDto.getPhone(), true);
        eb.addField("스팀 아이디", joinResponseDto.getSteamId(), true);
        eb.addField("배틀그라운드 아이디", joinResponseDto.getBattleGroundId(), true);
        eb.addField("디스코드 닉네임", discordName, true);
        eb.addField("게임 모드", joinResponseDto.getMode(), true);
        eb.addField("티어", String.valueOf(joinResponseDto.getTier()), true);
        eb.addField("선호 스트리머", joinResponseDto.getFavoriteStreamer(), true);
        return eb;
    }
}
