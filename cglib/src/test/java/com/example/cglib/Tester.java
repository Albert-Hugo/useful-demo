package com.example.cglib;

import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.beans.ImmutableBean;
import net.sf.cglib.proxy.*;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Carl
 * @date 2020/4/16
 */
public class Tester {
    @Test
    public void testFixedValue() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(EnhancerTest.SampleClass.class);
        enhancer.setCallback(new FixedValue() {
            @Override
            public Object loadObject() throws Exception {
                return "Hello cglib!";
            }
        });
        EnhancerTest.SampleClass proxy = (EnhancerTest.SampleClass) enhancer.create();
        assertEquals("Hello cglib!", proxy.test(null));
    }


    @Test
    public void testInvocationHandler() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(EnhancerTest.SampleClass.class);
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                if(method.getDeclaringClass() != Object.class
                        && method.getReturnType() == String.class) {
                    return "Hello cglib!";
                } else {
                    return "\"Do not know what to do.\"";
//                    throw new RuntimeException("Do not know what to do.");
                }
            }
        });
        EnhancerTest.SampleClass proxy = (EnhancerTest.SampleClass) enhancer.create();
        assertEquals("Hello cglib!", proxy.test(null));
        assertNotEquals("Hello cglib!", proxy.toString());
    }


    @Test
    public void testMethodInterceptor() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(EnhancerTest.SampleClass.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
                    throws Throwable {
                if(method.getDeclaringClass() != Object.class
                        && method.getReturnType() == String.class) {
                    return "Hello cglib!";
                } else {
                    return proxy.invokeSuper(obj, args);
                }

            }
        });
        EnhancerTest.SampleClass proxy = (EnhancerTest.SampleClass) enhancer.create();
        assertEquals("Hello cglib!", proxy.test(null));
        assertNotEquals("Hello cglib!", proxy.toString());
        proxy.hashCode(); // Does not throw an exception or result in an endless loop.
    }


    @Test(expected = IllegalStateException.class)
    public void testImmutableBean() throws Exception {
        SampleBean bean = new SampleBean();
        bean.setValue("Hello world!");
        SampleBean immutableBean = (SampleBean) ImmutableBean.create(bean);
        assertEquals("Hello world!", immutableBean.getValue());
        bean.setValue("Hello world, again!");
        assertEquals("Hello world, again!", immutableBean.getValue());
        immutableBean.setValue("Hello cglib!"); // Causes exception.
    }


    @Test
    public void testBeanGenerator() throws Exception {
        SampleBean bean = new SampleBean();
        BeanMap map = BeanMap.create(bean);
        bean.setValue("Hello cglib!");
        assertEquals("Hello cglib!", map.get("value"));
    }


}
