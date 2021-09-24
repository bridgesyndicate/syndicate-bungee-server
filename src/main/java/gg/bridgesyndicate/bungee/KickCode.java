package gg.bridgesyndicate.bungee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KickCode {

    public String kickCode;

    public KickCode() {
    }

    public String getKickCode() {
        return kickCode;
    }

    public void setKickCode(String kickCode) {
        this.kickCode = kickCode;
    }

    String serialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return( mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this) );
    }

    public static KickCode deserialize(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        KickCode kickCode = new KickCode();
        try {
            kickCode = objectMapper.readValue(json, KickCode.class);
        } catch (
                JsonProcessingException e) {
            System.err.println("Cannot parse kick code.");
            e.printStackTrace();
        }
        return(kickCode);
    }
}