package com.wensw.bean.properties;

import org.springframework.beans.factory.annotation.Value;

/**
 * bean实例属性赋值
 */
public class BeanProperties {

    @Value("测试属性设置")
    private String name;

    /**
     * 通过spring的SPEL表达式设置属性
     */
    @Value("#{20-2}")
    private Integer size;

    @Value("${beanProperties.init}")
    private Boolean initStartFlag;

    /**
     * 指定value时，会默认启用spring内部提供的类型转换方法将转换后的值设置为字段的属性值
     * 若转换时发生类型转换异常，则会将其抛出
     */
    @Value("0020")
    //org.springframework.beans.factory.UnsatisfiedDependencyException:
    // Error creating bean with name 'beanProperties':
    // Unsatisfied dependency expressed through field 'transcode';
    // nested exception is org.springframework.beans.TypeMismatchException:
    // Failed to convert value of type 'java.lang.String' to required type 'int';
    // nested exception is java.lang.NumberFormatException: For input string: "abc"
    private int transcode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getInitStartFlag() {
        return initStartFlag;
    }

    public void setInitStartFlag(Boolean initStartFlag) {
        this.initStartFlag = initStartFlag;
    }

    @Override
    public String toString() {
        return "BeanProperties{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", initStartFlag=" + initStartFlag +
                ", transcode=" + transcode +
                '}';
    }
}
