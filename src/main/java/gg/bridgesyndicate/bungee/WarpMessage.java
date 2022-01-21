package gg.bridgesyndicate.bungee;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@SuppressWarnings("deprecation")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WarpMessage {

    private String minecraftUuid;
    @SuppressWarnings("unused")
    private String hostname;
    @SuppressWarnings("unused")
    private String port;
    @SuppressWarnings("unused")
    private boolean cached;

    @SuppressWarnings("unused")
    public WarpMessage() {
    }

    public WarpMessage(UUID playerUUID) {
        minecraftUuid = playerUUID.toString();
    }

    public String getHostname() {
        return hostname;
    }

    public String getMinecraftUuid() {
        return minecraftUuid;
    }

    @SuppressWarnings("unused")
    public String getPort() {
        return port;
    }

    public boolean isCached(){
        return cached;
    }
}