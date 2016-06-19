
import java.nio.ByteOrder;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MapleSession extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
	}
	
	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object message) throws Exception {
		Channel channel = ctx.channel();

		final MaplePacketReader pr = new MaplePacketReader(Unpooled.wrappedBuffer((byte[]) message).order(ByteOrder.LITTLE_ENDIAN));
        final short header = pr.readShort();
        System.out.println("[RECV] " + header);
        switch(header) {
	        case 0x33:
	        	handleLogin(pr, channel);
	        	break;
	        case 0x2D:
	        	handleLogin2(pr, channel);
	        	break;
	        case 0x35:
	        	handleLogin3(pr, channel);
	        	break;
        }
	}
	
	public void handleLogin(MaplePacketReader pr, Channel channel) {	
		pr.readInt();
		String username = pr.readLoginAuthString();
		//String password = pr.readLoginAuthString();
		
		//System.out.println(username /* + " " + password */);
		
		MaplePacketWriter pw = new MaplePacketWriter();
		pw.writeShort(0x34);
		pw.writeInt(8);
		pw.writeInt(0);
		pw.writeShort(0);
		pw.writeLoginAuthString(username);
		pw.writeInt(0);
		pw.writeInt(0);
		pw.writeInt(87);
		pw.writeShort(0);
		
		channel.writeAndFlush(pw.getPacket());
	}
	
	public void handleLogin2(MaplePacketReader pr, Channel channel) {
		MaplePacketWriter pw = new MaplePacketWriter();
		pw.writeShort(0x2E);
		pw.writeInt(0);
		pw.writeShort(0);
		pw.writeMapleNullTerminatedCharString("RANDOM STUFF");
		pw.writeMapleNullTerminatedCharString("RANDOM NAME");
		pw.writeInt(1234); // Account ID
		pw.writeShort(0x16);
		pw.writeInt(1);
		pw.write(0);
		pw.writeInt(0); // Token part 1
		pw.writeInt(0); // Token part 2
		pw.writeInt(0); // Token part 3
		pw.writeInt(0); // Token part 4
		
		channel.writeAndFlush(pw.getPacket());
	}
	
	public void handleLogin3(MaplePacketReader pr, Channel channel) {
		MaplePacketWriter pw = new MaplePacketWriter();
		pw.writeShort(0x36);
		pw.writeInt(2);
		pw.writeInt(0);
		pw.writeShort(0);
		
		channel.writeAndFlush(pw.getPacket());
	}

}
