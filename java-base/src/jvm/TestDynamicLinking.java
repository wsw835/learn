package jvm;

/**
 * @author: wensw
 * @description:
 */
public class TestDynamicLinking {

    int num;

    private void test1() {
        num++;
    }

    private void test2() {
        test1();
    }

    public void animalEat(Animal animal) {
        animal.eat();//动态链接（晚期绑定）
    }


    public void hunterHunt(Hunter hunter) {
        hunter.hunt();//动态链接（晚期绑定）
    }

}

abstract class Animal {
    abstract void eat();
}

interface Hunter {
    void hunt();
}

class Dog extends Animal implements Hunter {

    @Override
    void eat() {
        System.out.println("吃骨头");
    }

    @Override
    public void hunt() {
        System.out.println("抓老鼠");
    }
}

class Cat extends Animal implements Hunter {

    Cat() {
        super();
    }

    Cat(String s) {
        this();
    }

    @Override
    void eat() {
        System.out.println("吃老鼠");
    }

    @Override
    public void hunt() {
        System.out.println("抓老鼠");
    }
}