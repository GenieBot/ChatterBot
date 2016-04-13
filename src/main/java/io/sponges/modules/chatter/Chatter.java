package io.sponges.modules.chatter;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import io.sponges.bot.api.cmd.CommandManager;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.module.Module;
import io.sponges.modules.chatter.cmd.ChatbotCommand;
import io.sponges.modules.chatter.cmd.ChatbotModeCommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chatter extends Module {

    public static final ChatterBotFactory chatterBotFactory = new ChatterBotFactory();

    private final Map<Channel, ChatterBotSession> sessions = new ConcurrentHashMap<>();
    private final Map<Channel, Mode> modes = new ConcurrentHashMap<>();

    public Chatter() {
        super("ChatterBot", "1.01-SNAPSHOT");
    }

    @Override
    public void onEnable() {
        CommandManager commandManager = getCommandManager();
        commandManager.registerCommand(this, new ChatbotCommand(this));
        commandManager.registerCommand(this, new ChatbotModeCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public static String join(String[] input, char joiner) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            if (i != 0) builder.append(joiner);
            builder.append(input[i]);
        }
        return builder.toString();
    }

    public boolean hasSession(Channel channel) {
        return sessions.containsKey(channel);
    }

    public ChatterBotSession getSession(Channel channel) {
        return sessions.get(channel);
    }

    public Mode getMode(Channel channel) {
        return modes.get(channel);
    }

    public ChatterBotSession getOrCreateSession(Channel channel) {
        if (hasSession(channel)) return getSession(channel);
        else return createSession(channel);
    }

    public ChatterBotSession createSession(Channel channel) {
        Mode mode;
        if (!modes.containsKey(channel)) {
            modes.put(channel, Mode.BOYFRIEND);
            mode = Mode.BOYFRIEND;
        } else {
            mode = modes.get(channel);
        }
        ChatterBotSession session = mode.getBot().createSession();
        sessions.put(channel, session);
        return session;
    }

    public void setMode(Channel channel, Mode mode) {
        modes.put(channel, mode);
        sessions.remove(channel);
    }

    public enum Mode {

        CHOMSKY("b0dafd24ee35a477"),
        LEVI("b8c97d77ae3471d8"),
        FARHA("dbf443e58e345c14"),
        ROZA("c9c4b9bf6e345c25"),
        JERVIS("9efbc6c80e345e65"),
        LISA("b0a6a41a5e345c23"),
        BOYFRIEND("94023160ee3425e0"),
        LAUREN("f6d4afd83e34564d"),
        LAILA("a66718a38e345c15"),
        SANTAS_ROBOT("c39a3375ae34d985");

        private ChatterBot bot;

        Mode(String key) {
            try {
                this.bot = Chatter.chatterBotFactory.create(ChatterBotType.PANDORABOTS, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return name().toLowerCase().replace("_", "-");
        }

        public ChatterBot getBot() {
            return bot;
        }

    }

}
