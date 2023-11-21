package com.example.spring_discordexample.join.listener;

import com.example.spring_discordexample.join.dto.JoinRequestDto;
import com.example.spring_discordexample.join.dto.JoinResponseDto;
import com.example.spring_discordexample.join.enums.Tier;
import com.example.spring_discordexample.join.service.JoinService;
import com.example.spring_discordexample.utill.DateUtils;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JoinModalListener extends ListenerAdapter {

    private final JoinService joinService;

    private final JoinRequestDto joinRequestDto;

    public JoinModalListener(JoinService joinService) {
        this.joinService = joinService;
        this.joinRequestDto = new JoinRequestDto();
    }

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().equals("joinModal")) {
            // TODO 값을 받고 검증하는 로직 구현

            log.info("===Modal Listener Working===");
            joinRequestDto.setName(Objects.requireNonNull(event.getValue("name")).getAsString());
            joinRequestDto.setBirth(DateUtils.stringToLocalDate(Objects.requireNonNull(event.getValue("birth")).getAsString()));
            joinRequestDto.setPhone(Objects.requireNonNull(event.getValue("phone")).getAsString());
            joinRequestDto.setSteamId(Objects.requireNonNull(event.getValue("steam")).getAsString());
            joinRequestDto.setBattleGroundId(Objects.requireNonNull(event.getValue("battleground")).getAsString());

            event.reply("랭크를 입력해주세요.")
                    .addActionRow(
                            StringSelectMenu.create("mode")
                                    .addOption("솔로", "솔로")
                                    .addOption("듀오", "듀오")
                                    .addOption("스쿼드", "스쿼드")
                                    .build()
                    )
                    .addActionRow(
                            StringSelectMenu.create("tier")
                                    .addOption("브론즈", Tier.BRONZE.name())
                                    .addOption("실버", Tier.SILVER.name())
                                    .addOption("골드", Tier.GOLD.name())
                                    .addOption("플래티넘", Tier.PLATINUM.name())
                                    .addOption("마스터", Tier.MASTER.name())
                                    .build()
                    ).addActionRow(
                            StringSelectMenu.create("streamer")
                                    .addOption("1", "1")
                                    .addOption("2", "2")
                                    .addOption("3", "3")
                                    .build()
                    )
                    .addActionRow(
                            Button.primary("check", "확인")
                    ).queue();


        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("mode")) {
            joinRequestDto.setMode(event.getValues().get(0));
            event.reply(event.getValues().get(0) + "모드가 선택되었습니다.").setEphemeral(true).queue();
        }

        if (event.getComponentId().equals("tier")) {
            joinRequestDto.setTier(Tier.valueOf(event.getValues().get(0)));
            event.reply(event.getValues().get(0) + "티어가 선택되었습니다.").setEphemeral(true).queue();
        }

        if (event.getComponentId().equals("streamer")){
            joinRequestDto.setFavoriteStreamer(event.getValues().get(0));
            event.reply(event.getValues().get(0) + "을 선택하였습니다.").setEphemeral(true).queue();
        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("check")) {
            String discordId = event.getUser().getId();
            joinRequestDto.setDiscordId(discordId);
            EmbedBuilder eb = getEmbedBuilder();

            MessageEmbed embed = eb.build();

            event.deferReply()
                    .setEmbeds(embed)
                    .addActionRow(
                            Button.primary("send", "신청하기")
                    )
                    .queue();
        }

        if (event.getComponentId().equals("send")) {
            joinService.save(joinRequestDto);
            event.reply("전송되었습니다.").queue();
        }
    }

    @NotNull
    private EmbedBuilder getEmbedBuilder() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(221, 8, 23));
        eb.setAuthor("재확인");
        eb.addField("이름", joinRequestDto.getName(), true);
        eb.addField("생년월일", String.valueOf(joinRequestDto.getBirth()), true);
        eb.addField("전화번호", joinRequestDto.getPhone(), true);
        eb.addField("스팀 아이디", joinRequestDto.getSteamId(), true);
        eb.addField("배틀그라운드 아이디", joinRequestDto.getBattleGroundId(), true);
        eb.addField("디스코드 아이디", joinRequestDto.getDiscordId(), true);
        eb.addField("게임 모드", joinRequestDto.getMode(), true);
        eb.addField("티어", String.valueOf(joinRequestDto.getTier()), true);
        eb.addField("선호 스트리머", joinRequestDto.getFavoriteStreamer(), true);
        return eb;
    }
}