package raverbury.achievement_rewards;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class MyUtil {

  public static class WeightedList<T> {
    private final NavigableMap<Double, T> weightedMap = new TreeMap<Double, T>();
    private final Random random;
    private double total = 0;
    public Boolean isEmpty = true;

    public WeightedList() {
      random = new Random();
    }

    public WeightedList(Random random) {
      this.random = random;
    }

    public WeightedList<T> add(T item, double weight) {
      if (weight <= 0.0) {
        weight = 1.0;
      }
      isEmpty = false;
      total += weight;
      weightedMap.put(Double.valueOf(total), item);
      return this;
    }

    public T getRandomItem() {
      double roll = random.nextDouble() * total;
      return weightedMap.higherEntry(roll).getValue();
    }
  }

  public static class Misc {

    public static int getExperienceAtLevel(int level) {
      if (level >= 30) {
        return 112 + (level - 30) * 9;
      } else {
        return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
      }
    }

    public static int getTotalExperienceAtLevel_EmptyBar(int level) {
      if (level <= 0) level = 0;
      int totalExpNeeded = 0;
      for (int tmpLevel = 0; tmpLevel < level; tmpLevel++) {
        totalExpNeeded += getExperienceAtLevel(tmpLevel);
      }
      return totalExpNeeded + 1;
    }

    public static int getTotalExperienceAtLevel_FullBar(int level) {
      if (level <= 0) level = 0;
      int totalExpNeeded = 0;
      for (int tmpLevel = 0; tmpLevel <= level; tmpLevel++) {
        totalExpNeeded += getExperienceAtLevel(tmpLevel);
      }
      return totalExpNeeded;
    }
  }
}