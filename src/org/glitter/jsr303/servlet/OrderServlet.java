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
			// TODO 再看裴老师吧
			ResourceBundle bundle = ResourceBundle.getBundle("ColumnMessages");
			// leafBean表示当前校验字段所属的那个bean
			// rootBean表示validator.validate(order);方法中输入参数传入的这个bean。
			// 做到这一步，如果同一个字段的信息想展示一行，就很容易了，办法有很多，例如可以用map封装返回信息key就是(leafBeanName +"."+propertyName)，然后把这一个字段的所有出错的校验拼接到一行就行了。
			// 当然这个事情不是在这个方法里做的，只是将注释写在这里而已。
			
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
	 * 如果propertyPath中包含中括号，则获取中括号中的内容
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
		// 从 request 中获取输入信息
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
		// 纯粹为了测试三级下的各种注解的支持情况
		List<ProductChild> productChilds = new ArrayList<ProductChild>();
		productChilds.add(productChild1);
		productChilds.add(productChild2);
		product.setProductChilds(productChilds);
		// 纯粹为了测试三级下的各种注解的支持情况
		Set<ProductChild> setProductChilds = new HashSet<ProductChild>();
		setProductChilds.add(productChild1);
		setProductChilds.add(productChild2);
		product.setSetProductChilds(setProductChilds);
		// 纯粹为了测试三级下的各种注解的支持情况
		Map<String,ProductChild> map = new HashMap<String,ProductChild>();
		map.put("one", productChild1);
		map.put("two", productChild2);
		product.setMap(map);
		// 纯粹为了测试三级下的各种注解的支持情况
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
		
		// 将 Bean 放入 session 中
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
		// 重要：特别注意：这里的Set集合是以注解为单位的，不是以字段为单位的，比如字体orgId中有两个注解校验不通过，那么这个集合中关于orgId的校验不通过记录就有两条。
//		Set<ConstraintViolation<Product>> violations2 = validator.validate(product);
		Set<ConstraintViolation<Order>> violations = validator.validate(order);
		if (violations.size() == 0) {
			session.setAttribute("order", null);
			session.setAttribute("errorMsg", null);
			resp.sendRedirect("creatSuccessful.jsp");
		} else {
			StringBuffer buf = new StringBuffer();
			
			// 假定现在的需求是如果字段status的值是0，则字段orderId执行标记为OrderStatus0Group的注解的校验，字段customer可以为空。
			// 假定现在的需求是如果字段status的值是1，则字段orderId执行标记为OrderStatus1Group的注解的校验
			boolean hasOrderStatus = false;
			System.out.println(violations);
			for (ConstraintViolation<Order> violation : violations) {
				String leafBeanName = violation.getLeafBean().getClass().getName();
				String rootBeanName = violation.getRootBean().getClass().getName();
				String propertyName = this.getPropertyName(violation);
				// 如果当前遍历到了Order对象的status字段的错误则不需要对受其影响的字段进行校验
				if((rootBeanName+".status").equals(leafBeanName+"."+propertyName)){
					hasOrderStatus = true;
				}
				buf.append(this.getPropertyMessage(violation));
				// 其他会影响其他字段的字段也是一样的...
			}
			
			// 如果Order对象的status字段校验通过，则根据其取值的不同，动态的进行受其影响的字段的校验
			if(!hasOrderStatus){
				buf = new StringBuffer();
				// 去除受其影响的字段的校验信息
				Iterator<ConstraintViolation<Order>> it = violations.iterator(); 
				boolean removeFlag = false;
				while(it.hasNext()) {
					ConstraintViolation<Order> violation = (ConstraintViolation<Order>)it.next();
					
					String property = this.getPropertyName(violation);
					String leafBeanName = violation.getLeafBean().getClass().getName();
					String rootBeanName = violation.getRootBean().getClass().getName();
				
					// 下面是跟业务相关的代码
					// status字段通过校验了有了正确的取值了，那么如果orderId之前有默认的校验没通过，也将其删掉，重新进行status影响下的完整校验，如果之前有默认校验通过了或者之前没校验，那更好，不用管了，以后直接进行新校验即可。
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
				// 进行新的校验
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
