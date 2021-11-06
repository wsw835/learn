package com.wensw.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class MyBeanFilter implements TypeFilter {

    /**
     * 设置自己的包扫描过滤
     *
     * @param metadataReader        元数据读取
     * @param metadataReaderFactory 元数据读取工厂
     * @return
     * @throws IOException
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        Resource resource = metadataReader.getResource();
        System.out.println("---->annotationMetadata  : " + annotationMetadata);
        System.out.println("---->classMetadata  : " + classMetadata);
        System.out.println("---->resource  : " + resource);
        String className = annotationMetadata.getClassName();
        System.out.println("元数据类获取到具体类名:====> " + className);
        if (className.contains("er")) {
            return true;
        }
        return false;
    }

}
