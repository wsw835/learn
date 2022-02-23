package juc;

/**
 * @author: wensw
 * @description:

 * Java5.0在java.util.并发包中提供了多种并发容器类来改进同步容器的性能。

 * ConcurrentHashMap同步容器类是Java 5增加的一个线程安全的哈希表。对与多线程的操作，介于HashMap与Hashtable之间.
 * 内部采用“锁分段”机制替代Hashtable的独占锁.进而提高性能.
 *
 * 此包还提供了设计用于多线程上下文中的集合实现：
 *
 * ConcurrentHashMap、ConcurrentSkipListMap、ConcurrentSkipListSet、
 * CopyOnWriteArrayList和CopyOnWriteArraySet。
 *
 * 当期望许多线程访问一个给定集合时，ConcurrentHashMap通常优于同步的HashMap，

 * ConcurrentSkipListMap通常优于同步的TreeMap。
 *
 * 当期望的读数和遍历远远大于列表的更新数时，CopyOnWriteArrayList，优于同步的ArrayList。
 *
 */
public class TestConcurrentHashMap {

}
