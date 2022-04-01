import java.io.IOException;

public class Test {
  public static void main(String[] args) throws IOException {
    /*String path = args[0];
    if(args.length != 1) {
      System.out.println("Usage: java Entropy <path>");
      return;
    }*/

    //testEncoding();
    testDecoding();

  }

  static void testEncoding() throws IOException {
    String path = "./testy1/small-test.txt";
    byte[] input = BitsIO.getInput(path);
    BitsIO.initOutputFile("./output/small-test-coded.txt");
    ArithmeticEncoder.encode(input);
  }

  static void testDecoding() throws IOException {
    String path = "./output/small-test-coded.txt";
    byte[] input = BitsIO.getInput(path);
    BitsIO.initOutputFile("./output/small-test-decoded.txt");
    ArithmeticDecoder.decode(input);
  }
}
