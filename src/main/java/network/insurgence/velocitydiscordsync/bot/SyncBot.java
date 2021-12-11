package network.insurgence.velocitydiscordsync.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import network.insurgence.velocitydiscordsync.bot.listeners.ReadyListener;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;

public class SyncBot {

    private final Logger logger = VelocityDiscordSync.getLogger();

    private final JDA jda;

    public SyncBot(String token) {
        JDABuilder builder = JDABuilder.createDefault(token);

        builder.disableCache(CacheFlag.VOICE_STATE);

        jda = login(builder);
        registerEvents();
    }


    private void registerEvents() {
        jda.addEventListener(new ReadyListener());
    }


    /**
     * Attempts to login to the JDA instance.
     * @param builder The JDABuilder instance to login with.
     * @return The Bot instance.
     */
    private JDA login(JDABuilder builder) {
        JDA login = null;
        try {
            login = builder.build();
        } catch (LoginException e) {
            logger.error("There was an issue logging in to Discord", e);
            e.printStackTrace();
        }

        return login;
    }
}
