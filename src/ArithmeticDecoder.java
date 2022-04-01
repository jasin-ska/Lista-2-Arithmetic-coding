import java.io.IOException;

import static java.lang.Integer.compareUnsigned;
import static java.lang.Integer.toUnsignedLong;

public class ArithmeticDecoder {

    public static void decode(byte[] input) throws IOException {
        Model model = new Model();
        int high = 0xFFFFFFFF;
        int low = 0;
        long value = 0;
        byte b;
        System.out.print("Value in bits: ");
        for (int i = 0; i < 32; i++) {
            value <<= 1;
            value += BitsIO.getInputBit() ? 1 : 0;
            //System.out.print((value & 1)); value jest git
        }
        System.out.println();

        while (BitsIO.isInput()) {
            System.out.println("value: " + value);
            System.out.print("Value in bits: ");
            for(int i = 0; i<32; i++) System.out.print(((value >> (31-i)) & 1));
            System.out.println();
            long range = toUnsignedLong(high) - toUnsignedLong(low) + 1;
            //System.out.println("range = " + range);
            int byteRange = (int)( ((value - low + 1) * Probability.denominator - 1) / range);
            //int byteRange = (int)( ((value - low + 1)) / range);
            System.out.println("(value - low + 1) * Prob.denom - 1) = " + ((value - low + 1) * Probability.denominator - 1));
            System.out.println("value - low + 1 = " +(value - low + 1));
            System.out.println("Probability.denominator = " +(Probability.denominator));
            System.out.println("count: " + byteRange);
            b = model.getByte(byteRange);
            System.out.println("Decoded byte: " + b);
            model.countByte(b);
            if(model.getNumberOfBytes()%256==0) model.updateRanges();
            Probability p = model.getProbability(b);
            System.out.println("bytes low and high: " + p.low + ", " + p.high);
            BitsIO.outputByte(b);
            high = (int)(low + (range * p.high) / (Probability.denominator) - 1);
            System.out.println("high after 28: " + high);
            low = (int)(low + (range * p.low) / (Probability.denominator));
            System.out.println("low after 30: " + low);
            while (BitsIO.isInput()) {
                if (!(compareUnsigned(low, 0x80000000) < 0)|| compareUnsigned(high, 0x80000000) < 0){
                    //System.out.println("IF 1");
                    low <<= 1;
                    high <<= 1;
                    high |= 1;
                    value <<= 1;
                    value += BitsIO.getInputBit() ? 1 : 0;
                } else if (!(compareUnsigned(low, 0x40000000) < 0) && compareUnsigned(high, 0xC0000000) < 0) {
                    //System.out.println("IF 2");
                    low <<= 1;
                    low &= 0x7FFFFFFF;
                    high <<= 1;
                    high |= 0x80000001;
                    value -= 0x4000000;
                    value <<= 1;
                    value += BitsIO.getInputBit() ? 1 : 0;
                } else {
                    //System.out.println("ELSE");
                    break;
                }

            }
        }
    }
}
