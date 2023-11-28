package com.example.spring_discordexample.config;

import com.example.spring_discordexample.join.listener.*;
import com.example.spring_discordexample.join.repository.JoinRepository;
import com.example.spring_discordexample.join.service.ExactService;
import com.example.spring_discordexample.join.service.JoinService;
import com.example.spring_discordexample.join.service.impl.ExactServiceImpl;
import com.example.spring_discordexample.join.service.impl.JoinServiceImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class DiscordConfig {


    private final JoinRepository joinRepository;

    @Value("${secret.key}")
    private String token;

    @Value("${guild.id}")
    private String guildId;

    public DiscordConfig(JoinRepository joinRepository) {
        this.joinRepository = joinRepository;
    }

    public JoinService joinService() {
        return new JoinServiceImpl(joinRepository);
    }

    public ExactService exactService() {
        return new ExactServiceImpl();
    }

    @Bean
    public JDA discordJda() throws LoginException {
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new JoinListener(joinService()))
                .addEventListeners(new JoinModalListener(joinService()))
                .addEventListeners(new CancelListener(joinService()))
                .addEventListeners(new CheckListener(joinService()))
                .addEventListeners(new ExtractListener(exactService()))
                .build();

        // 슬래시 커맨더 추가
        jda.updateCommands()
                .addCommands(
                        Commands.slash("join", "배틀그라운드 신청하기")
                )
                .addCommands(
                        Commands.slash("cancel", "신청 취소")
                ).addCommands(
                        Commands.slash("check", "신청 확인")
                ).addCommands(
                        // 어드민만 사용 가능
                        Commands.slash("extract", "엑셀화").setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                )
                .queue();

        return jda;
    }
}
