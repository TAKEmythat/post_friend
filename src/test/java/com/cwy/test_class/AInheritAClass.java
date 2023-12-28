package com.cwy.test_class;

/**
 * @Classname AInheritAClass
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-27 20:26
 * @Since 1.0.0
 */
public class AInheritAClass extends AClass {
    @Override
    public void a0() {
        System.out.println("我是前置");
        super.a0();
        System.out.println("我是后置");
    }

    @Override
    public void a1() {
        System.out.println("我是前置");
        super.a1();
        System.out.println("我是后置");
    }
}
