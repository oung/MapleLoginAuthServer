
import java.nio.ByteOrder;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MapleSession extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		System.out.println("Got Login Connection");
	}
	
	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object message) throws Exception {
		Channel channel = ctx.channel();
		System.out.println("read packet");
		
		final MaplePacketReader pr = new MaplePacketReader(Unpooled.wrappedBuffer((byte[]) message).order(ByteOrder.LITTLE_ENDIAN));
        final short header = pr.readShort();
        System.out.println(header);
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
		//pr.readInt();
		System.out.println("handleLogin");
		String username = pr.readMapleAsciiString();
		//String password = pr.readMapleAsciiString();
		
		System.out.println(username /* + " " + password */);
		
		MaplePacketWriter pw = new MaplePacketWriter();
		pw.writeShort(0x3400);
		pw.writeInt(8);
		pw.writeInt(0);
		pw.writeShort(0);
		pw.writeMapleAsciiString(username);
		pw.writeInt(0);
		pw.writeInt(0);
		pw.writeInt(87);
		pw.writeShort(0);
		
		channel.writeAndFlush(pw.getPacket());
	}
	
	public void handleLogin2(MaplePacketReader pr, Channel channel) {
		
	}
	
	public void handleLogin3(MaplePacketReader pr, Channel channel) {
		
	}

}
