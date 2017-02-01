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
	   ���ԣ� 1.û���������������£���ÿһ���ֶζ�����Ĭ�ϵ�ע�⣬��û��ָ��groups��ע�⡣
	       2.�����ִ��ĳЩ����ʱ����ĳЩ�ֶ����������󣬾���Ҫ���ֶ�ע������������һ������groups��ע�⣬ע�⣬��������������ֶ���Ҫ�����֮�⣬�����ֶ�Ҳ��Ҫ�ӣ������Ǹ�����groups��ע��һģһ����ҲҪ�ӣ�
	         ��Ϊ�������ʱһ��������group����ô�����ֶζ���ȥ�Ҵ������groupע��Ľ���У�飬Ҳ����˵һ��ĳ�����ֶ�������������Ҫȫ����֤���ֶζ�����group��ֻ���������ֶζ�ӵ����ע������û��groupsע��Ļ�����ָ��һ��group��
	         ������������ģ�����ָ��group֮�⻹Ҫ��������д�Ϻ��ʵ�����������У����򡣱��粻ָ��groupʱ��Size(max=8)����ָ����group�ģ����������������Size(max=10)�ˣ����������˼��
           3.�����У��˳����Ҫ�����ǻ�����ָ���ֶε�У��˳��ʹ��@GroupSequence����˳�򣬾���ο�http://blog.csdn.net/gaoshanliushui2009/article/details/50667017���ͣ�������Ҫ˵�����ǣ�һ����������ǲ���Ҫ����У��˳��
	         ��Ϊ���յ�У����������������ֱ�ӵ��û��ǽ���Spring�����յõ��Ĵ�����Ϣ���������Ƕ����õ��ֶ����ƺ��ֶ�У�������Ϣ�������ڷ��ص�ǰ̨��ʱ���Ѿ����Ժܺõ����ö�Ӧ�ˡ�
	         ���������@GroupSequence��һ���׶ˣ���������������ʵҲ�൱������һ�������ˣ���ô�Ͳ����������������ˣ�һ�����ã���ô���˳����ܾ����ˣ�����validator.validate(order,GroupSort.class,OrderGroupA.class);
	         ���������˵ֻҪע���б����GroupSort.class(����@GroupSequence�Ĺ�ϵʵ������n��GroupSortn.class)����OrderGroupA.class��ע��(ֻҪ�������֮һ)������У�飬�����Ļ��е�ע�����ֻ�����OrderGroupA.classҲ��У�飬��ʱУ���ܾͲ�һ����˳�����ˡ�
	         �������validator.validate(order,GroupSort.class);�����ĵ��÷�ʽ�Ļ���˳���������õģ����Ǵ�ʱOrderGroupA.class��OrderGroupB.class�Ͳ��������ˣ��˴�ʧ���ˣ����Ҽ�ʱ������OrderGroupA.class��OrderGroupB.class����ʱ�����������õĽ����
	         ����Լ����˳�����У�飬һ������У��ʧ�ܵ��ֶξ�ֹͣ�������¼���У���ˣ����Եõ��Ľ����Զ��ֻ��һ���������Ҳ������ĳ����������٣�һ������£���õ��û�������һ���԰����еĲ��Ϸ���Ϣȫ�����ظ��û���(�Ͼ�����ǰ̨У��Ҳ������Щ����)
	         ��һ�򲽣���ʱ���У��Ҳ����һ��һ������һ��һ����ͨ���ͷ�����һ���������Ҳ�������ַ�ʽ�����ǻῼ����������ʽ������(���������ˣ�������Spring��validateУ���ܣ�����jsr303��)
	         ˵����ô�࣬�ܽ�һ�£�˳��У�������֪������ô���¾����ˣ����ǲ���ȥ�������������˾Ͱ������˰ɡ�
	
	       4.���ĳ���ֶε�У�������������ֶε�ȡֵ����ô��ʱ�����Ǿ���Ҫ�ڹ���У����ɺ��ٽ��д���΢���ˣ���õķ�ʽ����ʹ��SpringAOP��������ʹ��ҪдһЩ��֤���룬Ҳ�ǿ��Ժ;���ҵ��������ģ���֤����ĸɾ���
	         ע����ʱ�Ͳ���Ҫһ���ˣ�ֻ��Ҫ�������ֶ�Ӱ����Ǹ��ֶ��Լ������Լ�������Ҫ����ɫ(ע�ⲻͬ��ע�������Ҫ��ȫ����Ҫ����©��ֱ������ֶ���ҪдС��һ��У��ע��)�Ϳ����ˣ���Ϊ�����ʱ��Ҳ�ǵ����������Լ��ģ��������ֶ��޹��ˡ�
	         ����һ���������������������ֶ�Ӱ���У����ʵҲ������������У���ˣ�����ֻ���Լ�����һ�׶������������ֶ�Ӱ����Ǹ�����У�鼴�ɣ�����У�鶼����Ҫд�ˡ�
	
	       5.jsr303��У���������ģ�����һ���ֶ��ϼ��˶��ٸ�ע�⣬ֻҪ����ֶ��Ƿ�װ���ͻ��������͵�(������@Validע�����������)�������ֶ�ֵΪnullʱ�Ͳ������κ�У���ˣ�����nullʱ�Ž��и���ע���У�飬���Ǻ���ģ���Ϊnull�˻�У��ʲôѽ��
	         ��Ȼ�������������ֶε�ֵ�ǿտ���ͨ������@NotNull/@NotBlank/@NotEmptyУ��ע�⣬���ʱ������ֶ��������ֵ��null����ô���У��Ͳ���ͨ����(����ע����Ȼ����У��)������������ֵ��Ϊnull����У������ֶ��ϵ�����ע�⣬���е�У�鲻ͬ����Ϣ�����¼������
	         ���ϵõ����漸��ԭ��
	       5-1.���������ǵ��ֶμȿ���ʹ�÷�װ����Ҳ����ʹ�ü����ͣ������������Ҫʹ��jsr303У�飬����ò��ұ���ʹ�÷�װ���ͣ���Ϊ�ü���������������У��������������ֵ�ǻ���Ĭ��ֵ�ģ����ǱȽϲ��׵ģ�������Ϊ��һ�㣬
	                             ���ԣ���ʵ������jsr303�������ط�Ҳ�ǣ������Ƽ���ʵ����ʹ�ü����ͣ�����ʹ�÷�װ���ʹ��棬�������������int���ͣ������ǰ̨����ֵ�ǿյģ���ע����ֵ����0����ʵ�������0һ�������ܾͺ�Σ�գ������ܻᱻ��Ϊ���û�¼���ֵ��
	                             ����boolean���ǰ�������ǿյĻ�����ע��ʱĬ��ֵΪfalse����ʱ�����Ǿͻ���Ϊ��ʱǰ�˴�������ֵ����Σ�գ����嵽jsr303������Ǽ����ͣ�ǰ�������ǿյģ�����ע��ʱΪ��jdk�ḳ��Ĭ��ֵ�������Ļ������͵�ֵ��Զ����Ϊnull��
	                             Ҳ����˵������������ǲ��ɿ������ݣ�Ĭ�����ݣ���ʵǰ��������ǿգ���Ҳ�����jsr303У�飬�����Ϳ��ܻ���û����ش������֤��Ϣ����������Ҫ�÷�װ���ͣ����ü����͡�
	       5-2.ͬ������String��String���Ǽ����ͣ��������Ƶĵ������峡���������ģ����ǰ��ҳ���ĳ���ֶε�ֵ�û�û��¼�����ݣ�������ֶ���bean�е�������String�Ļ����ͻ���Ĭ��ע��ֵ""����ʵ�û�����û��¼�������������ȫ����
	       ��nullֵ������ʹ�����""ֵ��ʹ��nullֵ��Ȼ��ʹ��nullֵ�ĺô�����ʹ��""��ȻҲ�в�ʹ��""�ĺô���ʹ��nullֵ�ĺô�����������װ���ͱ���һ�£�û������ֵͳһ�����ݿ��ж�����null�����������nullֵ�ǲ�����jsr303��֤�ģ����Ҫȥ��null��
	       ���ǿ�����@NotNull/@NotBlank/@NotEmpty��У��ע�⣬��֮�����null������jsr303������ֶε�У����ȫ���ǵģ�������ʲô��������ܽ���У�飬�����Ĭ��ֵ��""������͸����ˣ�Ҫ֪��""Ҳ�������ݵģ�������null����ôjsr303��Ҫ������ֶν���
	       ��֤�ˣ�����ע����@Email����������""����@Email����ȷ��ʽ����Ȼ�Ͳ���ͨ����֤�ˣ������ǵ����������û�������ֶο��Բ�¼�����ݣ������¼�����ݾͱ�����@Email��ȷ�ĸ�ʽ���ǲ���ɵ���ˣ���""��檰첻������������
	      �������Ĭ��ֵ��null�Ͳ�һ���ˣ��û�ǰ�˲�¼�����ݣ�ע���װ��bean�е�Ĭ��ֵ��null�Ļ����Ͳ��������֤�ˣ��������ֵ�˲Ż����@Email��֤����������Ҫ��
	       5-3.��5-1��5-2���Կ������Ǳ��뱣֤�ֶε�Ĭ�����������ǿɿصģ�����5-1���򵥣�����ֻҪ���ֶζ���Ϊ��װ���ͣ�ע��ʱ���ǰ�˴����ǿ�ֵ����Ȼע��Ķ���nullûʲô˵�ģ��÷�װ��������ͽ���ˡ�
	                             ���Ƕ���5-2������String���͵��ֶΣ����ǰ��û��¼�����ݣ��������ݾ���ע���Ĭ��ֵ����""����������Ҫ����������Ǹı����Ĭ��ֵ�������޸�Ϊnull��һ�����������ʹ�õĶ���SpringMVC��ܣ����Ǿ�����һ��SpringMVC�����
	                             ��θı�String��ע��ֵ��
	                             ���ַ�����1.��String���͵��ֶε�setter���������ж�,������""��ȫ���滻��null�����ַ�ʽ�е㼫�ˣ���ζ���ڳ������κ�λ�õ���setter���������ܸ�""ֵ�ˣ������ǵ�Ŀ�Ľ�����ǰ�˴����������ݲ�����""�����������ø��ֶ�
	                                                           ��getter���������жϣ����ǵõ�""�ͷ���null�����ƺ�Ҳ��Щ���ԣ���һ���������򲻵���������ܾͲ�̫���״����������ַ�ʽ������ʹ�ã���Ȼ�������ǿӲ��Լ������������涨�ģ��ǵ�Ҳ�Ǻ��£��Ͼ��򵥣�
	                                                           ����Լ����Զ�����ƻ�����������������Լ����ͻ�ľ�����취����������⣬�������Լ�������ƻ���
	                	2.������ܣ������ʹ��SpringMVC����Ҫ�����ݰ�ʱ���������ػ��ƽ���""��nullֵ���滻�����ǱȽϺõ�һ�ַ�ʽ��������ȡ������һ��������ȫ��ͳһ����̬֯���������У�����ϡ�������Ҫ����һ�¡�����
	      
			6.���ܻ���һ�����󣬾����ҿ���ֻ��У��ʵ������е�ĳһ���ֶΣ���ʵ����ĳ�����ֶε�һ�����������ַ�ʽ��
			  (1).�����ʹ��SpringMvc�Լ����õ�jsr303����ôҪ˵�������������������һ�����󣬲���ֱ����������ԣ�SpringMvc�ݲ�֧�����֣���ô�����������һ������ֻ������ĳһ���ֶβ���������Ҫ�����������ֻ��ҪУ��������ԣ�
			                ��ô�����Ǹ�����������ר������������ֶ����趨һ��ע����飬SpringMvc�б�ע�������ע�⣬��ô��ʱ��ֻ��У����������ֶΣ����ַ�ʽ�׶���Ҫר��Ϊһ���ֶζ���һ��ר�ŵ����ͣ����ͻ�Ƚ϶࣬��Ҫ��һ���õ�ע�����͹滮��
			  (2).��ʵר�����һ�����Ե����������û��Ҫ���˷�У�����ע���ˣ��ֲ����ж��������ҪУ�飬���ǻ�ֵ����һ�����顣���������������ȫ���Բ���SpringMvc����jsr303�ˣ��������������������ֱ������������ֶΣ�������
     			      ��������ֶ�����Ӧ��ʵ�壬��ʱ�����յ����������ǿ���ֱ�ӵ������Ƿ�װ�õ�jsr303У�鷽�����ֶ�дһ�е��ô������У�鼴�ɣ���Ȼ�����װ�õķ������ص����ݻ��ڷ����ڲ���װ�ɷ���SpringMvc��BindingResult���󷵻أ�
     			      ͳһ��SpringMvc�Ķ����װ������Ϣ����������ͳһ����Ҳͦ�õġ�
     			      ����һ����ǣ���ʱ���ǾͿ���дSpring��aop֯������ˣ���beforeģ���н���ͳһ�����봦���ˡ���Ŀ�귽���ϼ�һ���Զ���ı��ע�⣬aop�����ù�����Ƿ����������ǽӿڵķ������������롣
     			      ͬʱ��������������ж���������Ǹ���Ҫ�ֶ���У����������Ҳ�����Զ�����ע�⣬��before�����л���룬��ͬ��ע����Խ��в�ͬ���Զ���У�顣����Ч���ʹﵽ�ˡ�
     			      ���ң����������ж������������������֧�ֻ�ϵģ������е��ֶ������Զ���У����ע�⣬�е�����SpringMvc��У��ע�⣬��ô��ʱ��У��˳����Spring���Ǹ��ֶ���У�飬���ǵ�aop�������ġ�Ҫ֪�����˳��
	
	
	  �ܽ᣺���ģ�һ���ֶΣ����Ǹ�����ҪҪ�滮��һ�׻�����֤ע���������������������˰ɡ�
	  ���ʵ������дһ��Ĭ�ϵģ�������˵����ʱУ������Ĭ�Ϲ����е��ֶ��в�һ���ĵط����ٲ�һ��������У��ע��͵����¡���֮�����з�����־�Ҫ����
	  (����׿ɴ��С����Ļ��ǻ���ʵ��ÿ���ֶεģ���Ϊִ��validator.validate����ʱ��������֤�����ʵ���࣬��ȻҲ��С����СΪһ���ֶ��ϵ�ע��Ҫ���ף��������м���OrderStatus0Group�����������ֶ�һ��У��ע�⣬�����κη����ǵ�������ֶε�����һ��У��ע�⣬
	   ����У���ʱ��Ϳ��Ի���ʵ���������ֶλ�ʵ�����ĳһ���ֶη���У���ˣ��ԣ�����У����Ǻ���)
	   �������ʵ�����ǲ�Ҫͨ��ȥ���������ֶ�ֱ��ȥ�ӣ����Ǹ���ҵ����������ֶθ����Լ������Լ���������ϵȥ��У�飬������ÿһ���ֶ��ϵļ���У��Ӻ��ˣ���ô�����ֶ�Ҳ�������ʵ����ļ���У��ע����ȻҲ���γ��ˣ���������ʵ������   
	
	
	  ���⣺�Ƽ�ע�ⶼд��get�����ϣ�������֤�ֶ��Ǹɾ��ģ���ǿ�ɶ��ԡ�ע��һ��д�ö��ĵĻ�����ٺܶ��ˣ��ҵ�ʱ���ȥget�����Ͽ��¡�
	**/
	
	
	
	// ������ʱִ�е���֤ע��
	@NotNull
	// TODO �ؼ��ֹ��˵��Զ���ע�⣬��OrderStatus1Group���ʱ��Ч��
	@Size.List(value={
		// ��������OrderGroupA��ʵ��ȫ������һ������дmin��max������дĿ����Ϊ��˵�����������ã���Щʱ����õ���
		// ����������������ʽ��ע�⣬��Ҫͬʱ������������������д��һ�����治����д��ʱ��
			
		// ����ʱ���ֵΪOrderGroupA.classʱִ�е���֤ע��
		@Size(min = 2,message="{javax.validation.constraints.Size_min.message}",groups={OrderGroupA.class}),
		// ����ʱ���ֵΪOrderGroupA.classʱִ�е���֤ע��
		@Size(max = 3,message="{javax.validation.constraints.Size_max.message}",groups={OrderGroupA.class}),
		// ����ʱ���ֵΪOrderGroupB.classʱִ�е���֤ע��
		@Size(min = 4,message="{javax.validation.constraints.Size_min.message}",groups={OrderGroupB.class}),
		// ������ʱִ�е���֤ע��
		@Size(min = 5,message="{javax.validation.constraints.Size_min.message}"),
		@Size(min = 6,message="{javax.validation.constraints.Size_min.message}"),
		// ������ʱִ�е���֤ע��
		@Size(max = 7,message="{javax.validation.constraints.Size_max.message}"),
		
		// �����ֶ�Statusȡֵ�Ĳ�ͬ��̬������֤(��Status��ֵΪ0ʱ��ִ�������У��)
		@Size(min = 8,message="{javax.validation.constraints.Size_min.message}",groups={OrderStatus0Group.class}),
		@Size(min = 9,message="{javax.validation.constraints.Size_min.message}",groups={OrderStatus0Group.class}),
		@Size(max = 10,message="{javax.validation.constraints.Size_max.message}",groups={OrderStatus0Group.class}),
		@Size(min = 11,message="{javax.validation.constraints.Size_min.message}",groups={OrderStatus1Group.class}),
		@Size(max = 12,message="{javax.validation.constraints.Size_max.message}",groups={OrderStatus1Group.class}),
	})
	private String orderId;
	
	@NotBlank.List(value={
		// ������ʱִ�е���֤ע��
		@NotBlank,
		// ����ʱ���ֵΪOrderGroupA.class��OrderGroupA.classʱִ�е���֤ע�⣨ֻ����ǰָ����OrderGroupA.class��OrderGroupB.class��OrderGroupA.class��OrderGroupB.class��ָ��ʱ��ִ�иø�ע�⣬��û�б�Ǹ�group��ע�ⶼ��ִ�У�
		@NotBlank(groups={OrderGroupA.class,OrderGroupB.class})
	})
	@Size(min = 3,message="{javax.validation.constraints.Size_min.message}",groups=OrderStatus0Group.class)
	private String customer;
	// ������һ�����������ַ
	@Email
	private String email;
	
	// ����֮�⣺�����֤ʱ��ǲ����������OrderGroupB.class,��address�Ϳ����ǿյ��ˣ���Ϊ���������ע�ⶼ������Ч��
	@NotEmpty.List(value={
		// ������ʱִ�е���֤ע��
		@NotEmpty,
		// ����ʱ���ֵΪOrderGroupA.classʱִ�е���֤ע��
		@NotEmpty(groups={OrderGroupA.class})
	})
	private String address;
	// ���벻Ϊ null, �����������ĸ��ַ���'created', 'paid', 'shipped', 'closed'����֮һ
	// @Status ��һ�����ƻ��� contraint
	@NotNull.List(value={@NotNull(groups={OrderGroupA.class,OrderGroupB.class})})
	@Status.List(value={@Status(groups={OrderGroupA.class,OrderGroupB.class})})
	private String status;
	// ���벻Ϊ null
	@NotNull(groups={OrderGroupA.class})
	private Date createDate;
	// Ƕ����֤
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
	
	
//	// ������ʱִ�е���֤ע��
//	@NotBlank(groups=GroupSort1.class)
//	// ������ʱִ�е���֤ע��(���Ƽ�����д��ͬһ��ע���Ƽ���д��List�У����£���ͬ��ע����Ƕ����ⲿֱ��ָ����ָ��group���Ͳ���Ҳ��������List��)
//	@Size(min = 3,message="{javax.validation.constraints.Size_min.message}",groups=GroupSort1.class)
//	@Size.List(value={
//		// ��������OrderGroupA��ʵ��ȫ������һ������дmin��max������дĿ����Ϊ��˵�����������ã���Щʱ����õ���
//		// ����������������ʽ��ע�⣬��Ҫͬʱ������������������д��һ�����治����д��ʱ��
//			
//		// ����ʱִ�е���֤ע��
//		@Size(min = 2,message="{javax.validation.constraints.Size_min.message}",groups={GroupSort1.class,OrderGroupA.class}),
//		// ����ʱִ�е���֤ע��
//		@Size(max = 3,message="{javax.validation.constraints.Size_max.message}",groups={GroupSort1.class,OrderGroupA.class}),
//		// ����ʱִ�е���֤ע��
//		@Size(min = 5,message="{javax.validation.constraints.Size_min.message}",groups={GroupSort1.class,OrderGroupB.class}),
//		// ������ʱִ�е���֤ע��
//		@Size(max = 8,message="{javax.validation.constraints.Size_max.message}")
//	})
//	private String orderId;
//	
//	// ������ʱִ�е���֤ע��
//	@NotBlank(groups=GroupSort2.class)
//	// ����ʱִ�е���֤ע�⣨ֻ����ǰָ����OrderGroupA.class��OrderGroupB.class��OrderGroupA.class��OrderGroupB.class��ָ��ʱ��ִ�иø�ע�⣬��û�б�Ǹ�group��ע�ⶼ��ִ�У�
//	@NotEmpty(groups={GroupSort2.class,OrderGroupA.class,OrderGroupB.class})
//	private String customer;
//	// ������һ�����������ַ
//	@Email(groups=GroupSort3.class)
//	private String email;
//	
//	// ����֮�⣺�����֤ʱ��ǲ�������ĵ�OrderGroupB.class,��address�Ϳ����ǿյ��ˣ���Ϊ���������ע�ⶼ������Ч��
//	@NotEmpty.List(value={
//		// ������ʱִ�е���֤ע��
//		@NotEmpty(groups={GroupSort4.class}),
//		// ����ʱִ�е���֤ע��
//		@NotEmpty(groups={GroupSort4.class,OrderGroupA.class})
//	})
//	private String address;
//	// ���벻Ϊ null, �����������ĸ��ַ���'created', 'paid', 'shipped', 'closed'����֮һ
//	// @Status ��һ�����ƻ��� contraint
//	@NotNull
//	@Status
//	private String status;
//	// ���벻Ϊ null
//	@NotNull
//	private Date createDate;
//	// Ƕ����֤
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
