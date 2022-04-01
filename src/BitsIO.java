import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class BitsIO {
    private static byte currByte = 0;
    private static int bitsInCurrByte = 0;
    private static FileOutputStream outputStream;
    private static byte[] input;
    private static int inputByteIt = 0;
    private static int inputBitIt = 0;

    public static void initOutputFile(String path) throws FileNotFoundException {
        File outputFile = new File(path);
        outputStream = new FileOutputStream(outputFile);
    }


    public static void output_bit(boolean bit) throws IOException {
        currByte <<= 1;
        currByte += bit ? 1 : 0;
        bitsInCurrByte++;
        if(bitsInCurrByte == 8) {
            outputByte(currByte);
            currByte = 0;
            bitsInCurrByte = 0;
        }
    }

    public static void outputByte(byte b) throws IOException {
        outputStream.write(b);
    }

    public static byte[] getInput(String path) throws IOException {
        input = Files.readAllBytes(Paths.get(path));
        return input;
    }

    public static boolean getInputBit() {
        byte b = input[inputByteIt];

        return false; //TODO:: aaasdads
    }
}
