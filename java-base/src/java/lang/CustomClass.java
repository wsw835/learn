package java.lang;

/**
 * @author: wensw
 * @description:
 */
public class CustomClass {

    /**
     * java.lang.SecurityException: Prohibited package name: java.lang
     * at java.lang.ClassLoader.preDefineClass(ClassLoader.java:659)
     * at java.lang.ClassLoader.defineClass(ClassLoader.java:758)
     * at java.security.SecureClassLoader.defineClass(SecureClassLoader.java:142)
     * at java.net.URLClassLoader.defineClass(URLClassLoader.java:467)
     * at java.net.URLClassLoader.access$100(URLClassLoader.java:73)
     * at java.net.URLClassLoader$1.run(URLClassLoader.java:368)
     * at java.net.URLClassLoader$1.run(URLClassLoader.java:362)
     * at java.security.AccessController.doPrivileged(Native Method)
     * at java.net.URLClassLoader.findClass(URLClassLoader.java:361)
     * at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
     * at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:331)
     * at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
     * at sun.launcher.LauncherHelper.checkAndLoadMain(LauncherHelper.java:495)
     * Error: A JNI error has occurred, please check your installation and try again
     * Exception in thread "main"
     */
    public static void main(String[] args) {
        System.out.println("核心包下面的自定义的类");//安全检查
    }

}
