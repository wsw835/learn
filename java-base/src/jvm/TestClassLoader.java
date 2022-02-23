package jvm;

import java.net.URL;
import java.security.Provider;

/**
 * @author: wensw
 * @description:
 */
public class TestClassLoader {

    public static void main(String[] args) {
        //系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);//sun.misc.Launcher$AppClassLoader@14dad5dc

        //系统类加载器的上层 - 》 扩展类加载器
        ClassLoader parent = systemClassLoader.getParent();
        System.out.println(parent);//sun.misc.Launcher$ExtClassLoader@1540e19d

        //扩展类加载器的上层 - 》 ？获取不到引导类加载器
        ClassLoader bootStrapLoad = parent.getParent();
        System.out.println(bootStrapLoad); //null

        //用户自定义的类加载器  - 默认使用系统类加载器加载
        ClassLoader classLoader = TestClassLoader.class.getClassLoader();
        System.out.println(classLoader);//sun.misc.Launcher$AppClassLoader@14dad5dc

        //String类的加载器 - 使用引导类加载器加载 -》JAVA的核心类库都是使用引导类加载器加载的
        ClassLoader strClassLoader = String.class.getClassLoader();
        System.out.println(strClassLoader); //null


        System.out.println("-------启动类加载器-------");
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urls) {
            System.out.println(url.toExternalForm());
        }

        System.out.println("-------核心类Provider的类加载器（引导类加载器）-------");
        ClassLoader provider = Provider.class.getClassLoader();
        System.out.println(provider);

        System.out.println("-------扩展类加载器-------");
        String extDirs = System.getProperty("java.ext.dirs");
        System.out.println(extDirs);
        for (String path : extDirs.split(";")) {
            System.out.println(path);
        }


    }


}
