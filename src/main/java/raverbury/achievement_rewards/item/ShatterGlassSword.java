package raverbury.achievement_rewards.item;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import raverbury.achievement_rewards.ModItem;
import raverbury.achievement_rewards.util.CustomItemTier;

public class ShatterGlassSword extends SwordItem {

  public static final String ITEM_ID = "shatter_glass_sword";
  public static final Properties ITEM_PROPERTIES = new Properties().maxStackSize(1).defaultMaxDamage(512)
      .group(ModItem.ModCreativeTab.instance).rarity(Rarity.EPIC);
  public static final IItemTier ITEM_TIER = new CustomItemTier(0, 512, 2.0F, 1.0F, 25, () -> {
    return Ingredient.EMPTY;
  });
  public static final int ATTACK_DAMAGE = 3;
  public static final float ATTACK_SPEED = -2.2F;

  public static final int HIT_NEEDED_TO_TRIGGER = 3;
  public static final float BONUS_MAGIC_DAMAGE = 5F;

  private static final String HIT_COUNT_NBT_KEY = "hitCount";

  public ShatterGlassSword() {
    super(ITEM_TIER, ATTACK_DAMAGE, ATTACK_SPEED, ITEM_PROPERTIES);
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
    if (!player.world.isRemote) {
      doStuff(stack, player, entity);
    }
    return super.onLeftClickEntity(stack, player, entity);
  }

  @Override
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (Screen.hasShiftDown()) {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.shatter_glass_sword_expanded", HIT_NEEDED_TO_TRIGGER, BONUS_MAGIC_DAMAGE));
    } else {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.hold_shift_for_more"));
    }
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  private void doStuff(ItemStack swordItemStack, PlayerEntity player, Entity entity) {
    if (swordItemStack.getItem() != ModItem.SHATTER_GLASS_SWORD.get()) {
      return;
    }
    if (!swordItemStack.hasTag()) {
      swordItemStack.setTag(new CompoundNBT());
    }
    CompoundNBT nbt = swordItemStack.getTag();
    if (nbt.contains(HIT_COUNT_NBT_KEY)) {
      nbt.putInt(HIT_COUNT_NBT_KEY, nbt.getInt(HIT_COUNT_NBT_KEY) + 1);
    } else {
      nbt.putInt(HIT_COUNT_NBT_KEY, 1);
    }
    if (nbt.getInt(HIT_COUNT_NBT_KEY) >= HIT_NEEDED_TO_TRIGGER) {
      entity.attackEntityFrom(DamageSource.causePlayerDamage(player), BONUS_MAGIC_DAMAGE);
      nbt.putInt(HIT_COUNT_NBT_KEY, 0);
    }
  }
}
