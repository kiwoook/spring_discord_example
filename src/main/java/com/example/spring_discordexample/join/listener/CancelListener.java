package com.example.spring_discordexample.join.listener;

import com.example.spring_discordexample.join.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class CancelListener extends ListenerAdapter {

    private final JoinService joinService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("cancel")) {
            String discordId = event.getUser().getId();

            if (joinService.alreadyExist(discordId)) {
                event.reply("정말 삭제하시겠습니까?")
                        .addActionRow(
                                Button.danger("delete", "삭제")
                        ).setEphemeral(true)
                        .queue();
            } else {
                event.reply("신청되지 않은 회원입니다.").queue();
            }

        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("delete")) {

            try {
                String discordId = event.getUser().getId();
                joinService.delete(discordId);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
                event.reply("에러가 발생했습니다.").setEphemeral(true).queue();
                return;
            }
            event.reply("삭제되었습니다.")
                    .setEphemeral(true)
                    .queue();
        }
    }
}
