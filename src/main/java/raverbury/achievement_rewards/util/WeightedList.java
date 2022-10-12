package raverbury.achievement_rewards.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedList<T> {
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