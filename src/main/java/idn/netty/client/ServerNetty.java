package idn.netty.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.socks.SocksMessageEncoder;

public class ServerNetty {
     private int port; //要监听的端口
     private void bind(int port){
    	 EventLoopGroup boss = new NioEventLoopGroup();
    	 EventLoopGroup worker = new NioEventLoopGroup();
    	 ServerBootstrap server = new ServerBootstrap();
    	 server.group(boss, worker);
    	 server.channel(NioServerSocketChannel.class);
    	 //最大tcp连接数 128
    	 server.option(ChannelOption.SO_BACKLOG,128);
    	 //关闭tcp Nagle算法
    	 server.option(ChannelOption.TCP_NODELAY,true);
    	 //指定等待时间为0，此时调用主动关闭时不会发送FIN来结束连接，而是直接将连接设置为CLOSE状态，清除套接字中的发送和接收缓冲区，直接对对端发送RST包。    
    	 server.option(ChannelOption.SO_LINGER,0);
    	 server.childOption(ChannelOption.SO_LINGER, 0);
    	 //保存长连接
    	 server.childOption(ChannelOption.SO_KEEPALIVE, true);
    	 
    	 server.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				// TODO Auto-generated method stub
				ChannelPipeline pipeline = socketChannel.pipeline();
				//在这里添加自己的handler 解压 编码器
				
			}
    		 
		});
     }
}
