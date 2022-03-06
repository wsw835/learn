package nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author: wensw
 * @description: 了解NIO核心非阻塞原理 - 阻塞式NIO
 */
public class TestBlockingNio {

    @Test
    public void blockedClient() throws Exception{
        //1.获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8181));
        //2.分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //3.将文件数据放入缓冲区
        FileChannel fileChannel = FileChannel.open(Paths.get("channel.png"), StandardOpenOption.READ);
        while(fileChannel.read(buffer) != -1){
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }

        //4.关闭通道
        socketChannel.close();
        fileChannel.close();
    }


    @Test
    public void blockedServer() throws Exception{
        //1、获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2、绑定客户端对应端口
        serverSocketChannel.bind(new InetSocketAddress(8181));
        FileChannel fileChannel = FileChannel.open(Paths.get("channel_copy.png"),StandardOpenOption.WRITE,StandardOpenOption.CREATE);

        //3、获取客户端连接通道
        SocketChannel clientChannel = serverSocketChannel.accept();

        //4、创建缓冲区大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //5、接收客户端的数据，保存至本地
        while (clientChannel.read(buffer) != -1){
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }

        //6、关闭连接
        serverSocketChannel.close();
        fileChannel.close();
        clientChannel.close();
    }



}
