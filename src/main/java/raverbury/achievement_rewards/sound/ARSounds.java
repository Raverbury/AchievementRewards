package raverbury.achievement_rewards.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import raverbury.achievement_rewards.AchievementRewards;

public class ARSounds {

  public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AchievementRewards.MOD_ID);

  public static final RegistryObject<SoundEvent> ACTIVATOR_READY = registerSoundEvent("activator_ready");
  public static final RegistryObject<SoundEvent> ACTIVATOR_UNLEASH = registerSoundEvent("activator_unleash");

  private static RegistryObject<SoundEvent> registerSoundEvent(String name)
  {
    return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(AchievementRewards.MOD_ID, name)));
  }

  public static void registerSounds(IEventBus eventBus)
  {
    SOUND_EVENTS.register(eventBus);
  }
  
}
