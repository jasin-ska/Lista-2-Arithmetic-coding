import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Entropy {


    public static float calculateEntropy(String path) {


        try {
            byte[] byteArray = Files.readAllBytes(Paths.get(path));

            Map<Byte, Integer> bytesCount = new HashMap<>(); // ile razy wystepuje
            for(byte b : byteArray) {
                if(!bytesCount.containsKey(b)) {
                    bytesCount.put(b, 1);
                }
                else {
                    bytesCount.put(b, bytesCount.get(b) + 1);
                }
            }

            float HX = 0;
            for(byte b : bytesCount.keySet()) {
                float pb = (float)bytesCount.get(b)/byteArray.length;
                HX-=pb*(Math.log(pb)/Math.log(2));
            }

            return HX;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;

    }
}
