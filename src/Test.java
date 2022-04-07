import java.io.IOException;

public class Test {
  public static String testDirectory = "./testy1";

  public static void main(String[] args) throws IOException {

    testFile("pan-tadeusz", "txt");

  }




  static void testFile(String name, String ext) throws IOException {
    String filePath = testDirectory + "/" + name + "." + ext;
    String codedFilePath = "./output/" + name + "-coded." + ext;
    String decodedFilePath = "./output/" + name + "-decoded." + ext;

    //encode
    byte[] input = BitsIO.getInput(filePath);
    BitsIO.initOutputFile(codedFilePath);
    ArithmeticEncoder.encode(input);

    BitsIO.reset();

    //decode
    input = BitsIO.getInput(codedFilePath);
    BitsIO.initOutputFile(decodedFilePath);
    ArithmeticDecoder.decode(input);
  }
}
