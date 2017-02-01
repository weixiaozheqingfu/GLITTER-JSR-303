Spring 配合jsr303的校验。

先写一个独立的spring自己的校验。
先写一个独立的jsr303自己的校验。
再写一个Spring下的jsr303校验，其实主要是Spring会自动启用jsr303校验，另外对jsr303校验的结果做了一个错误数据的封装而已。

然后再分别用不同的方案解决下面的问题，看怎样的组合或解决方式才是最好的解决方案（可以上面几种混搭解决，也可以使用其中一种解决，分别分析利弊，综合看哪种是最优的）。

  （1）.实现一个完整的示例。
  （2）.示例中应该包含博客文档，同一个字段针对不同的情况（根据其他字段的取值）采取不同的校验策略。
  （3）.示例中应该解决一个问题，就是被校验的对象中如果包含子对象，则对子对象的校验也要进行到。
  （4）.示例中应该解决一个问题，就是有的字段允许为空，但不为空时才进行jsr303注解的校验。这个问题怎么解决，很常见的。
  （5）.不管哪种校验，都实现从配置文件读取msg信息，方便国际化。

有些时候，在用户的应用中需要一些更复杂的 constraint。Bean Validation 提供扩展 constraint 的机制。可以通过两种方法去实现，一种是组合现有的 constraint 来生成一个更复杂的 constraint，另外一种是开发一个全新的 constraint。



http://haohaoxuexi.iteye.com/blog/1812584#comments
http://haohaoxuexi.iteye.com/blog/1044152
http://www.cnblogs.com/yangzhilong/p/3724967.html
本项目参考博文
http://www.ibm.com/developerworks/cn/java/j-lo-jsr303/
http://www.cnblogs.com/peida/archive/2013/04/24/3036689.html
http://breezylee.iteye.com/blog/1559188
http://lavasoft.blog.51cto.com/62575/184605

Hibernate懒加载（FetchType.LAZY）与Controller的JSR 303并存
http://blog.csdn.net/chaijunkun/article/details/9083171
下面这篇文章很重要
http://blog.csdn.net/chaijunkun/article/details/44854071
敏感词验证，有点意思，看来还真是有点用，学以致用了，有创意
http://www.blogjava.net/bolo/archive/2013/12/16/407650.html
实现权限控制
http://blog.csdn.net/v123411739/article/details/25899693

重要的工具类但是我看他代码有点问题
http://www.tuicool.com/articles/FRFjqmA


http://my.oschina.net/scjelly/blog/523711和http://sishuok.com/forum/blogPost/list/7847.html配合着看有效果。

非常不错的一篇文章，但对jsr303的排序说的不太好，尽量不用排序。
http://blog.csdn.net/wangpeng047/article/details/41726299

Spring的类型转换分析，对于传入的String类型如果想让默认值为null，也只能靠这个了
http://www.cnblogs.com/Leo_wl/p/3764937.html

/**
 * User: zhoujingjie
 * Date: 14-10-31
 * Time: 下午6:00
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { PhoneNoValidator.class})
public @interface PhoneNo {
 
    //默认错误消息
    String message() default "手机号码错误";
 
    //分组
    Class<?>[] groups() default { };
 
    //负载
    Class<? extends Payload>[] payload() default { };
 
    //指定多个时使用
    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        PhoneNo[] value();
    }
}

获取重复注解：
比如在使用hibernate validation时，我们想在一个方法上加相同的注解多个，需要使用如下方式：
@Length.List(
        value = {
                @Length(min = 1, max = 2, groups = A.class),
                @Length(min = 3, max = 4, groups = B.class)
        }
)

