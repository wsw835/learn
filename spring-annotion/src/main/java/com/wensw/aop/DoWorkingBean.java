package com.wensw.aop;

public class DoWorkingBean {

    public DoWorkingBean() {

    }

    //业务处理
    public Integer doWork(int mutPly, int div) {
        return mutPly / div;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
