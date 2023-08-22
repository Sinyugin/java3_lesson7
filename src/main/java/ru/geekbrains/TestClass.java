package ru.geekbrains;

public class TestClass {
    @BeforeSuite
    static void before() {
        System.out.println("Before");
    }

    @AfterSuite
    void after() {
        System.out.println("After");
    }

    @Test(order = 3)
    void testMethod() {
        System.out.println("order 3");
    }

    @Test
    void testDefaultOrder() {
        System.out.println("default order");
    }

    @Test(order = 10)
    void testOrder10() {
        System.out.println("order 10");
    }

}
