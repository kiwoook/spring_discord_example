package com.example.spring_discordexample.join.listener;

import com.example.spring_discordexample.join.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

@Slf4j
@RequiredArgsConstructor
public class JoinListener extends ListenerAdapter {

    private final JoinService joinService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("join")) {
            String discordId = event.getUser().getId();

            if (joinService.alreadyExist(discordId)){
                event.reply("이미 처리된 사용자입니다.").setEphemeral(true).queue();
                return;
            }


            TextInput nameInput = TextInput.create("name", "이름", TextInputStyle.SHORT)
                    .setPlaceholder("이름을 입력해주세요.")
                    .setRequired(true)
                    .setMinLength(3)
                    .setMaxLength(20)
                    .build();

            TextInput birthInput = TextInput.create("birth", "생년월일", TextInputStyle.SHORT)
                    .setPlaceholder("YYYYMMDD 형식으로 작성해주세요.")
                    .setRequired(true)
                    .setMaxLength(8)
                    .setMaxLength(8)
                    .build();

            TextInput phoneInput = TextInput.create("phone", "전화번호", TextInputStyle.SHORT)
                    .setPlaceholder("휴대폰 번호 11자리를 입력해주세요.")
                    .setRequired(true)
                    .setMinLength(11)
                    .setMaxLength(11)
                    .build();

            TextInput steamIdInput = TextInput.create("steam", "스팀 아이디", TextInputStyle.SHORT)
                    .setPlaceholder("스팀 아이디를 입력해주세요.")
                    .setRequired(true)
                    .setMinLength(3)
                    .setMaxLength(20)
                    .build();

            TextInput bgIdInput = TextInput.create("battleground", "배틀그라운드 아이디", TextInputStyle.SHORT)
                    .setPlaceholder("배틀그라운드 아이디를 입력해주세요.")
                    .setRequired(true)
                    .setMinLength(2)
                    .setMaxLength(30)
                    .build();


            Modal modal = Modal.create("joinModal", "참가신청서")
                    .addComponents(ActionRow.of(nameInput),
                            ActionRow.of(birthInput),
                            ActionRow.of(phoneInput),
                            ActionRow.of(steamIdInput),
                            ActionRow.of(bgIdInput)
                    ).build();

            event.replyModal(modal).queue();
        }
    }

}
