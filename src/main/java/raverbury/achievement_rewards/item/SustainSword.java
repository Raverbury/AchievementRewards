package raverbury.achievement_rewards.item;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import raverbury.achievement_rewards.ModItem;
import raverbury.achievement_rewards.util.CustomItemTier;

public class SustainSword extends SwordItem {

  public static final String ITEM_ID = "sustain_sword";
  public static final Properties ITEM_PROPERTIES = new Properties().maxStackSize(1).defaultMaxDamage(1024)
      .group(ModItem.ModCreativeTab.instance).rarity(Rarity.EPIC);
  public static final IItemTier ITEM_TIER = new CustomItemTier(0, 512, 1.0F, 0F, 25, () -> {
    return Ingredient.EMPTY;
  });
  public static final int ATTACK_DAMAGE = 0;
  public static final float ATTACK_SPEED = -1F;
  /**
   * Understood as x%.
   */
  public static final float BONUS_MAX_HEALTH_DAMAGE = 3F;
  /**
   * Understood as x%.
   */
  public static final float PRE_MITIGATED_LIFE_STEAL = 5F;

  public SustainSword() {
    super(ITEM_TIER, ATTACK_DAMAGE, ATTACK_SPEED, ITEM_PROPERTIES);
  }

  @Override
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (Screen.hasShiftDown()) {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.sustain_sword_expanded", BONUS_MAX_HEALTH_DAMAGE, PRE_MITIGATED_LIFE_STEAL));
    } else {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.hold_shift_for_more"));
    }
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
    if (!player.world.isRemote) {
      doStuff(stack, player, entity);
    }
    return super.onLeftClickEntity(stack, player, entity);
  }

  private void doStuff(ItemStack swordItemStack, PlayerEntity player, Entity entity) {
    if (swordItemStack.getItem() != ModItem.SUSTAIN_SWORD_ITEM.get()) {
      return;
    }
    if (entity instanceof LivingEntity == false) {
      return;
    }
    LivingEntity target = (LivingEntity) entity;
    float percentMaxHealthDamage = target.getMaxHealth() * (BONUS_MAX_HEALTH_DAMAGE / 100F);
    entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, player), percentMaxHealthDamage);
    player.heal(percentMaxHealthDamage * (PRE_MITIGATED_LIFE_STEAL / 100F));
  }
}
