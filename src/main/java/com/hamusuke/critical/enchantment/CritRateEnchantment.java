package com.hamusuke.critical.enchantment;

import com.hamusuke.critical.Critical;
import net.minecraft.enchantment.Enchantment;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CritRateEnchantment extends PercentEnchantment {
    public CritRateEnchantment() {
        super(Rarity.UNCOMMON);
        this.setName("crit_rate");
        this.setRegistryName(Critical.MOD_ID, "crit_rate");
    }

    @Override
    protected boolean canApplyTogether(Enchantment enchantment) {
        return !(enchantment instanceof CritRateEnchantment);
    }
}
