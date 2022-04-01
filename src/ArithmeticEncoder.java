import jdk.jfr.Unsigned;

import javax.swing.*;
import java.io.IOException;

import static java.lang.Integer.*;

public class ArithmeticEncoder {

    public static void encode(byte[] input) throws IOException {
        Model model = new Model();
        int high = 0xFFFFFFFF;
        //System.out.println("high: " + high);
        //System.out.println("unsigned high: " + toUnsignedLong(high));
        int low = 0;
        //System.out.println("low: " + low);
        //System.out.println("unsigned low: " + toUnsignedLong(low));
        //System.out.println("uh - ul = " + (toUnsignedLong(high) - toUnsignedLong(low) + 1));
        int pending_bits = 0;
        int byte_it = 0;
        byte b;

        while (byte_it < input.length) {
            b = input[byte_it];
            //System.out.println("byte_it: " + byte_it);
            //System.out.println("byte b: " + b);
            model.countByte(b);

            long range = toUnsignedLong(high) - toUnsignedLong(low) + 1;
            //System.out.println("range: " + range);
            Probability p = model.getProbability(b);
            high = (int)(low + (range * p.high) / Probability.denominator);
            //System.out.println("high after 28: " + high);
            low = (int)(low + (range * p.low) / Probability.denominator);
            //System.out.println("low after 30: " + low);
            for (; ; ) {
                if (compareUnsigned(high, 0x80000000) < 0) {
                    output_bit_plus_pending(false, pending_bits);
                    pending_bits = 0;
                    low <<= 1;
                    high <<= 1;
                    high |= 1;
                } else if (!(compareUnsigned(low, 0x80000000) < 0)) {
                    System.out.print("IN TRUE");
                    output_bit_plus_pending(true, pending_bits);
                    pending_bits = 0;
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
