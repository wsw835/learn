package java.lang;

/**
 * @author: wensw
 * @description:
 */
public class String {

    static {
        System.out.println("我是自定义的String");
    }

    /**
     * 错误: 在类 java.lang.String 中找不到 main 方法, 请将 main 方法定义为:
     * public static void main(String[] args)
     * 否则 JavaFX 应用程序类必须扩展javafx.application.Application
     */
    public static void main(String[] args) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(contextClassLoader);
    }
}
