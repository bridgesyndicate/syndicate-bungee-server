package gg.bridgesyndicate.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatMessages {

    private static final String SYNDICATE_DISCORD_INVITE_LINK = "https://discord.gg/58yu8pDmsu";

    public static void sendUnverifiedMessages(ProxiedPlayer player, KickCode kickCode) {
        final String kickHost = System.getenv("SYNDICATE_KICK_HOST");

        player.sendMessage(new ComponentBuilder(
                "\n" +
                ChatColor.RED + "You must be verified in order to play.").create());

        TextComponent copyYourCodeMessage = new TextComponent(
                "\n" +
                ChatColor.BLUE + "--> Click HERE, then click YES <--");
        copyYourCodeMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                "http://" +
                kickHost +
                "/?" +
                kickCode.kickCode));
        player.sendMessage(copyYourCodeMessage);

        TextComponent useVerifyMessage = new TextComponent(
                "\n" +
                ChatColor.GRAY + "Then, use " +
                ChatColor.BLUE + "/verify " +
                ChatColor.GRAY + "in the Discord to register." +
                "\n");
        useVerifyMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                SYNDICATE_DISCORD_INVITE_LINK));
        player.sendMessage(useVerifyMessage);
    }

    public static void sendVerifiedMessage(ProxiedPlayer player) {
        player.sendMessage(new ComponentBuilder(
                "\n" +
                ChatColor.GRAY + "You are " +
                ChatColor.BLUE + "verified " +
                ChatColor.GRAY + "and ready to play." +
                "\n").create());
    }

    public static void sendErrorMessage(ProxiedPlayer player, String error) {
        player.sendMessage(new ComponentBuilder(
                "\n" +
                ChatColor.RED + "Error. Status was " +
                error +
                "\n").create());
    }

}
