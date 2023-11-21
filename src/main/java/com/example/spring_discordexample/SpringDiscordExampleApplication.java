package com.example.spring_discordexample;

import com.example.spring_discordexample.config.DiscordConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@Import({DiscordConfig.class})
public class SpringDiscordExampleApplication {


    public static void main(String[] args) throws LoginException {
        SpringApplication.run(SpringDiscordExampleApplication.class, args);

    }

}
