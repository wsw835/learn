package com.wensw;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( test());
    }

    private static String test(){
       int a= 5;
       try {
           throw new RuntimeException();
       }catch (Exception e){
           return "a="+a;
       }finally {
           a=7;
           System.out.println("a="+a);
       }
    }
}
