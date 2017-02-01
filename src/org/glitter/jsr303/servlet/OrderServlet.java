package org.glitter.jsr303.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.glitter.jsr303.constraint.group.column.OrderStatusGroup.OrderStatus0Group;
import org.glitter.jsr303.constraint.group.column.OrderStatusGroup.OrderStatus1Group;
import org.glitter.jsr303.model.Order;
import org.glitter.jsr303.model.Product;
import org.glitter.jsr303.model.ProductChild;

public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OrderServlet() {
		super();
	}

	@SuppressWarnings("rawtypes")
	private <T> String validateProperty(T bean,String propertyName, Class... arg2){
		String result = null;
		if(bean==null || null==propertyName || "".equals(propertyName)){
			return null;
		}
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violationOrderIdSet = validator.validateProperty(bean, propertyName,arg2);
		Iterator<ConstraintViolation<T>> it = (violationOrderIdSet!=null && violationOrderIdSet.size()>0)?violationOrderIdSet.iterator():null;
		if(it!=null){
			StringBuffer buf = new StringBuffer();
			while(it.hasNext()) {
				buf.append(this.getPropertyMessage(it==null?null:it.next()));
			}
			result = buf.toString();
		}
		return result;
	}
	
	private <T> String getPropertyName(ConstraintViolation<T> violation){
		String propertyPath = violation==null?null:violation.getPropertyPath().toString();
		String propertyName = (null==propertyPath||"".equals(propertyPath))?null:(propertyPath.substring(propertyPath.lastIndexOf(".")<0?0:propertyPath.lastIndexOf(".")+1));
		return propertyName;
	}
	
	private <T> String getPropertyMessage(ConstraintViolation<T> violation){
		String result = null;
		if(violation!=null){
			StringBuffer buf = new StringBuffer();
			String propertyName = this.getPropertyName(violation);
			String leafBeanName = violation.getLeafBean().getClass().getName();
			// TODO �ٿ�����ʦ��
			ResourceBundle bundle = ResourceBundle.getBundle("ColumnMessages");
			// leafBean��ʾ��ǰУ���ֶ��������Ǹ�bean
			// rootBean��ʾvalidator.validate(order);���������������������bean��
			// ������һ�������ͬһ���ֶε���Ϣ��չʾһ�У��ͺ������ˣ��취�кܶ࣬���������map��װ������Ϣkey����(leafBeanName +"."+propertyName)��Ȼ�����һ���ֶε����г����У��ƴ�ӵ�һ�о����ˡ�
			// ��Ȼ������鲻����������������ģ�ֻ�ǽ�ע��д��������ѡ�
			
			String propertyPathMiddleBracketValue = this.getPropertyPathMiddleBracketValue(violation.getPropertyPath().toString());
			buf.append(leafBeanName +"."+propertyName);
			buf.append(propertyPathMiddleBracketValue==null?"":"["+propertyPathMiddleBracketValue+"]");
			buf.append("-");
			buf.append(bundle.getString(leafBeanName +"."+propertyName));
			buf.append("-");
			buf.append(violation.getMessage());
			buf.append("<BR>\n");
			result = buf.toString();
		}
		return result;
	}
	
	/**
	 * ���propertyPath�а��������ţ����ȡ�������е�����
	 */
	private String getPropertyPathMiddleBracketValue(String propertyPath){
		String result = null;
		if(null!=propertyPath && !"".equals(propertyPath)){
			if(propertyPath.contains(".") && propertyPath.length()>1){
				String propertyPathSub = propertyPath.substring(0, propertyPath.lastIndexOf("."));
				String propertyPathSub2 = propertyPathSub;
				if(propertyPathSub.contains(".")){
					propertyPathSub2 = propertyPathSub.substring(propertyPathSub.lastIndexOf(".")+1);
				}
				if(propertyPathSub2.contains("[") && propertyPathSub2.contains("]") && propertyPathSub2.endsWith("]")){
					result = propertyPathSub2.substring(propertyPathSub2.indexOf("[")+1,propertyPathSub2.indexOf("]"));
				};
			}
		}
		return result;
	}
	
	private <T> Object getRootBean(Set<ConstraintViolation<T>> violations){
		Object rootBean = null;
		ConstraintViolation<T> violation = (violations!=null && violations.size()>0)?violations.iterator().next():null;
		rootBean = violation==null?null:violation.getRootBean();
		return rootBean;
	}
	
	@SuppressWarnings("unused")
	private <T> Object getLeafBean(Set<ConstraintViolation<T>> violations){
		Object leafBean = null;
		ConstraintViolation<T> violation = (violations!=null && violations.size()>0)?violations.iterator().next():null;
		leafBean = violation==null?null:violation.getLeafBean();
		return leafBean;
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		// �� request �л�ȡ������Ϣ
		String orderId = (String) req.getParameter("orderId");
		String customer = (String) req.getParameter("customer");
		String email = (String) req.getParameter("email");
		String address = (String) req.getParameter("address");
		String status = (String) req.getParameter("status");
		String productName = (String) req.getParameter("productName");
		String price = (String) req.getParameter("price");
		
		Product product = new Product();
		product.setProductName(productName);
		if (price != null && price.length() > 0){
			product.setPrice(Float.valueOf(price));
		}
		
		ProductChild productChild1 = new ProductChild();
		productChild1.setProductChildName("productChildName1");
		productChild1.setProductChildPrice(5);
		ProductChild productChild2 = new ProductChild();
		productChild2.setProductChildPrice(6.00f);
		// ����Ϊ�˲��������µĸ���ע���֧�����
		List<ProductChild> productChilds = new ArrayList<ProductChild>();
		productChilds.add(productChild1);
		productChilds.add(productChild2);
		product.setProductChilds(productChilds);
		// ����Ϊ�˲��������µĸ���ע���֧�����
		Set<ProductChild> setProductChilds = new HashSet<ProductChild>();
		setProductChilds.add(productChild1);
		setProductChilds.add(productChild2);
		product.setSetProductChilds(setProductChilds);
		// ����Ϊ�˲��������µĸ���ע���֧�����
		Map<String,ProductChild> map = new HashMap<String,ProductChild>();
		map.put("one", productChild1);
		map.put("two", productChild2);
		product.setMap(map);
		// ����Ϊ�˲��������µĸ���ע���֧�����
		Map<String,ProductChild> map2 = new HashMap<String,ProductChild>();
		List<Map<String,ProductChild>> maps = new ArrayList<Map<String,ProductChild>>();
		map2.put("yi", productChild1);
		map2.put("er", productChild2);
		Map<String,ProductChild> map3 = new HashMap<String,ProductChild>();
		map3.put("san", productChild1);
		map3.put("si", productChild2);
		maps.add(map2);
		maps.add(map3);
		product.setMaps(maps);
		
		// �� Bean ���� session ��
		Order order = new Order();
		order.setOrderId(orderId);
//		order.setOrderId(null);
		order.setCustomer(customer);
		order.setEmail(email);
		order.setAddress(address);
		order.setStatus(status);
		order.setCreateDate(new Date());
//		order.setIntegerTest(1);
//		order.setIntTest(1);
//		order.setBooleanFengzhuangTest(true);
//		order.setBooleanJiandanTest(true);
		order.setProduct(product);
		session.setAttribute("order", order);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<Order>> violations = validator.validate(order,GroupSort.class,OrderGroupA.class);
//		Set<ConstraintViolation<Order>> violations = validator.validate(order,OrderGroupA.class);
		// ��Ҫ���ر�ע�⣺�����Set��������ע��Ϊ��λ�ģ��������ֶ�Ϊ��λ�ģ���������orgId��������ע��У�鲻ͨ������ô��������й���orgId��У�鲻ͨ����¼����������
//		Set<ConstraintViolation<Product>> violations2 = validator.validate(product);
		Set<ConstraintViolation<Order>> violations = validator.validate(order);
		if (violations.size() == 0) {
			session.setAttribute("order", null);
			session.setAttribute("errorMsg", null);
			resp.sendRedirect("creatSuccessful.jsp");
		} else {
			StringBuffer buf = new StringBuffer();
			
			// �ٶ����ڵ�����������ֶ�status��ֵ��0�����ֶ�orderIdִ�б��ΪOrderStatus0Group��ע���У�飬�ֶ�customer����Ϊ�ա�
			// �ٶ����ڵ�����������ֶ�status��ֵ��1�����ֶ�orderIdִ�б��ΪOrderStatus1Group��ע���У��
			boolean hasOrderStatus = false;
			System.out.println(violations);
			for (ConstraintViolation<Order> violation : violations) {
				String leafBeanName = violation.getLeafBean().getClass().getName();
				String rootBeanName = violation.getRootBean().getClass().getName();
				String propertyName = this.getPropertyName(violation);
				// �����ǰ��������Order�����status�ֶεĴ�������Ҫ������Ӱ����ֶν���У��
				if((rootBeanName+".status").equals(leafBeanName+"."+propertyName)){
					hasOrderStatus = true;
				}
				buf.append(this.getPropertyMessage(violation));
				// ������Ӱ�������ֶε��ֶ�Ҳ��һ����...
			}
			
			// ���Order�����status�ֶ�У��ͨ�����������ȡֵ�Ĳ�ͬ����̬�Ľ�������Ӱ����ֶε�У��
			if(!hasOrderStatus){
				buf = new StringBuffer();
				// ȥ������Ӱ����ֶε�У����Ϣ
				Iterator<ConstraintViolation<Order>> it = violations.iterator(); 
				boolean removeFlag = false;
				while(it.hasNext()) {
					ConstraintViolation<Order> violation = (ConstraintViolation<Order>)it.next();
					
					String property = this.getPropertyName(violation);
					String leafBeanName = violation.getLeafBean().getClass().getName();
					String rootBeanName = violation.getRootBean().getClass().getName();
				
					// �����Ǹ�ҵ����صĴ���
					// status�ֶ�ͨ��У����������ȷ��ȡֵ�ˣ���ô���orderId֮ǰ��Ĭ�ϵ�У��ûͨ����Ҳ����ɾ�������½���statusӰ���µ�����У�飬���֮ǰ��Ĭ��У��ͨ���˻���֮ǰûУ�飬�Ǹ��ã����ù��ˣ��Ժ�ֱ�ӽ�����У�鼴�ɡ�
					if(violation.getRootBean().getStatus().equals("0") && 
						(  (rootBeanName+".orderId").equals(leafBeanName+"."+property) || (rootBeanName+".customer").equals(leafBeanName+"."+property)  )  
					){
						removeFlag = true;
					}else if(violation.getRootBean().getStatus().equals("1") && (rootBeanName+".orderId").equals(leafBeanName+"."+property)  ){
						removeFlag = true;
					}
					if(removeFlag){
						it.remove();
						removeFlag = false;
					}else{
						buf.append(this.getPropertyMessage(violation));
					}
				}
				// �����µ�У��
				Order o = (Order)this.getRootBean(violations);
				if(o.getStatus().equals("0")){
					buf.append(this.validateProperty(order, "orderId", OrderStatus0Group.class));
					buf.append(this.validateProperty(order, "customer", OrderStatus0Group.class));
				}else if(o.getStatus().equals("1")){
					buf.append(this.validateProperty(order, "orderId", OrderStatus1Group.class));
				}
			}
			session.setAttribute("errorMsg", buf.toString());
			resp.sendRedirect("createOrder.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
