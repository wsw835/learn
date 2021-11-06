package com.wensw.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

//判断是否注册bean - > 张三
public class ZCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        System.out.println("上下文环境-->Z " + conditionContext);
        System.out.println("注释元数据信息---->Z" + annotatedTypeMetadata);
        return false;
    }

}
