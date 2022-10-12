package raverbury.achievement_rewards;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import raverbury.achievement_rewards.item.ARModItems;
import raverbury.achievement_rewards.item.ActivatorBlade;

@Mod.EventBusSubscriber(modid = AchievementRewards.MOD_ID)
public class AREventHandlers {
  @SubscribeEvent
  public static void onAdvancementEvent(AdvancementEvent event) {
    if (event.getEntity().world.isRemote) {
      return;
    }
    if (event.getAdvancement().getDisplay() == null) {
      return;
    }
    event.getPlayer().addItemStackToInventory(new ItemStack(ARModItems.REWARD_BAG_ITEM.get(), 1));
  }

  @SubscribeEvent(priority = EventPriority.LOW)
  public static void onLivingHurtEvent(LivingHurtEvent event) {
    if (event.getEntity().world.isRemote) {
      return;
    }
    ActivatorBlade.applyEffect(event);
    // SustainSword.applyEffect(event);

  }

  @SubscribeEvent
  public static void onAttackEntityEvent(AttackEntityEvent event) {
    if (event.getEntity().world.isRemote) {
      return;
    }
    ActivatorBlade.increaseHitCounterForPlayer(event);
  }

}
