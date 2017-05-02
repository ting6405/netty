package idn.netty.client;

import idn.netty.client.model.Charge;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ChargeClinetHandler extends ChannelHandlerAdapter {
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("exceptionCaught----------------"+cause.getMessage());
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelRegistered------");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive------");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive------------");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		byte[] buf = new byte[byteBuf.readableBytes()]; 
		byteBuf.readBytes(buf);
		System.out.println("--------length:"+buf.length);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		System.out.println("channelReadComplete------------------");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("userEventTriggered------------");
		IdleStateEvent event = (IdleStateEvent) evt;
		System.out.println("监测这个长连接"+ctx.channel());
		if(event.state() == IdleState.READER_IDLE || 
				event.state()==IdleState.WRITER_IDLE || 
				event.state()==IdleState.ALL_IDLE){
			System.out.println("这个长连接不能用了"+ctx.channel());
		}
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("close-----");
	}
   
}
