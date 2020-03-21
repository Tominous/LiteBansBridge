package be.woutzah.litebansbridge.managers;

import be.woutzah.litebansbridge.LiteBansBridge;
import be.woutzah.litebansbridge.notifications.Notification;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;

import java.util.UUID;

public class DiscordManager {

    private LiteBansBridge plugin;
    private String stafflogId;

    public DiscordManager(LiteBansBridge plugin) {
        this.plugin = plugin;
        this.stafflogId = plugin.getConfig().getString("stafflog-textchannel-id");
    }

    public boolean userIsLinked(String id) {
        return DiscordSRV.getPlugin().getAccountLinkManager().getUuid(id) != null;
    }

    public UUID getUUIDById(String id){
        return DiscordSRV.getPlugin().getAccountLinkManager().getUuid(id);
    }

    public String getIdByUUID(UUID uuid){
        return DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(uuid);
    }

    public User getUserById(String id){
        return DiscordSRV.getPlugin().getJda().getUserById(id);
    }

    public void sendPrivateDiscordMessage(User user, String message){
            openAndSend(user,message);
    }

    public void sendPrivateDiscordMessage(User user, Notification notification){
            openAndSend(user,notification);
    }

    public void sendMessageToStaffLog(String message){
        getGuild().getTextChannelsByName(stafflogId,true)
                .get(0)
                .sendMessage(message).queue();
    }

    private void openAndSend(User user, String message){
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(message).queue();
        });
    }

    private void openAndSend(User user, Notification notification){
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(notification.toString()).queue();
        });
    }



    public Guild getGuild() {
        return DiscordSRV.getPlugin().getJda().getGuilds().stream().findFirst().orElse(null);
    }
}