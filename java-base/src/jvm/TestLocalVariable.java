package jvm;

import java.util.Date;

/**
 * @author: wensw
 * @description: 查看栈内对应局部变量表相关信息
 */
public class TestLocalVariable {

    public int count;

    public TestLocalVariable() {

    }

    public TestLocalVariable(int count) {
        this.count = count;
    }

    public static void main(String[] args) {
        TestLocalVariable test = new TestLocalVariable();
        TestLocalVariable.test1();
        int num = 0;
    }

    public static void test1() {
        TestLocalVariable testLocalVariable = new TestLocalVariable();
        int num = 0;
        //64位占用两个Slot
        double a = 0;
        Date date = new Date();
        System.out.println(date);
    }

    public void test2() {
        count++;
    }

    public void test3() {
        Date date = new Date();
        int count = 123;
        double a = 123;
        TestLocalVariable testLocalVariable = new TestLocalVariable(123);
    }

    public void test4() {
        int a = 0;
        {
            int b = a;
            b = b + 1;
        }
        int c = 2;
    }
}
