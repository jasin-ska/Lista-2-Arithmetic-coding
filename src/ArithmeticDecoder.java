import java.io.IOException;

import static java.lang.Integer.compareUnsigned;
import static java.lang.Integer.toUnsignedLong;

public class ArithmeticDecoder {

    public static void decode(byte[] input) throws IOException {
        Model model = new Model();
        int high = 0xFFFFFFFF;
        int low = 0;
        int value = 0;
        byte b;
        for (int i = 0; i < 32; i++) {
            value <<= 1;
            value += BitsIO.getInputBit() ? 1 : 0;
        }
        for (; ; ) {
            int range = (int)(toUnsignedLong(high) - toUnsignedLong(low) + 1);
            int count = ((value - low + 1) * model.getNumberOfBytes() - 1) / range;
            b = model.getByte(count);
            model.countByte(b);
            if(model.getNumberOfBytes()%256==0) model.updateRanges();
            Probability p = model.getProbability(b);
            //if (c == 256)
            //    break;
            BitsIO.outputByte(b);
            high = low + (range * p.high) / (p.high - p.low) - 1;
            low = low + (range * p.low) / (p.high - p.low);
            for (; ; ) {
                if (!(compareUnsigned(low, 0x80000000) < 0)|| compareUnsigned(high, 0x80000000) < 0){
                    low <<= 1;
                    high <<= 1;
                    high |= 1;
                    value <<= 1;
                    value += BitsIO.getInputBit() ? 1 : 0;
                } else if (!(compareUnsigned(low, 0x40000000) < 0) && compareUnsigned(high, 0xC0000000) < 0) {
                    low <<= 1;
                    low &= 0x7FFFFFFF;
                    high <<= 1;
                    high |= 0x80000001;
                    value -= 0x4000000;
                    value <<= 1;
                    value += BitsIO.getInputBit() ? 1 : 0;
                } else
                    break;
            }
        }
    }
}
