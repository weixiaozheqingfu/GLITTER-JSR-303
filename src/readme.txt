Spring ���jsr303��У�顣

��дһ��������spring�Լ���У�顣
��дһ��������jsr303�Լ���У�顣
��дһ��Spring�µ�jsr303У�飬��ʵ��Ҫ��Spring���Զ�����jsr303У�飬�����jsr303У��Ľ������һ���������ݵķ�װ���ѡ�

Ȼ���ٷֱ��ò�ͬ�ķ��������������⣬����������ϻ�����ʽ������õĽ���������������漸�ֻ������Ҳ����ʹ������һ�ֽ�����ֱ�������ף��ۺϿ����������ŵģ���

  ��1��.ʵ��һ��������ʾ����
  ��2��.ʾ����Ӧ�ð��������ĵ���ͬһ���ֶ���Բ�ͬ����������������ֶε�ȡֵ����ȡ��ͬ��У����ԡ�
  ��3��.ʾ����Ӧ�ý��һ�����⣬���Ǳ�У��Ķ�������������Ӷ�������Ӷ����У��ҲҪ���е���
  ��4��.ʾ����Ӧ�ý��һ�����⣬�����е��ֶ�����Ϊ�գ�����Ϊ��ʱ�Ž���jsr303ע���У�顣���������ô������ܳ����ġ�
  ��5��.��������У�飬��ʵ�ִ������ļ���ȡmsg��Ϣ��������ʻ���

��Щʱ�����û���Ӧ������ҪһЩ�����ӵ� constraint��Bean Validation �ṩ��չ constraint �Ļ��ơ�����ͨ�����ַ���ȥʵ�֣�һ����������е� constraint ������һ�������ӵ� constraint������һ���ǿ���һ��ȫ�µ� constraint��



http://haohaoxuexi.iteye.com/blog/1812584#comments
http://haohaoxuexi.iteye.com/blog/1044152
http://www.cnblogs.com/yangzhilong/p/3724967.html
����Ŀ�ο�����
http://www.ibm.com/developerworks/cn/java/j-lo-jsr303/
http://www.cnblogs.com/peida/archive/2013/04/24/3036689.html
http://breezylee.iteye.com/blog/1559188
http://lavasoft.blog.51cto.com/62575/184605

Hibernate�����أ�FetchType.LAZY����Controller��JSR 303����
http://blog.csdn.net/chaijunkun/article/details/9083171
������ƪ���º���Ҫ
http://blog.csdn.net/chaijunkun/article/details/44854071
���д���֤���е���˼�������������е��ã�ѧ�������ˣ��д���
http://www.blogjava.net/bolo/archive/2013/12/16/407650.html
ʵ��Ȩ�޿���
http://blog.csdn.net/v123411739/article/details/25899693

��Ҫ�Ĺ����൫���ҿ��������е�����
http://www.tuicool.com/articles/FRFjqmA


http://my.oschina.net/scjelly/blog/523711��http://sishuok.com/forum/blogPost/list/7847.html����ſ���Ч����

�ǳ������һƪ���£�����jsr303������˵�Ĳ�̫�ã�������������
http://blog.csdn.net/wangpeng047/article/details/41726299

Spring������ת�����������ڴ����String�����������Ĭ��ֵΪnull��Ҳֻ�ܿ������
http://www.cnblogs.com/Leo_wl/p/3764937.html

/**
 * User: zhoujingjie
 * Date: 14-10-31
 * Time: ����6:00
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { PhoneNoValidator.class})
public @interface PhoneNo {
 
    //Ĭ�ϴ�����Ϣ
    String message() default "�ֻ��������";
 
    //����
    Class<?>[] groups() default { };
 
    //����
    Class<? extends Payload>[] payload() default { };
 
    //ָ�����ʱʹ��
    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        PhoneNo[] value();
    }
}

��ȡ�ظ�ע�⣺
������ʹ��hibernate validationʱ����������һ�������ϼ���ͬ��ע��������Ҫʹ�����·�ʽ��
@Length.List(
        value = {
                @Length(min = 1, max = 2, groups = A.class),
                @Length(min = 3, max = 4, groups = B.class)
        }
)

