package org.glitter.jsr303.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.glitter.jsr303.constraint.Price;
import org.hibernate.validator.constraints.NotEmpty;

public class Product {
	@NotEmpty(message="产品名称不能为空")
	private String productName;
	// 必须在 8000 至 10000 的范围内
	// @Price 是一个定制化的 constraint
	@Price
	private float price;

	/**
	 * 支持这样的校验，校验结果如下：
	 * ConstraintViolationImpl{interpolatedMessage='不能为空', propertyPath=product.productChilds[1].productChildName, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{org.hibernate.validator.constraints.NotBlank.message}'}, 
     * ConstraintViolationImpl{interpolatedMessage='最小不能小于8000', propertyPath=product.productChilds[1].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'}, 
	 * ConstraintViolationImpl{interpolatedMessage='最小不能小于8000', propertyPath=product.productChilds[0].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'},
	 */
	@Valid
	private List<ProductChild> productChilds;
	/**
	 * 支持这样的校验，校验结果如下：
	 * ConstraintViolationImpl{interpolatedMessage='不能为空', propertyPath=product.setProductChilds[].productChildName, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{org.hibernate.validator.constraints.NotBlank.message}'}, 
	 * ConstraintViolationImpl{interpolatedMessage='最小不能小于8000', propertyPath=product.setProductChilds[].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'}, 
	 * ConstraintViolationImpl{interpolatedMessage='最小不能小于8000', propertyPath=product.setProductChilds[].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'}]
	 */
	@Valid
	private Set<ProductChild> setProductChilds;
	/**
	 * 支持这样的校验，校验结果如下：
	 * [ConstraintViolationImpl{interpolatedMessage='不能为空', propertyPath=product.map[two].productChildName, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{org.hibernate.validator.constraints.NotBlank.message}'},
	 *  ConstraintViolationImpl{interpolatedMessage='最小不能小于8000', propertyPath=product.map[one].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'},
	 *  ConstraintViolationImpl{interpolatedMessage='最小不能小于8000', propertyPath=product.map[one].productChildPrice, rootBeanClass=class org.glitter.jsr303.model.Order, messageTemplate='{javax.validation.constraints.Min.message}'},
	 */
	@Valid
	private Map<String,ProductChild> map;
	
	// 即使maps不为空也不进行校验不知道为什么，如果非用这种结构不可，可以考虑循环list逐一map进行验证。
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
