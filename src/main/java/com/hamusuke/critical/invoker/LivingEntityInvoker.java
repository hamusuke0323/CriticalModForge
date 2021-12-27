package com.hamusuke.critical.invoker;

import com.hamusuke.criticalib.invoker.EntityLivingBaseInvoker;

import java.util.Random;

public interface LivingEntityInvoker {
    default float calculateCritDamage(float amount) {
        float totalCritRate = this.getTotalCritRate();
        float totalCritDMG = this.getTotalCritDamage();
        if (this.toCriticaLib().isCritical() || (totalCritDMG > 0.0F && totalCritRate > 0.0F && (totalCritRate >= 100.0F || this.getRand().nextFloat() < totalCritRate / 100.0F))) {
            this.toCriticaLib().setCritical(true);
            return amount * (100.0F + totalCritDMG) / 100.0F;
        }

        return amount;
    }

    Random getRand();

    default void setCritRate(float critRate) {
    }

    default void setCritDamage(float critDamage) {
    }

    default float getCritRate() {
        return 5.0F;
    }

    default float getCritRateFromEquipped() {
        return 0.0F;
    }

    default float getTotalCritRate() {
        return this.getCritRate() + this.getCritRateFromEquipped();
    }

    default float getCritDamage() {
        return 50.0F;
    }

    default float getCritDamageFromEquipped() {
        return 0.0F;
    }

    default float getTotalCritDamage() {
        return this.getCritDamage() + this.getCritDamageFromEquipped();
    }

    default EntityLivingBaseInvoker toCriticaLib() {
        return (EntityLivingBaseInvoker) this;
    }
}
