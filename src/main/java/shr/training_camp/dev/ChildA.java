package shr.training_camp.dev;

public class ChildA extends ParentA {

    public static void main(String[] args) {
        ChildA childA = new ChildA();
        System.out.println(childA.checkReturnType(2, "ha!"));
    }

    @Override
    String checkReturnType(int x, String str) {
        super.checkReturnType(x, str);
        return "result = " + result;
    }
}
