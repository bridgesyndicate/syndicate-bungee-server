package gg.bridgesyndicate.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatMessages {

    public static void sendUnverifiedMessages(ProxiedPlayer player, KickCode kickCode, String kickHost) {

        player.sendMessage(new ComponentBuilder(
                "\n" +
                ChatColor.RED + "You must be verified in order to play.").create());

        TextComponent copyYourCodeMessage = new TextComponent(
                "\n" +
                ChatColor.GREEN + "--> Click here to copy your code! <--");
        copyYourCodeMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                "http://" +
                kickHost +
                "/?" +
                kickCode.kickCode));
        player.sendMessage(copyYourCodeMessage);

        TextComponent useVerifyMessage = new TextComponent(
                "\n" +
                ChatColor.GRAY + "Then, use " +
                ChatColor.GREEN + "/verify " +
                ChatColor.GRAY + "in the Discord to register." +
                "\n");
        // useVerifyMessage.setClickEvent(to whatever the discord link would be);
        player.sendMessage(useVerifyMessage);
    }

}
