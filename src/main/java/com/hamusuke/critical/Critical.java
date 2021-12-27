package com.hamusuke.critical;

import com.hamusuke.critical.enchantment.CritDamageEnchantment;
import com.hamusuke.critical.enchantment.CritRateEnchantment;
import com.hamusuke.critical.enchantment.PercentEnchantment;
import com.hamusuke.critical.invoker.LivingEntityInvoker;
import com.hamusuke.critical.server.command.GetCritCommand;
import com.hamusuke.critical.server.command.SetCritCommand;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Critical.MOD_ID, name = Critical.MOD_NAME, version = Critical.VERSION, updateJSON = "https://raw.githubusercontent.com/hamusuke0323/CriticalModForge/update/update.json", dependencies = "required-after:criticalib@[1.0.2,);")
@Mod.EventBusSubscriber
public class Critical {
    public static final String MOD_ID = "critical";
    public static final String MOD_NAME = "Critical Mod";
    public static final String VERSION = "1.0.0";
    public static final PercentEnchantment CRIT_RATE = new CritRateEnchantment();
    public static final PercentEnchantment CRIT_DAMAGE = new CritDamageEnchantment();

    public Critical() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onCommandsRegistry(final FMLServerStartingEvent event) {
        event.registerServerCommand(new SetCritCommand());
        event.registerServerCommand(new GetCritCommand());
    }

    @SubscribeEvent
    public static void onEnchantmentsRegistry(final RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().registerAll(CRIT_RATE, CRIT_DAMAGE);
    }

    @SubscribeEvent
    public void onCritHit(final CriticalHitEvent event) {
        LivingEntityInvoker invoker = (LivingEntityInvoker) event.getEntityPlayer();
        float totalCritRate = invoker.getTotalCritRate();
        float totalCritDMG = invoker.getTotalCritDamage();
        if (invoker.toCriticaLib().isCritical() || (totalCritDMG > 0.0F && totalCritRate > 0.0F && (totalCritRate >= 100.0F || invoker.getRand().nextFloat() < totalCritRate / 100.0F))) {
            invoker.toCriticaLib().setCritical(true);
            event.setResult(Event.Result.ALLOW);
            event.setDamageModifier((100.0F + totalCritDMG) / 100.0F);
        }
    }

    @SubscribeEvent
    public void onHurt(final LivingHurtEvent event) {
        EntityLivingBase livingEntity = event.getEntityLiving();
        float amount = event.getAmount();
        if (!livingEntity.world.isRemote) {
            DamageSource source = event.getSource();
            if (source instanceof EntityDamageSourceIndirect && source.getTrueSource() instanceof LivingEntityInvoker && source.getImmediateSource() instanceof EntityArrow) {
                LivingEntityInvoker livingEntityInvoker = (LivingEntityInvoker) source.getTrueSource();
                livingEntityInvoker.toCriticaLib().setCritical(((EntityArrow) source.getImmediateSource()).getIsCritical());
                amount = livingEntityInvoker.calculateCritDamage(amount);
            } else if (source instanceof EntityDamageSource && !(source.getTrueSource() instanceof EntityPlayer) && source.getTrueSource() instanceof LivingEntityInvoker) {
                amount = ((LivingEntityInvoker) source.getTrueSource()).calculateCritDamage(amount);
            }
        }
        event.setAmount(amount);
    }
}
