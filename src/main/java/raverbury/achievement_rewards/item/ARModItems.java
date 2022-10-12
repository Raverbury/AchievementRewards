package raverbury.achievement_rewards.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import raverbury.achievement_rewards.AchievementRewards;

public class ARModItems{
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AchievementRewards.MOD_ID);
  public static final RegistryObject<Item> REWARD_BAG_ITEM = ITEMS.register(RewardBagItem.ITEM_ID, () -> new RewardBagItem());
  public static final RegistryObject<Item> TOME_OF_ENLIGHTENMENT_ITEM = ITEMS.register(TomeOfEnlightenmentItem.ITEM_ID, () -> new TomeOfEnlightenmentItem());
  public static final RegistryObject<Item> ACTIVATOR_BLADE = ITEMS.register(ActivatorBlade.ITEM_ID, () -> new ActivatorBlade());
  public static final RegistryObject<Item> SUSTAIN_SWORD_ITEM = ITEMS.register(SustainSword.ITEM_ID, () -> new SustainSword());
  // public static final RegistryObject<Item> REWARD_BAG_ITEM = ITEMS.register("reward_bag", () -> new Item(new Item.Properties().group(ModCreativeTab.instance)));

  /**
   * Register all listed items in the mod.
   * @param eventBus
   */
  public static void registerItems(IEventBus eventBus)
  {
    ITEMS.register(eventBus);
  }

  public static class ModCreativeTab extends ItemGroup {

    public static final ModCreativeTab instance = new ModCreativeTab(ItemGroup.getGroupCountSafe(), "achievement_rewards");

    private ModCreativeTab(int index, String label)
    {
      super(index, label);
    }

    @Override
    public ItemStack createIcon() {
      return new ItemStack(REWARD_BAG_ITEM.get());
    }
  }
}
