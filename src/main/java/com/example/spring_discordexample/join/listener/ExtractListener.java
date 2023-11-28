package com.example.spring_discordexample.join.listener;

import com.example.spring_discordexample.join.service.ExactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
@RequiredArgsConstructor
public class ExtractListener extends ListenerAdapter {

    private final ExactService exactService;


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("extract")) {
            Member member = event.getMember();
            if (member != null && member.hasPermission(Permission.ADMINISTRATOR)) {
                // DB와 엑셀을 사용하게 되므로 동시성 문제가 발생할 수 있다.
                exactService.exact();

            } else {
                event.reply("관리자 권한이 필요합니다.").setEphemeral(true).queue();
            }
        }
    }
}
