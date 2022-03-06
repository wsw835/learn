package nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author: wensw
 * @description: 了解NIO核心非阻塞原理 - 非阻塞式NIO
 */
public class TestUnBlockedNio {

    @Test
    public void unBlockedClient() throws Exception{
        //1、获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9494));
        //2、设置为非阻塞模式
        socketChannel.configureBlocking(false);
        //3、设置缓冲区、存储数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("非阻塞NIO信息发送".getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();

        //4、关闭channel
        socketChannel.close();
    }

    @Test
    public void unBlockedServer() throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //设置非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //绑定客户端通道channel
        serverSocketChannel.bind(new InetSocketAddress(9494));

        //创建选择器
        Selector selector = Selector.open();

        //将通道注册到选择器上，指定  -》 OP_ACCEPT ： 监听接受事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //轮训式的获取选择器上的已经准备就绪的事件
        while(selector.select()>0){
            //获取所有选择器上已注册的所有选择键（已就绪的监听事件）
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            //所有channel事件
            while (iterator.hasNext()){
                 SelectionKey eventKey = iterator.next();
                 //判断具体是什么事件准备就绪
                 //接收事件就绪
                 if(eventKey.isAcceptable()){
                     //接收事件已就绪，获取对应客户端连接
                     SocketChannel socketChannel = serverSocketChannel.accept();
                     //设置非阻塞模式
                     serverSocketChannel.configureBlocking(false);
                     //将通道注册到选择器上
                     socketChannel.register(selector,SelectionKey.OP_READ);
                 }else if(eventKey.isReadable()){
                     //读取事件就绪
                     SocketChannel channel = (SocketChannel) eventKey.channel();
                     ByteBuffer buff = ByteBuffer.allocate(1024);
                     int len = 0;
                     while((len = channel.read(buff)) != -1){
                         buff.flip();
                         System.out.println("读取的数据："+new String(buff.array(),0, len));
                         buff.clear();
                     }

                 }
                 //删除事件key
                 iterator.remove();
            }
        }
    }









}
