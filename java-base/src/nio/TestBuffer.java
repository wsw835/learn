package nio;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author: wensw
 * @description: 了解NIO核心之Buffer缓冲区
 */
public class TestBuffer {

    public static void main(String[] args) {
        //调用allocate方法创建缓冲池
        ByteBuffer byteBuffer =  ByteBuffer.allocate(1024);
        System.out.println("----buffer---init");
        System.out.println("buffer.position: "+byteBuffer.position());
        System.out.println("buffer.limit: "+byteBuffer.limit());
        System.out.println("byteBuffer.capacity: "+byteBuffer.capacity());

        String data = "abcdef";
        //往缓冲区里放数据, position 会指向当前数据最后一个元素后面一个位置对应的下标：即 data.length
        byteBuffer.put(data.getBytes());
        System.out.println("----buffer---put");
        System.out.println("buffer.position: "+byteBuffer.position());
        System.out.println("buffer.limit: "+byteBuffer.limit());
        System.out.println("byteBuffer.capacity: "+byteBuffer.capacity());

        //切换为读数据模式，position指向0下标，limit指向当前数据最后一个元素后面一个位置对应的下标：即 data.length
        byteBuffer.flip();
        System.out.println("----buffer---flip");
        System.out.println("buffer.position: "+byteBuffer.position());
        System.out.println("buffer.limit: "+byteBuffer.limit());
        System.out.println("byteBuffer.capacity: "+byteBuffer.capacity());


        //从缓冲区取数据，position、limit均指向当前数据最后一个元素后面一个位置对应的下标：即 data.length
        byte[] readData = new byte[byteBuffer.limit()];
        byteBuffer.get(readData);
        System.out.println("----buffer---get");
        System.out.println("buffer.position: "+byteBuffer.position());
        System.out.println("buffer.limit: "+byteBuffer.limit());
        System.out.println("byteBuffer.capacity: "+byteBuffer.capacity());


        //切换为可重复读模式，position -> 0 ,limit -> data.length
        byteBuffer.rewind();
        System.out.println("----buffer---rewind");
        System.out.println("buffer.position: "+byteBuffer.position());
        System.out.println("buffer.limit: "+byteBuffer.limit());
        System.out.println("byteBuffer.capacity: "+byteBuffer.capacity());


        //清空缓冲区，缓冲区的数据依旧还在，只不过处于被遗忘状态，无法感知数据的范围
        byteBuffer.clear();
        System.out.println("----buffer---clear");
        System.out.println("buffer.position: "+byteBuffer.position());
        System.out.println("buffer.limit: "+byteBuffer.limit());
        System.out.println("byteBuffer.capacity: "+byteBuffer.capacity());

    }

    @Test
    public void testMarkReset(){
        ByteBuffer byteBuffer =  ByteBuffer.allocate(1024);
        String data = "abcdef";
        //往缓冲区里放数据
        byteBuffer.put(data.getBytes());
        //切换读数据模式
        byteBuffer.flip();
        //获取指定下标数据，并使用mark标记
        byte[] readData3 = new byte[byteBuffer.limit()];
        byteBuffer.get(readData3,0,3);
        //标记当前position位置 3
        byteBuffer.mark();
        System.out.println("----buffer---mark");
        System.out.println("buffer.position: "+byteBuffer.position());
        System.out.println("buffer.limit: "+byteBuffer.limit());
        System.out.println("byteBuffer.capacity: "+byteBuffer.capacity());

        //获取指定下标数据
        byte[] readData1 = new byte[byteBuffer.limit()];
        byteBuffer.get(readData1,4,2);
        System.out.println("buffer.position: "+byteBuffer.position());
        System.out.println("buffer.limit: "+byteBuffer.limit());
        System.out.println("byteBuffer.capacity: "+byteBuffer.capacity());

        //重置对应读下标，恢复到mark的位置 5
        byteBuffer.reset();
        System.out.println("----buffer---reset");
        System.out.println("buffer.position: "+byteBuffer.position());
        System.out.println("buffer.limit: "+byteBuffer.limit());
        System.out.println("byteBuffer.capacity: "+byteBuffer.capacity());

        //如果还剩下数据，查看剩余数据的数据长度
        if(byteBuffer.hasRemaining()){
            System.out.println("剩余数据长度： "+byteBuffer.remaining());
        }
    }

    @Test
    public void testAllocateAboutDirect(){
        //new HeapByteBuffer(capacity, capacity)
        ByteBuffer byteBuffer =  ByteBuffer.allocate(1024);
        //  boolean pa = VM.isDirectMemoryPageAligned(); //直接内存页标记
        //         int ps = Bits.pageSize();
        //         long size = Math.max(1L, (long)cap + (pa ? ps : 0));
        //         Bits.reserveMemory(size, cap);
        //
        //         long base = 0;
        //         try {
        //             base = unsafe.allocateMemory(size); //开辟内存空间
        //         } catch (OutOfMemoryError x) {
        //             Bits.unreserveMemory(size, cap);
        //             throw x;
        //         }
        //         unsafe.setMemory(base, size, (byte) 0);//设置内存大小
        ByteBuffer byteBufferDirect =  ByteBuffer.allocateDirect(1024);
        System.out.println(byteBuffer.isDirect());
        System.out.println(byteBufferDirect.isDirect());

    }
}
