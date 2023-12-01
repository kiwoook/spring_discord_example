package com.example.spring_discordexample.join.listener;

import com.example.spring_discordexample.join.dto.JoinRequestDto;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Pattern;

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

            String phoneRegex = "010\\\\d{7,8}";

            String birth = Objects.requireNonNull(event.getValue("birth")).getAsString();
            String phone = Objects.requireNonNull(event.getValue("phone")).getAsString();

            LocalDate localDate;
            try {
                localDate = DateUtils.stringToLocalDate(birth);
            } catch (DateTimeParseException e) {
                event.reply("잘못된 생년월일입니다. 다시 입력해주세요.").setEphemeral(true).queue();
                return;
            }

            try {
                Pattern.compile(phoneRegex);
            } catch (Exception e) {
                event.reply("잘못된 전화번호입니다. 다시 입력해주세요.").setEphemeral(true).queue();
                return;
            }

            log.info("===Modal Listener Working===");
            joinRequestDto.setName(Objects.requireNonNull(event.getValue("name")).getAsString());
            joinRequestDto.setBirth(localDate);
            joinRequestDto.setPhone(phone);
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
                    )
                    .setEphemeral(true)
                    .queue();


        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("mode")) {
            joinRequestDto.setMode(event.getValues().get(0));
            event.reply("모드가 선택되었습니다.").setEphemeral(true).queue();
        }

        if (event.getComponentId().equals("tier")) {
            joinRequestDto.setTier(Tier.valueOf(event.getValues().get(0)));
            event.reply("티어가 선택되었습니다.").setEphemeral(true).queue();
        }

        if (event.getComponentId().equals("streamer")) {
            joinRequestDto.setFavoriteStreamer(event.getValues().get(0));
            event.reply("스트리머가 선택되었습니다.").setEphemeral(true).queue();
        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("check")) {

            if (joinRequestDto.getMode() == null) {
                event.reply("모드를 선택해주세요.").setEphemeral(true).queue();
                return;
            } else if (joinRequestDto.getTier() == null) {
                event.reply("티어를 선택해주세요.").setEphemeral(true).queue();
                return;
            } else if (joinRequestDto.getFavoriteStreamer() == null) {
                event.reply("신청할 스트리머를 선택해주세요.").setEphemeral(true).queue();
                return;
            } else {

                String discordId = event.getUser().getId();
                String discordName = event.getUser().getEffectiveName();
                joinRequestDto.setDiscordId(discordId);
                EmbedBuilder eb = getEmbedBuilder(discordName);

                MessageEmbed embed = eb.build();

                event.deferReply()
                        .setEmbeds(embed)
                        .addActionRow(
                                Button.primary("send", "신청하기")
                        )
                        .setEphemeral(true).queue();
            }
        }

        if (event.getComponentId().equals("send")) {
            if (joinService.alreadyExist(joinRequestDto.getDiscordId())) {
                event.reply("이미 신청된 아이디입니다.").setEphemeral(true).queue();
                return;
            }
            joinService.save(joinRequestDto);
            event.reply("신청이 완료되었습니다.").setEphemeral(true).queue();

        }
    }

    @NotNull
    private EmbedBuilder getEmbedBuilder(String discordName) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(221, 8, 23));
        eb.setAuthor("재확인");
        eb.addField("이름", joinRequestDto.getName(), true);
        eb.addField("생년월일", String.valueOf(joinRequestDto.getBirth()), true);
        eb.addField("전화번호", joinRequestDto.getPhone(), true);
        eb.addField("스팀 아이디", joinRequestDto.getSteamId(), true);
        eb.addField("배틀그라운드 아이디", joinRequestDto.getBattleGroundId(), true);
        eb.addField("디스코드 닉네임", discordName, true);
        eb.addField("게임 모드", joinRequestDto.getMode(), true);
        eb.addField("티어", String.valueOf(joinRequestDto.getTier()), true);
        eb.addField("선호 스트리머", joinRequestDto.getFavoriteStreamer(), true);
        return eb;
    }
}