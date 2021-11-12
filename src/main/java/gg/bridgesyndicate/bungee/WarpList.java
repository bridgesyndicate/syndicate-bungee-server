package gg.bridgesyndicate.bungee;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WarpList {
    private WarpMessage[] warpList;

    public WarpMessage[] getWarpList() {
        return warpList;
    }
}
