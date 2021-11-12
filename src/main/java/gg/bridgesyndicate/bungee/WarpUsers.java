package gg.bridgesyndicate.bungee;

import java.net.UnknownHostException;

public class WarpUsers {
    private final PluginMain pluginMain;
    private final WarpList warpList;

    public WarpUsers(PluginMain pluginMain, WarpList warpList) {
        this.pluginMain = pluginMain;
        this.warpList = warpList;
    }

    public void warp() throws UnknownHostException {
        for(WarpMessage warpMessage : warpList.getWarpList()){
            WarpUser warpUser = new WarpUser(pluginMain, warpMessage);
            warpUser.warp();
        }
    }
}
