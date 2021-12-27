package com.hamusuke.critical.mixin;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLiving.class)
public abstract class MobEntityMixin extends LivingEntityMixin {
    MobEntityMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onInitialSpawn", at = @At("TAIL"))
    private void init(DifficultyInstance difficulty, IEntityLivingData livingData, CallbackInfoReturnable<IEntityLivingData> cir) {
        float d = difficulty.getClampedAdditionalDifficulty();
        boolean bl = this.world.getDifficulty() == EnumDifficulty.HARD;
        d = bl ? 1.5F : d;
        this.setCritRate(this.getCritRate() + (bl ? 0.5F : 0.0F + this.rand.nextFloat()) * 50.0F * d);
        this.setCritDamage(this.getCritDamage() + (bl ? 0.5F : 0.0F + this.rand.nextFloat()) * 50.0F * d);
    }
}
