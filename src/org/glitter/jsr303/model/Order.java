package org.glitter.jsr303.model;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.glitter.jsr303.constraint.Status;
import org.glitter.jsr303.constraint.group.column.OrderStatusGroup.OrderStatus0Group;
import org.glitter.jsr303.constraint.group.column.OrderStatusGroup.OrderStatus1Group;
import org.glitter.jsr303.constraint.group.method.OrderGroup.OrderGroupA;
import org.glitter.jsr303.constraint.group.method.OrderGroup.OrderGroupB;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class Order {
	/**
	   策略： 1.没有特殊需求的情况下，给每一个字段都加上默认的注解，即没有指定groups的注解。
	       2.如果在执行某些方法时，对某些字段有特殊需求，就需要在字段注解上再另外多加一个带有groups的注解，注意，除了特殊需求的字段需要额外加之外，其他字段也需要加，哪怕是跟不带groups的注解一模一样的也要加，
	         因为传入参数时一旦传入了group，那么所有字段都会去找带有这个group注解的进行校验，也就是说一旦某几个字段有特殊需求，需要全盘验证的字段都加上group，只不过其他字段多加的这个注解是在没有groups注解的基础上指定一下group，
	         而有特殊需求的，除了指定group之外还要根据需求写上合适的另外的特殊的校验规则。比如不指定group时是Size(max=8)，而指定了group的，可能特殊需求就是Size(max=10)了，就是这个意思。
           3.如果对校验顺序有要求，我们还可以指定字段的校验顺序，使用@GroupSequence定义顺序，具体参考http://blog.csdn.net/gaoshanliushui2009/article/details/50667017博客，这里需要说明的是，一般情况下我们不需要关心校验顺序，
	         因为最终的校验结果，不论是我们直接调用还是借助Spring，最终得到的错误信息集合中我们都能拿到字段名称和字段校验错误信息，这样在返回到前台的时候已经可以很好的做好对应了。
	         并且这里的@GroupSequence有一个弊端，就是用了它，其实也相当于用了一个分组了，那么就不能再有其他分组了，一旦混用，那么这个顺序可能就乱了，比如validator.validate(order,GroupSort.class,OrderGroupA.class);
	         这个方法是说只要注解中标记了GroupSort.class(由于@GroupSequence的关系实际上是n多GroupSortn.class)或者OrderGroupA.class的注解(只要标记其中之一)都将被校验，这样的话有的注解可能只标记了OrderGroupA.class也会校验，此时校验框架就不一定按顺序来了。
	         而如果是validator.validate(order,GroupSort.class);这样的调用方式的话，顺序是起作用的，但是此时OrderGroupA.class或OrderGroupB.class就不起作用了，顾此失彼了，并且及时不考虑OrderGroupA.class或OrderGroupB.class，此时的排序起作用的结果是
	         按照约定的顺序进行校验，一旦遇到校验失败的字段就停止，不往下继续校验了，所以得到的结果永远都只有一个，这可能也有需求的场景，但很少，一般情况下，最好的用户体验是一次性把所有的不合法信息全部返回给用户，(毕竟还有前台校验也可以起些作用)
	         退一万步，及时后端校验也必须一个一个来，一个一个不通过就返回那一个结果，那也不用这种方式，我们会考虑用其他方式来进行(需求极特殊了，比如用Spring的validate校验框架，不用jsr303了)
	         说了这么多，总结一下，顺序校验的事情知道有这么回事就行了，我们不会去用它，分析完了就把它忘了吧。
	
	       4.如果某个字段的校验依赖于其他字段的取值，那么此时，我们就需要在规则校验完成后，再进行代码微调了，最好的方式就是使用SpringAOP，这样即使需要写一些验证代码，也是可以和具体业务代码分离的，保证代码的干净。
	         注意这时就不需要一套了，只需要受其他字段影响的那个字段自己加上自己所有想要的特色(注意不同的注解根据需要加全，不要有遗漏，直接这个字段上要写小的一套校验注解)就可以了，因为处理的时候也是单独处理它自己的，跟其他字段无关了。
	         不过一般这种情况下这个受其他字段影响的校验其实也不用其他配套校验了，他就只有自己的那一套独立的受其他字段影响的那个分组校验即可，其他校验都不需要写了。
	
	       5.jsr303的校验是这样的：不管一个字段上加了多少个注解，只要这个字段是封装类型或引用类型的(比如用@Valid注解的引用类型)，并且字段值为null时就不进行任何校验了，而非null时才进行各种注解的校验，这是合理的，都为null了还校验什么呀。
	         当然如果需求是这个字段的值非空可以通过加上@NotNull/@NotBlank/@NotEmpty校验注解，这个时候如果字段输入参数值是null，那么这个校验就不能通过了(其他注解自然不会校验)，如果输出参数值不为null，会校验这个字段上的所有注解，所有的校验不同过信息都会记录下来。
	         由上得到下面几条原则：
	       5-1.理论上我们的字段既可以使用封装类型也可以使用简单类型，但是如果我们要使用jsr303校验，则最好并且必须使用封装类型，因为用简单类型容易造成误判，简单类型如果不赋值是会有默认值的，这是比较不妥的，正是因为这一点，
	                             特性，其实不光是jsr303，其他地方也是，都不推荐在实体中使用简单类型，而都使用封装类型代替，避免混淆。比如int类型，如果从前台输入值是空的，则注入后的值就是0，而实际上这个0一旦入库可能就很危险，他可能会被认为是用户录入的值。
	                             比如boolean如果前端输入是空的话，则注入时默认值为false，这时，我们就会认为这时前端传过来的值，岂不危险！具体到jsr303，如果是简单类型，前端输入是空的，但是注入时为空jdk会赋予默认值，这样的话简单类型的值永远不会为null，
	                             也就是说即便这个数据是不可靠的数据（默认数据，其实前端输入的是空），也会进行jsr303校验，这样就可能会给用户返回错误的验证信息。所以我们要用封装类型，不用简单类型。
	       5-2.同理，对于String，String不是简单类型，但有类似的道理，具体场景是这样的，如果前端页面的某个字段的值用户没有录入数据，而这个字段在bean中的类型是String的话，就会有默认注入值""，其实用户就是没有录入数据嘛，我们完全可以
	       用null值，而不使用这个""值，使用null值自然有使用null值的好处，不使用""自然也有不使用""的好处，使用null值的好处是与其他封装类型保持一致，没有输入值统一在数据库中都存入null，并且如果是null值是不存于jsr303验证的，如果要去非null，
	       我们可以用@NotNull/@NotBlank/@NotEmpty等校验注解，总之如果是null，我们jsr303对这个字段的校验是全覆盖的，不管是什么情况，都能进行校验，而如果默认值是""，事情就复杂了，要知道""也是有数据的，并不是null，那么jsr303就要对这个字段进行
	       验证了，比如注解是@Email，而很明显""不是@Email的正确格式，自然就不能通过验证了，而我们的需求又是用户对这个字段可以不录入数据，但如果录入数据就必须是@Email正确的格式，是不是傻眼了，用""臣妾办不到啊！哈哈！
	      但是如果默认值是null就不一样了，用户前端不录入数据，注入封装到bean中的默认值是null的话，就不会进行验证了，如果输入值了才会进行@Email验证，完美满足要求。
	       5-3.由5-1和5-2可以看出我们必须保证字段的默认数据是我们可控的，对于5-1，简单，我们只要对字段定义为封装类型，注入时如果前端传的是空值，自然注入的都是null没什么说的，用封装类型问题就解决了。
	                             但是对于5-2，对于String类型的字段，如果前端没有录入数据，输入数据经过注入的默认值会是""，我们现在要做的事情就是改变这个默认值，将它修改为null，一般情况下我们使用的都是SpringMVC框架，我们就讨论一下SpringMVC框架下
	                             如何改变String的注入值。
	                             两种方法，1.给String类型的字段的setter方法增加判断,所有是""的全部替换成null，这种方式有点极端，意味着在程序中任何位置调用setter方法都不能赋""值了，而我们的目的仅仅是前端传过来的数据不能是""，亦或你会想用给字段
	                                                           的getter方法增加判断，凡是得到""就返回null，这似乎也有些绝对，万一有我们意向不到的情况可能就不太容易处理，所以这种方式不建议使用，当然如果你是强硬派约定程序就这样规定的，那倒也是好事，毕竟简单，
	                                                           这种约定永远不能破坏，有其他问题跟这个约定冲突的就再想办法解决其他问题，反正这个约定不能破坏。
	                	2.借助框架，如果是使用SpringMVC，就要在数据绑定时用它的拦截机制进行""到null值的替换，这是比较好的一种方式。单独抽取出来的一个方法且全局统一，动态织入程序代码中，松耦合。具体需要调查一下。。。
	      
			6.可能还有一种需求，就是我可能只想校验实体对象中的某一个字段，其实这是某几个字段的一个特例，两种方式：
			  (1).如果想使用SpringMvc自己调用的jsr303，那么要说明的是输入参数必须是一个对象，不能直接是这个属性，SpringMvc暂不支持这种，那么输入参数还是一个对象，只是其中某一个字段才是真正想要的输入参数，只需要校验这个属性，
			                那么还是那个做法，就是专门在这个属性字段上设定一个注解分组，SpringMvc中标注这个分组注解，那么此时就只会校验这个属性字段，这种方式弊端是要专门为一个字段定义一个专门的类型，类型会比较多，需要有一个好的注解类型规划。
			  (2).其实专门针对一个属性的输入参数，没必要再浪费校验分组注解了，又不是有多个属性需要校验，我们还值得来一个分组。这种情况，我们完全可以不用SpringMvc调用jsr303了，这样我们输入参数可以直接是这个属性字段，而不是
     			      这个属性字段所对应的实体，此时，接收到参数后，我们可以直接调用我们封装好的jsr303校验方法，手动写一行调用代码进行校验即可，当然这个封装好的方法返回的数据会在方法内部封装成返回SpringMvc的BindingResult对象返回，
     			      统一用SpringMvc的对象封装错误信息，即可做到统一，这也挺好的。
     			      更好一点的是，此时我们就可以写Spring的aop织入代码了，在before模块中进行统一的切入处理了。再目标方法上加一个自定义的标记注解，aop的配置规则的是凡是有这个标记接口的方法都进行切入。
     			      同时，输入参数可能有多个，对于那个需要手动加校验的输入参数也加入自定义标记注解，在before方法中会读入，不同的注解可以进行不同的自定义校验。这样效果就达到了。
     			      并且，对于这种有多个输入参数的情况，还支持混合的，比如有的字段用了自定义校验标记注解，有的用了SpringMvc的校验注解，那么此时的校验顺序是Spring的那个字段先校验，我们的aop是在其后的。要知道这个顺序。
	
	
	  总结：核心：一套字段，我们根据需要要规划出一套或几套验证注解来，这样描述够形象了吧。
	  最佳实践：先写一套默认的，等需求说新增时校验规则和默认规则有的字段有不一样的地方，再补一套新增的校验注解就得了呗。总之就是有分组出现就要成套
	  (这个套可大可小，大的话是基于实体每个字段的，因为执行validator.validate方法时可能是验证的这个实体类，当然也可小，最小为一个字段上的注解要成套，比如所有加了OrderStatus0Group的是真的这个字段一套校验注解，不加任何分组标记的是这个字段的另外一套校验注解，
	   这样校验的时候就可以基于实体类所有字段或实体类的某一个字段分套校验了，对，分套校验就是核心)
	   还有最佳实践就是不要通盘去考虑所有字段直接去加，而是根据业务需求，逐个字段根据自己需求，自己的依附关系去加校验，这样把每一个字段上的几套校验加好了，那么所有字段也就是这个实体类的几套校验注解自然也就形成了，这就是最佳实践啊。   
	
	
	  另外：推荐注解都写到get方法上，这样保证字段是干净的，增强可读性。注解一旦写好读改的机会就少很多了，找的时候就去get方法上看呗。
	**/
	
	
	
	// 不分组时执行的验证注解
	@NotNull
	// TODO 关键字过滤的自定义注解，在OrderStatus1Group标记时生效。
	@Size.List(value={
		// 下面两个OrderGroupA其实完全可以用一个里面写min和max，这样写目的是为了说明可以这样用，有些时候会用到，
		// 比如其他的正则表达式的注解，需要同时满足两个条件，而又写在一个里面不容易写的时候。
			
		// 分组时入参值为OrderGroupA.class时执行的验证注解
		@Size(min = 2,message="{javax.validation.constraints.Size_min.message}",groups={OrderGroupA.class}),
		// 分组时入参值为OrderGroupA.class时执行的验证注解
		@Size(max = 3,message="{javax.validation.constraints.Size_max.message}",groups={OrderGroupA.class}),
		// 分组时入参值为OrderGroupB.class时执行的验证注解
		@Size(min = 4,message="{javax.validation.constraints.Size_min.message}",groups={OrderGroupB.class}),
		// 不分组时执行的验证注解
		@Size(min = 5,message="{javax.validation.constraints.Size_min.message}"),
		@Size(min = 6,message="{javax.validation.constraints.Size_min.message}"),
		// 不分组时执行的验证注解
		@Size(max = 7,message="{javax.validation.constraints.Size_max.message}"),
		
		// 根据字段Status取值的不同动态进行验证(当Status的值为0时，执行上面的校验)
		@Size(min = 8,message="{javax.validation.constraints.Size_min.message}",groups={OrderStatus0Group.class}),
		@Size(min = 9,message="{javax.validation.constraints.Size_min.message}",groups={OrderStatus0Group.class}),
		@Size(max = 10,message="{javax.validation.constraints.Size_max.message}",groups={OrderStatus0Group.class}),
		@Size(min = 11,message="{javax.validation.constraints.Size_min.message}",groups={OrderStatus1Group.class}),
		@Size(max = 12,message="{javax.validation.constraints.Size_max.message}",groups={OrderStatus1Group.class}),
	})
	private String orderId;
	
	@NotBlank.List(value={
		// 不分组时执行的验证注解
		@NotBlank,
		// 分组时入参值为OrderGroupA.class或OrderGroupA.class时执行的验证注解（只有提前指定好OrderGroupA.class或OrderGroupB.class或OrderGroupA.class和OrderGroupB.class都指定时才执行该该注解，而没有标记该group的注解都不执行）
		@NotBlank(groups={OrderGroupA.class,OrderGroupB.class})
	})
	@Size(min = 3,message="{javax.validation.constraints.Size_min.message}",groups=OrderStatus0Group.class)
	private String customer;
	// 必须是一个电子信箱地址
	@Email
	private String email;
	
	// 言外之意：如果验证时标记参数传入的是OrderGroupB.class,则address就可以是空的了，因为下面的两个注解都不会生效。
	@NotEmpty.List(value={
		// 不分组时执行的验证注解
		@NotEmpty,
		// 分组时入参值为OrderGroupA.class时执行的验证注解
		@NotEmpty(groups={OrderGroupA.class})
	})
	private String address;
	// 必须不为 null, 必须是下面四个字符串'created', 'paid', 'shipped', 'closed'其中之一
	// @Status 是一个定制化的 contraint
	@NotNull.List(value={@NotNull(groups={OrderGroupA.class,OrderGroupB.class})})
	@Status.List(value={@Status(groups={OrderGroupA.class,OrderGroupB.class})})
	private String status;
	// 必须不为 null
	@NotNull(groups={OrderGroupA.class})
	private Date createDate;
	// 嵌套验证
	@NotNull
	@Valid
	private Product product;
	
	@Min(3)
	private Integer integerTest;
	@Min(3)
	private int intTest;
	
	@AssertFalse
	private Boolean booleanFengzhuangTest;
	@AssertFalse
	private boolean booleanJiandanTest;
	
	
//	// 不分组时执行的验证注解
//	@NotBlank(groups=GroupSort1.class)
//	// 不分组时执行的验证注解(不推荐这样写，同一个注解推荐都写到List中，如下，不同的注解才是都在外部直接指定或不指定group，就不会也不可能用List了)
//	@Size(min = 3,message="{javax.validation.constraints.Size_min.message}",groups=GroupSort1.class)
//	@Size.List(value={
//		// 下面两个OrderGroupA其实完全可以用一个里面写min和max，这样写目的是为了说明可以这样用，有些时候会用到，
//		// 比如其他的正则表达式的注解，需要同时满足两个条件，而又写在一个里面不容易写的时候。
//			
//		// 分组时执行的验证注解
//		@Size(min = 2,message="{javax.validation.constraints.Size_min.message}",groups={GroupSort1.class,OrderGroupA.class}),
//		// 分组时执行的验证注解
//		@Size(max = 3,message="{javax.validation.constraints.Size_max.message}",groups={GroupSort1.class,OrderGroupA.class}),
//		// 分组时执行的验证注解
//		@Size(min = 5,message="{javax.validation.constraints.Size_min.message}",groups={GroupSort1.class,OrderGroupB.class}),
//		// 不分组时执行的验证注解
//		@Size(max = 8,message="{javax.validation.constraints.Size_max.message}")
//	})
//	private String orderId;
//	
//	// 不分组时执行的验证注解
//	@NotBlank(groups=GroupSort2.class)
//	// 分组时执行的验证注解（只有提前指定好OrderGroupA.class或OrderGroupB.class或OrderGroupA.class和OrderGroupB.class都指定时才执行该该注解，而没有标记该group的注解都不执行）
//	@NotEmpty(groups={GroupSort2.class,OrderGroupA.class,OrderGroupB.class})
//	private String customer;
//	// 必须是一个电子信箱地址
//	@Email(groups=GroupSort3.class)
//	private String email;
//	
//	// 言外之意：如果验证时标记参数传入的的OrderGroupB.class,则address就可以是空的了，因为下面的两个注解都不会生效。
//	@NotEmpty.List(value={
//		// 不分组时执行的验证注解
//		@NotEmpty(groups={GroupSort4.class}),
//		// 分组时执行的验证注解
//		@NotEmpty(groups={GroupSort4.class,OrderGroupA.class})
//	})
//	private String address;
//	// 必须不为 null, 必须是下面四个字符串'created', 'paid', 'shipped', 'closed'其中之一
//	// @Status 是一个定制化的 contraint
//	@NotNull
//	@Status
//	private String status;
//	// 必须不为 null
//	@NotNull
//	private Date createDate;
//	// 嵌套验证
//	@Valid
//	private Product product;
	
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getIntegerTest() {
		return integerTest;
	}

	public void setIntegerTest(Integer integerTest) {
		this.integerTest = integerTest;
	}

	public int getIntTest() {
		return intTest;
	}

	public void setIntTest(int intTest) {
		this.intTest = intTest;
	}

	public Boolean getBooleanFengzhuangTest() {
		return booleanFengzhuangTest;
	}

	public void setBooleanFengzhuangTest(Boolean booleanFengzhuangTest) {
		this.booleanFengzhuangTest = booleanFengzhuangTest;
	}

	public boolean isBooleanJiandanTest() {
		return booleanJiandanTest;
	}

	public void setBooleanJiandanTest(boolean booleanJiandanTest) {
		this.booleanJiandanTest = booleanJiandanTest;
	}
}
