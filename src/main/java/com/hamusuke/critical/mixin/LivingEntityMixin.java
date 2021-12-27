package com.hamusuke.critical.mixin;

import com.hamusuke.critical.Critical;
import com.hamusuke.critical.invoker.LivingEntityInvoker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.function.Consumer;

@Mixin(EntityLivingBase.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityInvoker {
    @Shadow
    public abstract ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn);

    private static final DataParameter<Float> CRIT_RATE = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> CRIT_DMG = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    private final Random critRateRandom = new Random();

    LivingEntityMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "entityInit", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo ci) {
        this.dataManager.register(CRIT_RATE, 5.0F);
        this.dataManager.register(CRIT_DMG, 50.0F);
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void write(NBTTagCompound compound, CallbackInfo ci) {
        compound.setFloat("CritRate", this.getCritRate());
        compound.setFloat("CritDamage", this.getCritDamage());
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void read(NBTTagCompound compound, CallbackInfo ci) {
        if (compound.hasKey("CritRate")) {
            this.setCritRate(compound.getFloat("CritRate"));
        }

        if (compound.hasKey("CritDamage")) {
            this.setCritDamage(compound.getFloat("CritDamage"));
        }
    }

    @Override
    public Random getRand() {
        return this.critRateRandom;
    }

    @Override
    public void setCritRate(float critRate) {
        this.dataManager.set(CRIT_RATE, critRate);
    }

    @Override
    public void setCritDamage(float critDamage) {
        this.dataManager.set(CRIT_DMG, critDamage);
    }

    private void forEachEnchantmentExceptOffHand(Consumer<ItemStack> itemStackConsumer) {
        if (!this.world.isRemote) {
            for (EntityEquipmentSlot equipmentSlot : EntityEquipmentSlot.values()) {
                if (equipmentSlot != EntityEquipmentSlot.OFFHAND) {
                    ItemStack itemStack = this.getItemStackFromSlot(equipmentSlot);
                    if (!itemStack.isEmpty()) {
                        itemStackConsumer.accept(itemStack);
                    }
                }
            }
        }
    }

    @Override
    public float getCritRate() {
        return this.dataManager.get(CRIT_RATE);
    }

    @Override
    public float getCritRateFromEquipped() {
        MutableFloat additionalRate = new MutableFloat();
        this.forEachEnchantmentExceptOffHand(itemStack -> additionalRate.add(Critical.CRIT_RATE.getPercent(EnchantmentHelper.getEnchantmentLevel(Critical.CRIT_RATE, itemStack))));
        return additionalRate.floatValue();
    }

    @Override
    public float getCritDamage() {
        return this.dataManager.get(CRIT_DMG);
    }

    @Override
    public float getCritDamageFromEquipped() {
        MutableFloat additionalDamage = new MutableFloat();
        this.forEachEnchantmentExceptOffHand(itemStack -> additionalDamage.add(Critical.CRIT_DAMAGE.getPercent(EnchantmentHelper.getEnchantmentLevel(Critical.CRIT_DAMAGE, itemStack))));
        return additionalDamage.floatValue();
    }
}
