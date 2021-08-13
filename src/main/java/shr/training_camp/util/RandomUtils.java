package shr.training_camp.util;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import shr.training_camp.core.model.database.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
public class RandomUtils {

    private static Random random = new Random(System.currentTimeMillis());
    private final static String vowel = "aeiouy";
    private final static String consonant = "bcdfghjklmnpqrstvwxz";

    private static int[] patsTrush = {10, 30, 60}; // волосы, ногти, кожа

    public static void main(String[] args) {
//        System.out.println("Sveta: " + getRandom(2));
//        System.out.println("Shadow1: " + getRandom(3) + 2);
        Map<Integer, Integer> checkMap = new HashMap<>();
        //getRandom(-10);
        int[][] testProbability = new int[][] {{95, 1, 1, 1, 2}, {-1, 0, -6, 12, 100}};
        getProbabilityForGroup(testProbability);
        for (int i=1; i<=5000; i++) {
            //Integer result = getProbabilityForGroup(patsTrush);
            Integer result = getProbabilityForGroup(testProbability);
            if (checkMap.containsKey(result)) {
                Integer value = checkMap.get(result) + 1;
                checkMap.put(result, value);
            } else {
                checkMap.put(result, 1);
            }
        }
        checkMap.forEach((key, value) -> System.out.println(key + " : " + value));
        //System.out.println(calcGrowGroup(30, -0.5, 2));
    }

    public static boolean isEventHappens(double probability) {
        if (probability <= 0.0 && probability >= 1.0 ) {
            throw new IllegalArgumentException("The probability has to be between 0.0 and 1.0");
        }
        return Math.random()<= probability;
    }

    public static Long getValueFromMap(Map<Long, Integer> source) {
        Integer shoot = random.nextInt(source.values().stream().mapToInt(integer -> integer).sum());
        Integer localSum = 0;
        for (Map.Entry<Long, Integer> map: source.entrySet()) {
            localSum += map.getValue();
            if (localSum>= shoot) {
                return map.getKey();
            }
        }
        return source.entrySet().stream().skip(source.size()-1).findFirst().orElseThrow(IllegalArgumentException::new).getKey();
    }

    public static int getProbabilityForGroup(int[] source) {
        int sum = 0;
        for (int k: source) {
            sum+= k;
        }
        int result = random.nextInt(sum);
        sum = 0;
        for (int i=0; i< source.length; i++) {
            sum+= source[i];
            if (result<sum) {
                return i;
            }
        }
        return source.length;
    }

    public static int getProbabilityForGroup(int[][] source) {
        int sum = 0;
        for (int k: source[0]) {
            sum+= k;
        }
        int result = random.nextInt(sum);
        sum = 0;
        for (int i=0; i< source[0].length; i++) {
            sum+= source[0][i];
            if (result<sum) {
                return source[1][i];
            }
        }
        return source[1][source.length-1];
    }

    public static String getReadableString(final int min, final int max, final boolean isFirstUpper) {
        int count = (int) (min + (max - min) * random.nextDouble());
        String result = "";
        for (int i=1; i<= count; i++) {
            if (i % 2 == 1) {
                result += consonant.toCharArray()[random.nextInt(consonant.length())];
            } else {
                result += vowel.toCharArray()[random.nextInt(vowel.length())];
            }
        }
        if (isFirstUpper) {
            return result.substring(0,1).toUpperCase() + result.substring(1, result.length());
        }
        return result;
    }

    public static int calcGrowGroup(int count, double minValue, double maxValue) {
        float result = 0.0f;
        for (int i=1; i<= count; i++) {
            result+= minValue + random.nextDouble()*maxValue;
        }
        return Math.round(result);
    }

    public static List<Player> getPlayersAndGroups(List<Player> allPlayers, int maxGroup) {
        List<Player> result = new ArrayList<>();
        for (Player player: allPlayers) {
            int value = 1 + random.nextInt(maxGroup);
            if (value == 12) {
                result.add(player);
            }
        }
        return result;
    }

    public static int getRandom(int maxValue) {
        return maxValue >=0 ? 1 + random.nextInt(maxValue) : -1 - random.nextInt(-maxValue);
    }


    public static double getRandomForAAByDefaultScheme(final int value) {
        return value >= 0 ? 0.01 * random.nextInt(value) : -0.01 * random.nextInt(-value);
    }

}
