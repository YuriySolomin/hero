package shr.training_camp.util;

public class MathUtils {

    public static void main(String[] args) {
        System.out.println(getSumOfAP(100, 0.1, 2));
        System.out.println(getSumOfAP(1, 1, 10));
        System.out.println(getLastElementBySum(1, 1000, 999));
    }

    public static Double getSumOfAP(double firstElement, double ratio, long count) {
        return (2*firstElement + (count - 1)*ratio)*count / 2;
    }

    public static double getLastElementBySum(double firstElement, double ratio, double sum) {
        double b = 2*firstElement - ratio;
        double d = b*b + 8*ratio*sum;
        return  (-b + Math.sqrt(d))/(2*ratio);
    }


}
