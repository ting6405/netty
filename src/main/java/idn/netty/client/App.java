package idn.netty.client;





import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class App {
	private int port;
	private String hostname;
	private byte[] meg;
	private SocketChannel socketChannel;
	
	
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}
	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
	public App(int port,String hostname) {
		this.port = port;
		this.hostname = hostname;
	}
    public void bind(){
       EventLoopGroup worker = new  NioEventLoopGroup();
       Bootstrap bootstrap = new Bootstrap();
       bootstrap.group(worker);
       bootstrap.remoteAddress(hostname, port);
       bootstrap.channel(NioSocketChannel.class);
       //bootstrap.option(ChannelOption.TCP_NODELAY, true);
       bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
       bootstrap.handler(new ChannelInitializer<SocketChannel>() {

		@Override
		protected void initChannel(SocketChannel socketChannel) throws Exception {
			ChannelPipeline pipeline = socketChannel.pipeline();
			//pipeline.addLast("idleStateHandler", new IdleStateHandler(90, 90, 90));
			pipeline.addLast(new ChargeClinetHandler());
		}
	});
       ChannelFuture sync;
	try {
		sync = bootstrap.connect(hostname, port).sync();
       if(sync.isSuccess()){
    	   System.out.println("连接成功了--------------------------");
    	   socketChannel = (SocketChannel) sync.channel();
    	   ByteBuf buffer = Unpooled.copiedBuffer(meg);
    	   ChannelFuture sync2 = socketChannel.writeAndFlush(buffer).sync();
    	   if(sync2.isSuccess()){
    		   System.out.println("发送成功了-----");
    	   }
       }
       sync.channel().closeFuture().sync();
	} catch (InterruptedException e) {
		System.out.println("----------------"+e.getMessage());
	} 
	finally {
		worker.shutdownGracefully();
	}
    }
	public byte[] getMeg() {
		return meg;
	}
	public void setMeg(byte[] meg) {
		this.meg = meg;
	}
    
}
