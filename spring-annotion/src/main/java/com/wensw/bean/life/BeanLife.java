package com.wensw.bean.life;

public class BeanLife {

    private String name;

    private String stage;

    public BeanLife() {
        System.out.println("对象创建");
    }

    public BeanLife(String name, String stage) {
        this.name = name;
        this.stage = stage;
    }

    public void init() {
        System.out.println("对象属性设置完成之后，调用该初始化方法进行修饰...");
        this.stage = stage + "---后--修饰一番";
        System.out.println("初始化" + toString());
        System.out.println("bean init");
    }

    public void destroy() {
        System.out.println("在容器即将关闭之前，查看当前bean的状态...");
        System.out.println(toString());
        System.out.println("before context close and bean destroy");
    }

    @Override
    public String toString() {
        return "BeanLife{" +
                "name='" + name + '\'' +
                ", stage='" + stage + '\'' +
                '}';
    }
}
