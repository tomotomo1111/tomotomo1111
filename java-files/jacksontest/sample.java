import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Sample {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // settings.json を読み込む
        Settings settings = mapper.readValue(
            new File("settings.json"), 
            Settings.class
        );

        System.out.println("username : " + settings.username);
        System.out.println("volume   : " + settings.volume);
        System.out.println("brightness : " + settings.brightness);

        // volume が int か float か判定する例
        if (settings.volume instanceof Integer) {
            System.out.println("volume は int 値: " + settings.volume.intValue());
        } else if (settings.volume instanceof Double) {
            System.out.println("volume は float 値: " + settings.volume.doubleValue());
        }
    }
}


class Settings {
    public String username;
    public Number volume;      // int でも float でも OK
    public Number brightness;  // int でも float でも OK
}

