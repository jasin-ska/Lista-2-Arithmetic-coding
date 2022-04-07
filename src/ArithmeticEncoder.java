import java.io.IOException;

import static java.lang.Integer.*;

public class ArithmeticEncoder {

    public static void encode(byte[] input) throws IOException {
        Model model = new Model();

        int high = 0xFFFFFFFF;
        int low = 0;

        int pending_bits = 0;
        int byte_it = 0;
        byte b;

        while (byte_it < input.length) {
            b = input[byte_it];
            model.countByte(b);

            long range = toUnsignedLong(high) - toUnsignedLong(low) + 1;
            Probability p = model.getProbability(b);
            high = (int)(low + (range * p.high) / Probability.denominator) - 1;
            low = (int)(low + (range * p.low) / Probability.denominator);
            for (; ; ) {
                if (compareUnsigned(high, 0x80000000) < 0) {
                    outputBitAndPendingBits(false, pending_bits);
                    pending_bits = 0;
                } else if (!(compareUnsigned(low, 0x80000000) < 0)) {
                    outputBitAndPendingBits(true, pending_bits);
                    pending_bits = 0;
                    low-=0x80000000;
                    high-=0x80000000;
                } else if (!(compareUnsigned(low, 0x40000000) < 0) && compareUnsigned(high, 0xC0000000) < 0) {
                    pending_bits++;
                    low-=0x40000000;
                    high-=0x40000000;

                } else
                    break;
                low <<= 1;
                high <<= 1;
                high |= 1;
            }
            byte_it++;
            if((byte_it)%256 == 0) {
                model.updateRanges();
            }
        }
        BitsIO.finishByte(low, pending_bits);
    }

    static void outputBitAndPendingBits(boolean bit, int pending_bits) throws IOException {
        BitsIO.outputBit(bit);
        while (pending_bits > 0) {
            pending_bits--;
            BitsIO.outputBit(!bit);
        }

    }
}
