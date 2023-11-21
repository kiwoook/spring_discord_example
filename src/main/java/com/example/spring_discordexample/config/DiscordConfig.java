package com.example.spring_discordexample.config;

import com.example.spring_discordexample.join.listener.JoinListener;
import com.example.spring_discordexample.join.listener.JoinModalListener;
import com.example.spring_discordexample.join.repository.JoinRepository;
import com.example.spring_discordexample.join.service.JoinService;
import com.example.spring_discordexample.join.service.impl.JoinServiceImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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

    public JoinModalListener joinModalListener() {
        return new JoinModalListener(joinService());
    }

    public JoinListener joinListener(){
        return new JoinListener(joinService());
    }
    @Bean
    public JDA discordJda() throws LoginException {
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(joinListener())
                .addEventListeners(joinModalListener())
                .build();

        jda.updateCommands()
                .addCommands(
                        Commands.slash("join", "배틀그라운드 신청하기")
                )
                .addCommands(
                        Commands.slash("cancel", "신청 취소")
                )
                .queue();

        return jda;
    }
}
