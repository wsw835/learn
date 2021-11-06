package com.wensw.importSelector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

import java.util.Set;

/**
 * 通过返回的类名数组去注册相关bean进容器
 */
public class MyImportSelector implements ImportSelector {

    public String[] selectImports(AnnotationMetadata annotationMetadata) {

        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
        MergedAnnotations annotations = annotationMetadata.getAnnotations();
        String[] interfaceNames = annotationMetadata.getInterfaceNames();
        Set<MethodMetadata> annotatedMethods = annotationMetadata.getAnnotatedMethods("");
        String[] memberClassNames = annotationMetadata.getMemberClassNames();
        String superClassName = annotationMetadata.getSuperClassName();
        String className = annotationMetadata.getClassName();

        annotationTypes.stream().forEach(e -> {
            System.out.println("所有注解类别---》annotationTypes + " + e);
        });

        System.out.println("所有合并注解---》annotations + " + annotations);

        for (int i = 0; i < interfaceNames.length; i++) {
            System.out.println("所有接口名称---》interfaceName[i] + " + interfaceNames[i]);
        }

        annotatedMethods.stream().forEach(e -> {
            System.out.println("所有方法元数据---》annotatedMethods[i] + " + e);
        });

        for (int i = 0; i < memberClassNames.length; i++) {
            System.out.println("所有成员类---》memberClassNames[i] + " + memberClassNames[i]);
        }

        System.out.println("当前类超类---》superClassName + " + superClassName);
        System.out.println("当前类---》className + " + className);

        return new String[]{"com.wensw.entity.ImportT"};
    }


}
