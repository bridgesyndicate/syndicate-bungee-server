package gg.bridgesyndicate.bungee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class WarpMessageTest {

    public static String read(File filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        try( BufferedReader br =
                     new BufferedReader( new InputStreamReader(fis, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
            return sb.toString();
        }
    }

    @Test
    public void deserialize() throws IOException {
        String resourceName = "warps.json";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
        String message = read(file);
        ObjectMapper objectMapper = new ObjectMapper();
        WarpList warpList = objectMapper.readValue(message, WarpList.class);
        assertEquals(8, warpList.getWarpList().length);
    }

    @Test
    public void deserialize2() throws IOException {
        String resourceName = "lobby-warp.json";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
        String message = read(file);
        ObjectMapper objectMapper = new ObjectMapper();
        WarpList warpList = objectMapper.readValue(message, WarpList.class);
        assertEquals(1, warpList.getWarpList().length);
    }
}