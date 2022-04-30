import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Test {

    public static int updateInterval;
    private static final String testDirectory = "./testy";

    private static boolean isDecodedEqual(String name, String ext) throws IOException {
        String filePath = testDirectory + "/" + name + "." + ext;
        String decodedFilePath = "./output/" + name + "-decoded." + ext;

        byte[] fileBefore = Files.readAllBytes(Paths.get(filePath));
        byte[] fileDecoded = Files.readAllBytes(Paths.get(decodedFilePath));
        return Arrays.equals(fileBefore, fileDecoded);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        if (args.length != 2) {
            System.out.println("Usage: java Test [fileName] [updateInterval]");
            return;
        }
        System.out.println("File: " + args[0]);

        String fileNameExt = args[0];
        updateInterval = Integer.parseInt(args[1]);

        String fileName = args[0].substring(0, fileNameExt.indexOf('.'));
        String ext = args[0].substring(fileNameExt.indexOf('.') + 1);

        testFile(fileName, ext);
        System.out.println(isDecodedEqual(fileName, ext) ? "ORGINAL = DECODED" : "DECODING ERROR");
    }

    static void testFile(String name, String ext) throws IOException {
        BitsIO.reset();
        String filePath = testDirectory + "/" + name + "." + ext;
        String codedFilePath = "./output/" + name + "-coded." + ext;
        String decodedFilePath = "./output/" + name + "-decoded." + ext;
        int byteSize;

        //encode
        System.out.print("Encoding: ");
        byte[] input = BitsIO.getInput(filePath);
        byteSize = input.length;
        BitsIO.initOutputFile(codedFilePath);
        long cStart = System.currentTimeMillis();
        ArithmeticEncoder.encode(input);
        long cStop = System.currentTimeMillis();
        System.out.println("DONE");
        long encodingTime = cStop - cStart;

        BitsIO.reset();

        //decode
        System.out.print("Decoding: ");
        BitsIO.getInput(codedFilePath);
        BitsIO.initOutputFile(decodedFilePath);
        long dStart = System.currentTimeMillis();
        ArithmeticDecoder.decode(byteSize);
        long dStop = System.currentTimeMillis();
        System.out.println("DONE");
        long decodingTime = dStop - dStart;


        File fileBefore = new File(filePath);
        long sizeBefore = fileBefore.length();
        File fileCoded = new File(codedFilePath);
        long sizeCoded = fileCoded.length();
        File fileDecoded = new File(decodedFilePath);
        long sizeDecoded = fileDecoded.length();

        float entropy = Entropy.calculateEntropy(filePath);
        float avgLength = 8f * sizeCoded / sizeBefore;
        float CompressionRate = (float) sizeBefore / sizeCoded;

        System.out.println("CODING STATS ---------");
        System.out.println("\tEntropy: " + entropy);
        System.out.println("\tAvg code length: " + avgLength);
        System.out.println("\tCompression rate: " + CompressionRate);
        System.out.println("SIZE ------------");
        System.out.println("\tBefore: " + sizeBefore);
        System.out.println("\tAfter: " + sizeCoded);
        System.out.println("\tDecoded: " + sizeDecoded);
        System.out.println("TIME ------------");
        System.out.println("\tCompression: " + encodingTime / 1000f + " s");
        System.out.println("\tDecompression: " + decodingTime / 1000f + " s");

    }
}
