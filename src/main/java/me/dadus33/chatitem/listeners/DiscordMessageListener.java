package me.dadus33.chatitem.listeners;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import me.dadus33.chatitem.utils.Storage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.dadus33.chatitem.listeners.ChatEventListener.SEPARATOR;

public class DiscordMessageListener {

    private Storage config;

    public DiscordMessageListener(Storage storage) {
        config = storage;
    }

    @Subscribe
    public void onMessageSendToDiscord(GameChatMessagePreProcessEvent event) {

        final Player p = event.getPlayer();
        String message = event.getMessage();

        if (!message.endsWith(SEPARATOR + p.getName()))
            return;

        message = message.substring(0, message.length() - p.getName().length() - 1).replace(SEPARATOR + "", "");

        String itemName;
        final ItemStack itemStack = p.getInventory().getItemInMainHand();
        if (!itemStack.getType().equals(Material.AIR)) {
            itemName = ChatPacketListener.styleItem(itemStack, config);
        } else {
            itemName = config.HAND_NAME
                    .replace("{name}", p.getName())
                    .replace("{display-name}", p.getDisplayName());
        }

        for (String rep : config.PLACEHOLDERS) {
            message = message.replace(rep, SEPARATOR + "");
        }

        message = message.replace(SEPARATOR + "", itemName);
        event.setMessage(message);
    }
}
