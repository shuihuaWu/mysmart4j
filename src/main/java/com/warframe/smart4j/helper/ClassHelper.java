package com.warframe.smart4j.helper;


import com.warframe.smart4j.annotation.Controller;
import com.warframe.smart4j.annotation.Service;
import com.warframe.smart4j.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author warframe[github.com/WarframePrimer]
 * @Date 2017/10/14 15:57
 * 在smart.properties配置文件中指定了smart.framework.app.base_path，它是整个应用的基础包名，通过ClassUtil加载的类都需要基于该基础包名。
 * ClassHelper：
 * 作用：分别获取应用包名下的所有类，应用包名下的所有Service类，所有Controller类。
 * 此外，可以将带有Controller注解与Service注解的类所产生的对象，理解为由smart框架所管理的bean
 * 所有需要在ClassHelper类中增加一个获取应用包名下的所有Bean类的方法。
 */
public final class ClassHelper {

    /**
     * 定义类集合(用于存放所加载的类)
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用包名下的所有类
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取应用包名下的所有Service类
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                classSet.add(cls);
            }
        }

        return classSet;
    }

    /**
     * 获取应用包名下的所有Controller类
     */
    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)) {
                classSet.add(cls);
            }
        }

        return classSet;
    }

    /**
     * 获取应用包名下的所有bean类
     */
    public static Set<Class<?>> getBeanClassSet() {
        //思考，框架需要管理的Bean应该有哪些??
        Set<Class<?>> beanClassSet = new HashSet<>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());
        return beanClassSet;
    }
    /*===========================================================*/
    /* 因为需要扩展AspectProxy抽象类的所有具体类;此外,还需要获取带有Aspect注解的所有类
    * 所以需要ClassHelper中添加以下两个方法
    * */

    /**
     * 获取应用包名下某父类(或接口)的所有子类(或实现类)
     * 用来获取所有实现了AspectProxy的具体类
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            /*class1.isAssignableFrom(class2)判定class1是否和class2相同或者是class2的父类或父接口，如果是，return true*/
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                classSet.add(cls);
            }
        }

        return classSet;
    }

    /**
     * 获取应用包名下带有某注解的所有类
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            if(cls.isAnnotationPresent((annotationClass))){
                classSet.add(cls);
            }
        }
        return classSet;
    }


}
