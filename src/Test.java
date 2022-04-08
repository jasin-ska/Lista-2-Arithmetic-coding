import javax.print.attribute.standard.Compression;
import java.io.File;
import java.io.IOException;

public class Test {

  public static int updateInterval;

  public static void main(String[] args) throws IOException {
    if(args.length != 2) {
      System.out.println("Usage: java Test [fileName] [updateInterval]");
      return;
    }

    String fileNameExt = args[0];
    updateInterval = Integer.parseInt(args[1]);

    String fileName = args[0].substring(0, fileNameExt.indexOf('.'));
    String ext = args[0].substring(fileNameExt.indexOf('.')+1);

    testFile(fileName, ext);

  }

  static void testFile(String name, String ext) throws IOException {
    BitsIO.reset();
    String testDirectory = "./testy";
    String filePath = testDirectory + "/" + name + "." + ext;
    String codedFilePath = "./output/" + name + "-coded." + ext;
    String decodedFilePath = "./output/" + name + "-decoded." + ext;
    int byteSize;


    //encode
    System.out.print("Encoding: ");
    byte[] input = BitsIO.getInput(filePath);
    byteSize = input.length;
    BitsIO.initOutputFile(codedFilePath);
    ArithmeticEncoder.encode(input);
    System.out.println("DONE");

    BitsIO.reset();

    //decode
    System.out.print("Decoding: ");
    BitsIO.getInput(codedFilePath);
    BitsIO.initOutputFile(decodedFilePath);
    ArithmeticDecoder.decode(byteSize);
    System.out.println("DONE");


    File fileBefore = new File(filePath);
    long sizeBefore = fileBefore.length();
    File fileCoded = new File(codedFilePath);
    long sizeCoded = fileCoded.length();
    File fileDecoded = new File(decodedFilePath);
    long sizeDecoded = fileDecoded.length();

    float entropy = Entropy.calculateEntropy(filePath);
    float avgLength = 8f*sizeCoded/sizeBefore;
    float CompressionRate = (float)sizeBefore/sizeCoded;

    System.out.println("CODING STATS ---------");
    System.out.println("\tEntropy: " + entropy);
    System.out.println("\tAvg code length: " + entropy + 2/sizeCoded);
    System.out.println("\tCompression rate: " + CompressionRate);
    System.out.println("SIZE ------------");
    System.out.println("\tBefore: " + sizeBefore);
    System.out.println("\tAfter: " + sizeCoded);
    System.out.println("\tDecoded: " + sizeDecoded);

  }
}
