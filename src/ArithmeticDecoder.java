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
        for (int i = 0; i < 32; i++) {
            value <<= 1;
            value += BitsIO.getInputBit() ? 1 : 0;
        }

        while (BitsIO.isInput()) {
            long range = toUnsignedLong(high) - toUnsignedLong(low) + 1;
            int byteRange = (int)( ((toUnsignedLong((int) value) - toUnsignedLong(low)  + 1) * Probability.denominator - 1) / range);
            b = model.getByte(byteRange);
            model.countByte(b);

            Probability p = model.getProbability(b);
            BitsIO.outputByte(b);
            high = (int)(low + (range * p.high) / (Probability.denominator) - 1);
            low = (int)(low + (range * p.low) / (Probability.denominator));
            while (BitsIO.isInput()) {
                if(compareUnsigned(high, 0x80000000) < 0) {
                    low <<= 1;
                    high <<= 1;
                    high |= 1;
                }
                else if (!(compareUnsigned(low, 0x80000000) < 0)){
                    value-=0x80000000; //gt
                    low-=0x80000000; //gt
                    high -= 0x80000000; // gt
                    low <<= 1;
                    high <<= 1;
                    high |= 1;
                } else if (!(compareUnsigned(low, 0x40000000) < 0) && compareUnsigned(high, 0xC0000000) < 0) {
                    value -= 0x40000000;
                    low -= 0x40000000;
                    high -= 0x40000000;
                    low <<= 1;
                    high <<= 1;
                    high |= 1;

                } else {
                    break;
                }

                value <<= 1;
                value += BitsIO.getInputBit() ? 1 : 0;

            }
            if(model.getNumberOfBytes()%256==0) {
                model.updateRanges();
            }

        }
    }
}
