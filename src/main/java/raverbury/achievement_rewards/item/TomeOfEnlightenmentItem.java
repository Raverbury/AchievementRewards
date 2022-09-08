package raverbury.achievement_rewards.item;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import raverbury.achievement_rewards.ModItem;
import raverbury.achievement_rewards.MyUtil.Misc;

public class TomeOfEnlightenmentItem extends Item {

  public static final String ITEM_ID = "tome_of_enlightenment";

  private static final int LEVEL_THRESHOLD = 30;
  private static final int LEVEL_GAINED = 3;

  public TomeOfEnlightenmentItem() {
    super(new Item.Properties().group(ModItem.ModCreativeTab.instance).rarity(Rarity.EPIC));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote)
      useTomeOfEnlightenment(playerIn, handIn);
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  @Override
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (Screen.hasShiftDown()) {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.tome_of_enlightenment_expanded", LEVEL_THRESHOLD, LEVEL_GAINED));
    } else {
      tooltip.add(new TranslationTextComponent("tooltip.achievement_rewards.hold_shift_for_more"));
    }
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  private static void useTomeOfEnlightenment(PlayerEntity playerIn, Hand handIn) {
    ItemStack itemStack = playerIn.getHeldItem(handIn);
    if (!playerIn.isCreative()) {
      itemStack.shrink(1);
    }
    int expNeeded = 0;
    if (playerIn.experienceLevel < LEVEL_THRESHOLD) {
      expNeeded = Misc.getTotalExperienceAtLevel_FullBar(LEVEL_THRESHOLD - 1) - playerIn.experienceTotal;
    } else {
      expNeeded = Misc.getTotalExperienceAtLevel_EmptyBar(playerIn.experienceLevel + LEVEL_GAINED)
          - playerIn.experienceTotal;
    }
    playerIn.giveExperiencePoints(expNeeded);
    float f = 0.4F;
    playerIn.world.playSound((PlayerEntity) null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
        SoundEvents.ENTITY_PLAYER_LEVELUP, playerIn.getSoundCategory(), f * 0.75F, 1.0F);
  }
}