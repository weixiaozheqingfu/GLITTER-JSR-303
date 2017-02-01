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
	
	// ��0,1��Ӧ������������̬�ӳ������ж�ȡ������ö�٣��ٿ��ǣ�
	String message() default "ȡֵ����ȷ , Ӧ���� '0', '1'����֮һ";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	 //ָ�����ʱʹ��
    @Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
    	Status[] value();
    }
}
