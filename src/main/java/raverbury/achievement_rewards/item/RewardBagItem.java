package raverbury.achievement_rewards.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import raverbury.achievement_rewards.ModItem;
import raverbury.achievement_rewards.MyUtil.WeightedList;

public class RewardBagItem extends Item {

  public RewardBagItem() {
    super(new Item.Properties().group(ModItem.ModCreativeTab.instance));
  }

  public static final String ITEM_ID = "reward_bag";
  // public static final Properties ITEM_PROPERTIES = new Item.Properties().group(ModItem.ModCreativeTab.instance);

  private static final WeightedList<ItemStack> possibleRewards = new WeightedList<ItemStack>()
      .add(new ItemStack(Items.EMERALD, 10), 4.0)
      .add(new ItemStack(Items.IRON_INGOT, 2), 10.0)
      .add(new ItemStack(Items.GOLD_INGOT, 2), 10.0)
      .add(new ItemStack(Items.COOKED_BEEF, 2), 40.0)
      .add(new ItemStack(Items.BAMBOO, 1), 30.0)
      .add(new ItemStack(Items.VINE, 1), 30.0)
      .add(new ItemStack(Items.GOLDEN_APPLE, 1), 3.0)
      .add(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1), 1.0)
      .add(new ItemStack(Items.ARROW, 4), 14.0)
      .add(new ItemStack(Items.BOOK, 1), 11.0)
      .add(new ItemStack(Items.STRING, 1), 17.0);
      // .add(new ItemStack(ModItem.TOME_OF_ENLIGHTENMENT_ITEM.get(), 1), 2.0);
      // .add(new ItemStack(ModItem.TRANSFORMATIVE_SWORD_REGISTRY_ITEM.get(), 1), 1.0);

  private static ItemStack getRandomItemStackFromRewardList() {
    ItemStack newItemStack = possibleRewards.getRandomItem().copy();
    return newItemStack;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote)
      openRewardBag(playerIn, handIn);
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  private static void openRewardBag(PlayerEntity playerIn, Hand handIn) {
    ItemStack itemStack = playerIn.getHeldItem(handIn);
    if (!playerIn.isCreative()) {
      itemStack.shrink(1);
    }
    ItemStack randomItemStack = getRandomItemStackFromRewardList();
    if (playerIn.inventory.getFirstEmptyStack() == -1) {
      playerIn.dropItem(randomItemStack, false, true);
    } else {
      playerIn.addItemStackToInventory(randomItemStack);
    }
  }
}
