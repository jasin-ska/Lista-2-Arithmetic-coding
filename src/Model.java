public class Model {
    private int[] bytesCount = new int[256];
    private Probability[] bytesRanges = new Probability[256];
    private int numberOfBytes = 256;

    public Model() {
        initBytesCount();
        for(int i = 0; i< 256; i++) bytesRanges[i] = new Probability();
        updateRanges();
    }

    public void incrementNumberOfBytes() {
        numberOfBytes++;
    }

    public void countByte(byte b) {
        int idx = (int) b + 128;
        bytesCount[idx]++;
    }

    public Probability getProbability(byte b) {
        int idx = (int) b + 128;
        return bytesRanges[idx];
    }

    public byte getByte(int z) {
        for(int i = 0; i < 256; i++) {
            if(z >= bytesRanges[i].low && z < bytesRanges[i].high) return (byte)(i-128);
        }
        return -1;
    }

    private void initBytesCount() {
        for(int i = 0; i < 256; i++) bytesCount[i] = 1;
    }

    public void updateRanges() {
        Probability.denominator = numberOfBytes;
        int it = 0;
        int byteNr = 0;
        for(Probability p : bytesRanges) {
            p.low = it;
            p.high = it + bytesCount[byteNr];
            it += bytesCount[byteNr];
            byteNr++;
        }
    }

    public int getNumberOfBytes() {
        return numberOfBytes;
    }
}
