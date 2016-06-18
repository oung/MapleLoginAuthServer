import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MaplePacketEncoder extends MessageToByteEncoder<Object> {
	
	int[] xorTable = new int[] {
			0x040FC1578, 0x0113B6C1F, 0x08389CA19, 0x0E2196CD8,
			0x074901489, 0x04AAB1566, 0x07B8C12A0, 0x00018FFCD,
			0x0CCAB704B, 0x07B5A8C0F, 0x0AA13B891, 0x0DE419807,
			0x012FFBCAE, 0x05F5FBA34, 0x010F5AC99, 0x0B1C1DD01
	};

	@Override
	protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf out) throws Exception {
		System.out.println("Trying to encode");
		/*
		int temp = 0;
		int temp2 = 0;
		
		byte[] output = new byte[out.array().length];
		
		for (int i = 0; i < output.length/4; i++) {
			temp = temp2 ^ xorTable[i & 15];
			temp2 = MapleCrypto.getInt(out.array(), i*4);
			MapleCrypto.setBytes(temp ^ temp2, output, i*4);
		}
		*/
		
		byte[] input = (byte[]) message;
		System.out.println(HexTool.toString(input));
		short size = (short) (1 + 3 + 1 + 3 + 4 + 4 + input.length - 2);
		byte[] encrypted = new byte[size + 4];
		encrypted[1] = (byte) size;
		encrypted[2] = (byte) input[0];
		encrypted[4] = (byte) 0x18;
		System.arraycopy(writeSmallSize(size), 0, encrypted, 5, 3);
		encrypted[8] = 0; // flag
		System.arraycopy(writeSmallSize(size - 12), 0, encrypted, 9, 3);
		byte[] tempArr = new byte[] { (byte) 0xDE, (byte) 0xAD, (byte) 0xB0, (byte) 0x0B };
		System.arraycopy(tempArr, 0, encrypted, 12, 4);
		tempArr = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD };
		System.arraycopy(tempArr, 0, encrypted, 16, 4);
		System.arraycopy(input, 2, encrypted, 20, input.length - 2);
		System.out.println(HexTool.toString(encrypted));
		
		byte[] packet = new byte[] {
				0x00, 0x3E, 0x00, 0x34, 0x18, 0x00, 0x00, 0x3E, 0x00, 0x00, 0x00, 0x32,
				(byte) 0xDE, (byte) 0xAD, (byte) 0xB0, 0x0B, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, 0x08, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A, 0x00, 0x72, 0x00, 0x69,
				0x00, 0x63, 0x00, 0x68, 0x00, 0x61, 0x00, 0x72, 0x00, 0x64, 0x00,
				0x6C, 0x00, 0x61, 0x00, 0x6D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x57, 0x00, 0x00, 0x00, 0x00, 0x00 
		};
		out.writeBytes(packet);
		System.out.println(HexTool.toString(packet));
		System.out.println("Encode done");
	}
	
	private byte[] writeSmallSize(int input) {
		return new byte[] {(byte)(input >> 16), (byte)(input >> 8), (byte)(input >> 0) };
	}

}
