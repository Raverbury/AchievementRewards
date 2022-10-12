package raverbury.achievement_rewards.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import raverbury.achievement_rewards.util.CustomItemTier;

public class SustainSword extends SwordItem {

  public static final String ITEM_ID = "sustain_sword";
  public static final Properties ITEM_PROPERTIES = new Properties().maxStackSize(1).defaultMaxDamage(1024)
      .group(ARModItems.ModCreativeTab.instance).rarity(Rarity.EPIC);
  public static final IItemTier ITEM_TIER = new CustomItemTier(0, 512, 1.0F, 0F, 25, () -> {
    return Ingredient.EMPTY;
  });
  public static final int ATTACK_DAMAGE = 0;
  public static final float ATTACK_SPEED = -1F;
  /**
   * Understood as x%.
   */
  public static final float BONUS_MAX_HEALTH_DAMAGE = 100F;
  /**
   * Understood as x%.
   */
  public static final float PRE_MITIGATED_LIFE_STEAL = 10F;

  public SustainSword() {
    super(ITEM_TIER, ATTACK_DAMAGE, ATTACK_SPEED, ITEM_PROPERTIES);
  }

  private static String getMaxHealthBonuDamage()
  {
    return BONUS_MAX_HEALTH_DAMAGE + "%";
  }

  private static String getPremitigatedLifesteal()
  {
    return PRE_MITIGATED_LIFE_STEAL + "%";
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (Screen.hasShiftDown()) {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.sustain_sword_expanded",
          getMaxHealthBonuDamage(), getPremitigatedLifesteal()));
    } else {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.hold_shift_for_more"));
    }
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  /**
   * Checks if event is fired from an attack made by this sword. If yes,
   * applies bonus damage to event.
   * Event should ideally be provided from a listener.
   * 
   * @param event
   */
  public static void applyEffect(LivingDamageEvent event) {
    // need to check if the event originated
    // from a hit with this sword + some more validating
    Entity suspectedAttacker = event.getSource().getImmediateSource();
    if (suspectedAttacker == null || suspectedAttacker instanceof LivingEntity == false) {
      return;
    }
    if (suspectedAttacker.world.isRemote) {
      return;
    }
    LivingEntity attacker = (LivingEntity) suspectedAttacker;
    if (attacker.getHeldItemMainhand().getItem() != ARModItems.SUSTAIN_SWORD_ITEM.get()) {
      return;
    }
    LivingEntity target = event.getEntityLiving();
    if (target == null) {
      return;
    }
    // applies bonus damage only for attacks made at full strength if attacker is a
    // player
    // !BROKEN, always at 0.0F
    // if (suspectedAttacker instanceof PlayerEntity) {
    //   PlayerEntity player = (PlayerEntity) suspectedAttacker;
    //   if (player.getCooledAttackStrength(0) < 1F) {
    //     System.out.println("Fail 4: " + player.getCooledAttackStrength(0));
    //     return;
    //   }
    // }
    float percentMaxHealthDamage = target.getMaxHealth() * (BONUS_MAX_HEALTH_DAMAGE / 100F);
    event.setAmount(event.getAmount() + percentMaxHealthDamage);
    attacker.heal(percentMaxHealthDamage * (PRE_MITIGATED_LIFE_STEAL / 100F));
  }
}
