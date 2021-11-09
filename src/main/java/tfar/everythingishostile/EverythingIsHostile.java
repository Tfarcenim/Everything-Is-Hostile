package tfar.everythingishostile;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EverythingIsHostile.MODID)
public class EverythingIsHostile
{
    // Directly reference a log4j logger.

    public static final String MODID = "everythingishostile";

    public EverythingIsHostile() {
        MinecraftForge.EVENT_BUS.addListener(this::spawn);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::addAttribute);
    }

    private void spawn(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof CreatureEntity) {
            CreatureEntity mob = (CreatureEntity)e.getEntity();
            mob.goalSelector.addGoal(3, new MeleeAttackGoal(mob, 1.5D, false) {
                @Override
                public void tick() {
                    if (attacker.getAttackTarget() != null) {
                        super.tick();
                    }
                }
            });
            mob.goalSelector.addGoal(1, new NearestAttackableTargetGoal(mob, PlayerEntity.class, true){
                @Override
                protected double getTargetDistance() {
                    return 16;
                }
            });
        }
    }


    private void addAttribute(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> entityType : event.getTypes()) {
            if (!event.has(entityType, Attributes.ATTACK_DAMAGE)) {
                event.add(entityType,Attributes.ATTACK_DAMAGE,2);
            }
        }
    }
}
