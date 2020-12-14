public class TestOverride {
    public static void main(String[] args) {
        Sub c1 = new Sub();
        Sup c2 = new Sub();
        // 字段没有被重写
        System.out.println(c1.s);
        System.out.println(c2.s);
        // 方法被重写
        c1.info();
        c2.info();
        // 通过强转访问父类的字段
        c2 = (Sup)c1;
        System.out.println(c2.s);

    }
}

class Sup {
    String s = "Super";
    public void info(){
        System.out.println(this.s);
    }
}

class Sub extends Sup {
    String s = "Sub";

    @Override
    public void info(){
        System.out.println(this.s);
    }
}

/**
 * 抽象类
 */
abstract class A2 {
    abstract public void cry();
}

class NoNameClass {
    public static void main(String[] args) {
        // 匿名子类，通过实现抽象方法来实例化抽象类
        A2 a = new A2(){
            @Override
            public void cry() {
                System.out.println("555");
            }
        };
        a.cry();
    }

}


