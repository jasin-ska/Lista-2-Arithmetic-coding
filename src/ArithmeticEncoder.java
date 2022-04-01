import jdk.jfr.Unsigned;

import javax.swing.*;
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

            int range = (int)(toUnsignedLong(high) - toUnsignedLong(low) + 1);
            Probability p = model.getProbability(b);
            high = low + (range * p.high) / Probability.denominator;
            low = low + (range * p.low) / Probability.denominator;
            for (; ; ) {
                if (compareUnsigned(high, 0x80000000) < 0) {
                    output_bit_plus_pending(false, pending_bits);
                    low <<= 1;
                    high <<= 1;
                    high |= 1;
                } else if (!(compareUnsigned(low, 0x80000000) < 0)) {
                    output_bit_plus_pending(true, pending_bits);
                    low <<= 1;
                    high <<= 1;
                    high |= 1;
                } else if (!(compareUnsigned(low, 0x40000000) < 0) && compareUnsigned(high, 0xC0000000) < 0) {
                    pending_bits++;
                    low <<= 1;
                    low &= 0x7FFFFFFF;
                    high <<= 1;
                    high |= 0x80000001;
                } else
                    break;
            }
            byte_it++;
            if((byte_it)%256 == 0) model.updateRanges();
        }
    }

    static void output_bit_plus_pending(boolean bit, int pending_bits) throws IOException {
        BitsIO.output_bit(bit);
        while (pending_bits > 0) {
            pending_bits--;
            BitsIO.output_bit(!bit);
        }

    }
}
