package raverbury.achievement_rewards.item;

import java.util.List;

import javax.annotation.Nullable;

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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import raverbury.achievement_rewards.sound.ARSounds;
import raverbury.achievement_rewards.util.CustomItemTier;
import raverbury.achievement_rewards.util.Misc;

public class ActivatorBlade extends SwordItem {

  public static final String ITEM_ID = "activator_blade";
  public static final Properties ITEM_PROPERTIES = new Properties().maxStackSize(1).defaultMaxDamage(512)
      .group(ARModItems.ModCreativeTab.instance).rarity(Rarity.EPIC);
  public static final IItemTier ITEM_TIER = new CustomItemTier(0, 512, 2.0F, 1.0F, 25, () -> {
    return Ingredient.EMPTY;
  });
  public static final int ATTACK_DAMAGE = 3;
  public static final float ATTACK_SPEED = -2.2F;

  public static final int HIT_COUNT_THRESHOLD = 3;
  public static final float BONUS_MAGIC_DAMAGE = 5.0F;

  private static final String HIT_COUNT_NBT_KEY = "hitCount";

  public ActivatorBlade() {
    super(ITEM_TIER, ATTACK_DAMAGE, ATTACK_SPEED, ITEM_PROPERTIES);
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
      ITooltipFlag flagIn) {
    if (Screen.hasShiftDown()) {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.activator_blade_expanded",
          HIT_COUNT_THRESHOLD, BONUS_MAGIC_DAMAGE));
    } else {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.hold_shift_for_more"));
    }
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  public static void increaseHitCounterForPlayer(AttackEntityEvent event) {
    if (event.getEntityLiving() == null) {
      return;
    }
    LivingEntity attacker = event.getEntityLiving();
    if (attacker.world.isRemote) {
      return;
    }
    if (event.getTarget() == null) {
      return;
    }
    if (event.getTarget() instanceof LivingEntity == false) {
      return;
    }
    LivingEntity target = (LivingEntity) event.getTarget();
    if (!target.canBeAttackedWithItem()) {
      return;
    }
    if (target.hitByEntity(event.getEntity())) {
      return;
    }
    if (target.hurtResistantTime > 0) {
      return;
    }
    if (attacker.getHeldItemMainhand() == ItemStack.EMPTY) {
      return;
    }
    if (attacker.getHeldItemMainhand().getItem() != ARModItems.ACTIVATOR_BLADE.get()) {
      return;
    }
    PlayerEntity player = event.getPlayer();
    if (player.getCooledAttackStrength(0F) < 0.8F) {
      return;
    }
    increaseHitCounter(attacker.getHeldItemMainhand(), player);
  }

  private static void increaseHitCounter(ItemStack swordItemStack, LivingEntity attacker) {
    CompoundNBT nbt = Misc.getCompoundNBT(swordItemStack);
    if (nbt.contains(HIT_COUNT_NBT_KEY)) {
      nbt.putInt(HIT_COUNT_NBT_KEY, nbt.getInt(HIT_COUNT_NBT_KEY) + 1);
    } else {
      nbt.putInt(HIT_COUNT_NBT_KEY, 1);
    }
    if (isAlmostReady(swordItemStack)) {
      attacker.world.playSound((PlayerEntity) null, attacker.getPosX(),
          attacker.getPosY(), attacker.getPosZ(),
          ARSounds.ACTIVATOR_READY.get(), attacker.getSoundCategory(), 0.25F,
          1.0F);
    }
    if (canUnleash(swordItemStack)) {
      attacker.world.playSound((PlayerEntity) null, attacker.getPosX(),
          attacker.getPosY(), attacker.getPosZ(),
          ARSounds.ACTIVATOR_UNLEASH.get(), attacker.getSoundCategory(), 0.25F,
          1.0F);
    }
  }

  private static boolean isAlmostReady(ItemStack swordItemStack) {
    if (HIT_COUNT_THRESHOLD <= 1) {
      return false;
    }
    CompoundNBT nbt = Misc.getCompoundNBT(swordItemStack);
    if (!nbt.contains(HIT_COUNT_NBT_KEY)) {
      return false;
    }
    if (nbt.getInt(HIT_COUNT_NBT_KEY) == HIT_COUNT_THRESHOLD - 1) {
      return true;
    } else {
      return false;
    }
  }

  private static boolean canUnleash(ItemStack swordItemStack) {
    CompoundNBT nbt = Misc.getCompoundNBT(swordItemStack);
    if (!nbt.contains(HIT_COUNT_NBT_KEY)) {
      return false;
    }
    if (nbt.getInt(HIT_COUNT_NBT_KEY) == HIT_COUNT_THRESHOLD) {
      return true;
    } else {
      return false;
    }
  }

  private static void resetCounter(ItemStack swordItemStack) {
    CompoundNBT nbt = Misc.getCompoundNBT(swordItemStack);
    nbt.putInt(HIT_COUNT_NBT_KEY, 0);
  }

  /**
   * Does a bunch of checks and applies the bonus damage if over the hit threshold
   * 
   * @param
   */
  public static void applyEffect(LivingHurtEvent event) {
    Entity suspectedAttacker = event.getSource().getImmediateSource();
    if (suspectedAttacker == null || suspectedAttacker instanceof LivingEntity == false) {
      return;
    }
    if (suspectedAttacker.world.isRemote) {
      return;
    }
    LivingEntity attacker = (LivingEntity) suspectedAttacker;
    if (attacker.getHeldItemMainhand() == ItemStack.EMPTY) {
      return;
    }
    if (attacker.getHeldItemMainhand().getItem() != ARModItems.ACTIVATOR_BLADE.get()) {
      return;
    }
    ItemStack swordItemStack = attacker.getHeldItemMainhand();
    LivingEntity target = event.getEntityLiving();
    if (target == null) {
      return;
    }
    // increase hit counter if not player since we already did that at earlier
    if (suspectedAttacker instanceof PlayerEntity == false) {
      increaseHitCounter(swordItemStack, attacker);
    }

    if (canUnleash(swordItemStack)) {
      resetCounter(swordItemStack);
      event.setAmount(event.getAmount() + BONUS_MAGIC_DAMAGE);
      target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(attacker, null), BONUS_MAGIC_DAMAGE);
      target.hurtResistantTime = 0;
    }
  }
}
