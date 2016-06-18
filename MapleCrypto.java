
public class MapleCrypto {
	
	public static long getUnsignedLong(byte[] buffer, int offset) {
		final int a = (int)(buffer[offset + 0] << 0);
		final int b = ((int)(buffer[offset + 1])) << 8;
		final int c = ((int)(buffer[offset + 2])) << 16;
		final int d = ((int)(buffer[offset + 3])) << 24;
		
		return Integer.toUnsignedLong(a | b | c | d);
	}
	
	public static void setBytes(long input, byte[] buffer, int offset) {
		buffer[offset + 0] = (byte) ((input >> 0) & 0xFF);
		buffer[offset + 1] = (byte) ((input >> 8) & 0xFF);
		buffer[offset + 2] = (byte) ((input >> 16) & 0xFF);
		buffer[offset + 3] = (byte) ((input >> 24) & 0xFF);
	}

}
