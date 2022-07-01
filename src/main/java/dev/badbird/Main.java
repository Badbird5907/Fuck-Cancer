package dev.badbird;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main extends ListenerAdapter {
    private static List<String> allowed = new ArrayList<>();
    private static JDA jda;
    private static List<String> channels = new ArrayList<>();

    public static void main(String[] args) {
        File file = new File("config.json");
        Gson gson = new Gson();
        try {
            JsonObject json = gson.fromJson(new String(Files.readAllBytes(file.toPath())), JsonObject.class);
            String token = json.get("token").getAsString();

            for (JsonElement jsonElement : json.get("allowed").getAsJsonArray()) {
                allowed.add(jsonElement.getAsString());
            }
            jda = JDABuilder.createDefault(token).build();
            jda.addEventListener(new Main());
            jda.awaitReady();

            for (JsonElement channel : json.get("channels").getAsJsonArray()) {
                channels.add(channel.getAsString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if (!channels.contains(event.getChannel().getId())) {
            return;
        }
        for (String s : allowed) {
            if (!event.getMessage().getContentRaw().equalsIgnoreCase(s)) {
                event.getMessage().delete().queue();
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        if (!channels.contains(event.getChannel().getId())) {
            return;
        }

        for (String s : allowed) {
            if (!event.getMessage().getContentRaw().equalsIgnoreCase(s)) {
                event.getMessage().delete().queue();
            }
        }
    }
}
