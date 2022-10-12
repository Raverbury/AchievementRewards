package raverbury.achievement_rewards.util;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class Misc {
  public static int getExperienceAtLevel(int level) {
    if (level >= 30) {
      return 112 + (level - 30) * 9;
    } else {
      return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
    }
  }

  public static int getTotalExperienceAtLevel_EmptyBar(int level) {
    if (level <= 0)
      level = 0;
    int totalExpNeeded = 0;
    for (int tmpLevel = 0; tmpLevel < level; tmpLevel++) {
      totalExpNeeded += getExperienceAtLevel(tmpLevel);
    }
    return totalExpNeeded + 1;
  }

  public static int getTotalExperienceAtLevel_FullBar(int level) {
    if (level <= 0)
      level = 0;
    int totalExpNeeded = 0;
    for (int tmpLevel = 0; tmpLevel <= level; tmpLevel++) {
      totalExpNeeded += getExperienceAtLevel(tmpLevel);
    }
    return totalExpNeeded;
  }

  public static void logSide(World worldIn) {
    if (worldIn.isRemote) {
      System.out.println("CLIENT");
    }

    if (!worldIn.isRemote) {
      System.out.println("SERVER"); // These show as the color RED in the client as info, so it's easy to see!
    }
  }

  /**
   * Returns the existing (or new if null) NBT of an ItemStack
   * @param itemStack
   * @return a CompoundNBT object
   */
  public static CompoundNBT getCompoundNBT(ItemStack itemStack) {
    if (!itemStack.hasTag()) {
      itemStack.setTag(new CompoundNBT());
    }
    CompoundNBT nbt = itemStack.getTag();
    return nbt;
  }
}