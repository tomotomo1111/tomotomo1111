import java.io.IOException;
import java.nio.file.Paths;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {
    
    public JsonReader() {

    }

    public void read(String jsonPath) throws JsonProcessingException, IOException  {
        ObjectMapper  objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(Paths.get(jsonPath).toFile());
        System.out.println(json); 
    }
}
