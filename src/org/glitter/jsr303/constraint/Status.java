package org.glitter.jsr303.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.glitter.jsr303.constraint.validator.StatusValidator;

@Constraint(validatedBy = { StatusValidator.class })
@Documented
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Status {
	
	// 将0,1对应的中文描述动态从常量类中读取（或者枚举？再考虑）
	String message() default "取值不正确 , 应该是 '0', '1'其中之一";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	 //指定多个时使用
    @Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
    	Status[] value();
    }
}
