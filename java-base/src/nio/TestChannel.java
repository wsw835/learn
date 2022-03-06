package nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author: wensw
 * @description: 了解NIO核心之Channel通道
 */
@SuppressWarnings("checkstyle:unchecked")
public class TestChannel {

    /**
     * 通过使用非直接缓冲区完成文件的复制
     */
    @Test
    public void testUnDirectAllocate() throws Exception{
        FileInputStream  in = new FileInputStream("channel.png");
        FileOutputStream  out = new FileOutputStream("channel_copy.png");
        FileChannel   fileChannelIn = in.getChannel();
        FileChannel   fileChannelOut = out.getChannel();
            //建立缓冲区,分配对应大小
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //缓冲区放入数据
            while (fileChannelIn.read(byteBuffer) != -1) {
                //切换数据读取方式
                byteBuffer.flip();
                fileChannelOut.write(byteBuffer);
                //读完就清空缓冲区内容
                byteBuffer.clear();
            }
        in.close();
        out.close();
        fileChannelOut.close();
        fileChannelIn.close();
    }


    /**
     * 通过使用直接缓冲区完成文件复制
     */
    @Test
    public void testDirectAllocate()  throws Exception{
        //创建通道
        FileChannel inChannel = FileChannel.open(Paths.get("channel.png"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("channel_copy.png"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        //MappedByteBuffer -> type: direct byte buffer
        MappedByteBuffer inMap = inChannel.map(FileChannel.MapMode.READ_ONLY,0, inChannel.size());
        MappedByteBuffer outMap = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
        byte[] cacheData = new byte[inMap.limit()];
        //数据放入缓冲区
        inMap.get(cacheData);
        outMap.put(cacheData);

        inChannel.close();
        outChannel.close();
    }


    /**
     * 通道之间的数据传输：
     * transferTo:
     * transferFrom:
     */
    @Test
    public void testChannelTransfer()  throws Exception{
        //创建通道
        FileChannel inChannel = FileChannel.open(Paths.get("channel.png"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("channel_copy.png"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        //inChannel.transferTo / outChannel.transferFrom
        inChannel.transferTo(0,inChannel.size(),outChannel);
        //outChannel.transferFrom(inChannel,0,inChannel.size());

        inChannel.close();
        outChannel.close();
    }

    /**
     * 分散和聚集
     */
    @Test
    public void testScatterGather() throws Exception{
        RandomAccessFile from = new RandomAccessFile("channel.png","rw");
        //获取通道
        FileChannel fileChannel = from.getChannel();
        //分配固定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(24);
        ByteBuffer buf2 = ByteBuffer.allocate(1000);
        //分散读取
        ByteBuffer[] byteBuffers = {buf1,buf2};
        fileChannel.read(byteBuffers);
        for(ByteBuffer by : byteBuffers){
            by.flip();
        }

        //聚集写入
        RandomAccessFile to = new RandomAccessFile("channel_copy.png","rw");
        //获取通道
        FileChannel fileChannel2 = to.getChannel();
        fileChannel2.write(byteBuffers);


        from.close();
        to.close();
        fileChannel.close();
        fileChannel2.close();
    }

    @Test
    public void testCharset(){
        SortedMap<String, Charset> charsetColl = Charset.availableCharsets();
        for(Map.Entry<String,Charset> charset:charsetColl.entrySet()){
            System.out.println("charset=> key: "+charset.getKey() + " value: "+charset.getValue());
        }
    }


    @Test
    public void testCharsetEd() throws Exception{
        Charset gbk = Charset.forName("GBK");
        //获取编码器
        CharsetEncoder charsetEncoder = gbk.newEncoder();
        //获取解码器
        CharsetDecoder charsetDecoder = gbk.newDecoder();

        CharBuffer buffer = CharBuffer.allocate(1024);
        buffer.put("测试字符集编码解码！");
        buffer.flip();

        //对缓冲区数据进行编码
        ByteBuffer encodeBuffer = charsetEncoder.encode(buffer);
        System.out.println("=======encode======");
        for(int i = 0 ;i<encodeBuffer.capacity();i++) {
            System.out.println(encodeBuffer.get());
        }

        encodeBuffer.flip();
        //对编码后的缓冲区数据进行解码
        CharBuffer decodeBuffer = charsetDecoder.decode(encodeBuffer);
        System.out.println("=======decode======");
        System.out.println(decodeBuffer.toString());

        //使用UTF-8解码
        encodeBuffer.flip();
        Charset utf8 = Charset.forName("UTF-8");
        CharsetDecoder charsetDecoder1 = utf8.newDecoder();
        CharBuffer decode = charsetDecoder1.decode(encodeBuffer);
        System.out.println("=======decode UTF8======");
        System.out.println(decode.toString());
    }


}
