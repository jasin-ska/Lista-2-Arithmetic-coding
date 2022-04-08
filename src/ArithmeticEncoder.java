import java.io.IOException;

import static java.lang.Integer.*;

public class ArithmeticEncoder {

    public static void encode(byte[] input) throws IOException {
        Model model = new Model();

        int high = 0xFFFFFFFF;
        int low = 0;

        int storedBits = 0;
        int byteIt = 0;
        byte b;

        while (byteIt < input.length) {
            b = input[byteIt];
            model.countByte(b);

            long range = toUnsignedLong(high) - toUnsignedLong(low) + 1;
            Probability p = model.getProbability(b);
            high = (int)(low + (range * p.high) / Probability.denominator) - 1;
            low = (int)(low + (range * p.low) / Probability.denominator);

            for (; ; ) {

                if (compareUnsigned(high, 0x80000000) < 0) {
                    outputBitAndStoredBits(false, storedBits);
                    storedBits = 0;
                    low <<= 1;
                    high <<= 1;
                    high |= 1;

                } else if (!(compareUnsigned(low, 0x80000000) < 0)) {
                    outputBitAndStoredBits(true, storedBits);
                    storedBits = 0;
                    //low-=0x80000000;
                    //high-=0x80000000;
                    low <<= 1;
                    high <<= 1;
                    high |= 1;


                } else if (!(compareUnsigned(low, 0x40000000) < 0) && compareUnsigned(high, 0xC0000000) < 0) {
                    storedBits++;
                    //low-=0x40000000;
                    //high-=0x40000000;
                    low <<= 1;
                    low &= 0x7FFFFFFF;
                    high <<= 1;
                    high |= 0x80000001;
                } else
                    break;

            }
            byteIt++;
            if((byteIt)%64 == 0) {
                model.updateRanges();
            }
        }
        BitsIO.finishByte(low, storedBits);
    }

    static void outputBitAndStoredBits(boolean bit, int storedBits) throws IOException {
        BitsIO.outputBit(bit);
        while (storedBits > 0) {
            storedBits--;
            BitsIO.outputBit(!bit);
        }

    }
}
