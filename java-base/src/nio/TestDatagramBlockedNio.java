package nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @author: wensw
 * @description: 了解 DatagramChannel
 */
public class TestDatagramBlockedNio {

    @Test
    public  void send() throws Exception {
         //获取通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        //设置非阻塞
        datagramChannel.configureBlocking(false);

        //建立缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //存放数据
        byteBuffer.put("DatagramChannel发送数据".getBytes());
        byteBuffer.flip();
        //发送数据
        datagramChannel.send(byteBuffer,new InetSocketAddress("127.0.0.1",9696));
        byteBuffer.clear();

        //关闭通道
        datagramChannel.close();
    }

    @Test
    public void receive() throws Exception {
        //获取通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        //设置非阻塞
        datagramChannel.configureBlocking(false);

        datagramChannel.bind(new InetSocketAddress(9696));

        //注册事件选择器
        Selector selector = Selector.open();
        datagramChannel.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                //如果是已经都就绪状态
                if (next.isReadable()) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    datagramChannel.receive(byteBuffer);
                    byteBuffer.flip();
                    System.out.println(new String(byteBuffer.array(), 0, byteBuffer.limit()));
                    byteBuffer.clear();
                }
            }
            iterator.remove();
        }
    }
}
