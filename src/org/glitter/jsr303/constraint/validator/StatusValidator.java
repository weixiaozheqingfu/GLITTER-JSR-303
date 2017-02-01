package org.glitter.jsr303.constraint.validator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.glitter.jsr303.constraint.Status;

public class StatusValidator implements ConstraintValidator<Status, String> {
	// TODO �����ALL_STATUSֵӦ�ôӳ������ж�ȡ������ö�٣��ٿ��ǣ��������ࣨö���ࣿ���е�ֵӦ�ô����ö�̬�ļ���ȡ��
	// ������ö�ٸ�Ϊ���ʣ���֪�Ƿ�������о��ɡ�
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