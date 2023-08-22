package ru.geekbrains;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestExecutor {
    public static void start(Class<?> testClass) {

        try {
            startUnsafe(testClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void startUnsafe(Class<?> testClass) throws Exception {

        Method[] methods = testClass.getDeclaredMethods();

        Method beforeSuitMethods = null;
        List<Method> testMethods = new ArrayList<Method>();
        Method afterSuitMethods = null;

        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuitMethods != null) {
                    throw new RuntimeException("Illegal count of BeforeSuite annotations");
                }
                beforeSuitMethods = method;
            }

            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuitMethods != null) {
                    throw new RuntimeException("Illegal count of AfterSuite annotations");
                }
                afterSuitMethods = method;
            }

            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }

        testMethods.sort((m1, m2) -> {
            Test m1Anno = m1.getAnnotation(Test.class);
            Test m2Anno = m2.getAnnotation(Test.class);
            return Integer.compare(m2Anno.order(), m1Anno.order());
        });

        final Constructor<?> defaultConstructor;
        try{
            defaultConstructor = testClass.getDeclaredConstructor();
        }catch (NoSuchMethodException e){
            throw new RuntimeException("Can't find constructor with no args");
        }

        Object testInstance = defaultConstructor.newInstance();

        if (beforeSuitMethods != null) {
            beforeSuitMethods.invoke(testInstance);
        }

        for (Method testMethod : testMethods) {
            testMethod.invoke(testInstance);
        }

        if (afterSuitMethods != null) {
            afterSuitMethods.invoke(testInstance);
        }
    }
}
