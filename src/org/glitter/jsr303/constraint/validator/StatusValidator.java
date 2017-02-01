package org.glitter.jsr303.constraint.validator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.glitter.jsr303.constraint.Status;

public class StatusValidator implements ConstraintValidator<Status, String> {
	// TODO 这里的ALL_STATUS值应该从常量类中读取（或者枚举？再考虑），常量类（枚举类？）中的值应该从配置动态文件读取。
	// 可能用枚举更为合适，不知是否可行再研究吧。
	private final String[] ALL_STATUS = { "0", "1" };

	public void initialize(Status status) {
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean result = false;
		if (Arrays.asList(ALL_STATUS).contains(value)){
			result = true;
		}
		return result;
	}
}