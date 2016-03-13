package io.sponges.modules.chatter;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.module.Module;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chatter extends Module {

    private final Map<Channel, ChatterBotSession> sessions = new ConcurrentHashMap<>();

    private final ChatterBotFactory chatterBotFactory = new ChatterBotFactory();
    private ChatterBot chatterBot = null;

    public Chatter() {
        super("ChatterBot", "1.0-SNAPSHOT");
    }

    @Override
    public void onEnable() {
        try {
            this.chatterBot = chatterBotFactory.create(ChatterBotType.PANDORABOTS, "94023160ee3425e0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCommandManager().registerCommand(this, new Command("chat with the bot", "chatterbot", "c", "chatbot", "chat") {
            @Override
            public void onCommand(CommandRequest commandRequest, String[] args) {
                Channel channel = commandRequest.getChannel();
                ChatterBotSession session;
                if (sessions.containsKey(channel)) {
                    session = sessions.get(channel);
                } else {
                    session = chatterBot.createSession();
                    sessions.put(channel, session);
                }
                final String query = join(args, ' ');
                try {
                    String response = session.think(query);
                    commandRequest.reply(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDisable() {

    }

    private static String join(String[] input, char joiner) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            if (i != 0) builder.append(joiner);
            builder.append(input[i]);
        }
        return builder.toString();
    }
}
