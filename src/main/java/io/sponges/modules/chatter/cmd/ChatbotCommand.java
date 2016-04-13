package io.sponges.modules.chatter.cmd;

import com.google.code.chatterbotapi.ChatterBotSession;
import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.modules.chatter.Chatter;

public class ChatbotCommand extends Command {

    private final Chatter chatter;

    public ChatbotCommand(Chatter chatter) {
        super("chat with the bot", "chatterbot", "c", "chatbot", "chat");
        this.chatter = chatter;
    }

    @Override
    public void onCommand(CommandRequest commandRequest, String[] args) {
        if (args.length == 0) {
            commandRequest.reply("Usage: chat [question]\nWant a different AI to talk to? Use the 'chatbotmode' command.");
            return;
        }

        Channel channel = commandRequest.getChannel();
        ChatterBotSession session = chatter.getOrCreateSession(channel);
        final String query = Chatter.join(args, ' ');
        try {
            String response = session.think(query);
            commandRequest.reply(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
