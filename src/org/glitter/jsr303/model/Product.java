package org.glitter.jsr303.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.glitter.jsr303.constraint.Price;
import org.hibernate.validator.constraints.NotEmpty;

public class Product {
	@NotEmpty(message="��Ʒ���Ʋ���Ϊ��")
	private String productName;
	// ������ 8000 �� 10000 �ķ�Χ��
	// @Price ��һ�����ƻ��� constraint
	@Price
	private float price;

	/**
	 * ֧��������У�飬У�������£�
	 * ConstraintViolationImpl{interpolatedMessage='����Ϊ��', propertyPath=product.productChilds[1].productChildName, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{org.hibernate.validator.constraints.NotBlank.message}'}, 
     * ConstraintViolationImpl{interpolatedMessage='��С����С��8000', propertyPath=product.productChilds[1].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'}, 
	 * ConstraintViolationImpl{interpolatedMessage='��С����С��8000', propertyPath=product.productChilds[0].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'},
	 */
	@Valid
	private List<ProductChild> productChilds;
	/**
	 * ֧��������У�飬У�������£�
	 * ConstraintViolationImpl{interpolatedMessage='����Ϊ��', propertyPath=product.setProductChilds[].productChildName, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{org.hibernate.validator.constraints.NotBlank.message}'}, 
	 * ConstraintViolationImpl{interpolatedMessage='��С����С��8000', propertyPath=product.setProductChilds[].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'}, 
	 * ConstraintViolationImpl{interpolatedMessage='��С����С��8000', propertyPath=product.setProductChilds[].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'}]
	 */
	@Valid
	private Set<ProductChild> setProductChilds;
	/**
	 * ֧��������У�飬У�������£�
	 * [ConstraintViolationImpl{interpolatedMessage='����Ϊ��', propertyPath=product.map[two].productChildName, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{org.hibernate.validator.constraints.NotBlank.message}'},
	 *  ConstraintViolationImpl{interpolatedMessage='��С����С��8000', propertyPath=product.map[one].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'},
	 *  ConstraintViolationImpl{interpolatedMessage='��С����С��8000', propertyPath=product.map[one].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'},
	 */
	@Valid
	private Map<String,ProductChild> map;
	
	// ��ʹmaps��Ϊ��Ҳ������У�鲻֪��Ϊʲô������������ֽṹ���ɣ����Կ���ѭ��list��һmap������֤��
	@Valid
	private List<Map<String,ProductChild>> maps;
	
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<ProductChild> getProductChilds() {
		return productChilds;
	}

	public void setProductChilds(List<ProductChild> productChilds) {
		this.productChilds = productChilds;
	}

	public Set<ProductChild> getSetProductChilds() {
		return setProductChilds;
	}

	public void setSetProductChilds(Set<ProductChild> setProductChilds) {
		this.setProductChilds = setProductChilds;
	}

	public Map<String, ProductChild> getMap() {
		return map;
	}

	public void setMap(Map<String, ProductChild> map) {
		this.map = map;
	}

	public List<Map<String, ProductChild>> getMaps() {
		return maps;
	}

	public void setMaps(List<Map<String, ProductChild>> maps) {
		this.maps = maps;
	}
}
