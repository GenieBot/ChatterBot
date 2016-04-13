package io.sponges.modules.chatter.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.modules.chatter.Chatter;

public class ChatbotModeCommand extends Command {

    private final Chatter chatter;

    public ChatbotModeCommand(Chatter chatter) {
        super("change the ai of the chat bot", "chatbotmode", "cmode", "chatterbotmode", "chatbotai");
        this.chatter = chatter;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (!request.getUser().hasPermission("chatterbot.mode")) {
            request.reply("You do not have permission to do that! Required permission node: \"chatterbot.mode\"");
            return;
        }
        Channel channel = request.getChannel();
        if (args.length == 0) {
            StringBuilder builder = new StringBuilder("Current chat bot mode: ");
            if (chatter.hasSession(channel)) {
                Chatter.Mode mode = chatter.getMode(channel);
                builder.append(mode.toString());
            } else {
                builder.append(Chatter.Mode.BOYFRIEND.toString());
            }
            builder.append("\n").append("Available modes: ");
            boolean first = true;
            for (Chatter.Mode mode : Chatter.Mode.values()) {
                if (!first) builder.append(", ");
                else first = false;
                builder.append(mode.toString());
            }
            builder.append("\n>> To set the chat bot mode, use 'chatbotmode [mode]'");
            request.reply(builder.toString());
            return;
        }
        String mode = args[0].toUpperCase().replace("-", "_");
        Chatter.Mode selected;
        try {
            selected = Chatter.Mode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            request.reply("Invalid mode \"" + mode + "\"!");
            return;
        }
        chatter.setMode(channel, selected);
        request.reply("Set the chat bot mode to \"" + selected.toString() + "\"! To talk to it, use 'c [question]'.");
    }
}
