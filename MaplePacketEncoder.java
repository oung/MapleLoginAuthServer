
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
		/*  encryption 
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
		short size = (short) (1 + 3 + 1 + 3 + 4 + 4 + input.length - 2);
		
		ByteBuf buffer = Unpooled.buffer(size, size + 4);
		buffer.writeShort(size);
		buffer.writeShort(input[0]);
		buffer.writeByte(0x18);
		buffer.writeBytes(writeSmallSize(size));
		buffer.writeByte(0);
		buffer.writeBytes(writeSmallSize(size - 12));
		buffer.writeInt(0xDEADB00B);
		buffer.writeInt(0xAABBCCDD);
		buffer.writeBytes(input, 2, input.length - 2);

		//System.out.println(HexTool.toString(buffer.array()));

		out.writeBytes(buffer.array());
	}
	
	private byte[] writeSmallSize(int input) {
		return new byte[] {(byte)(input >> 16), (byte)(input >> 8), (byte)(input >> 0) };
	}

}
