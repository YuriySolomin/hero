package shr.training_camp.dev;

public class ParentA {

    int result;

    String checkReturnType(int x, String str) {
        int s = 10*x;
        result = x*x + 1;
        return str + " " + s;
    }
}
