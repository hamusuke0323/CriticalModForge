package com.hamusuke.critical.enchantment;

import com.hamusuke.critical.Critical;
import net.minecraft.enchantment.Enchantment;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CritDamageEnchantment extends PercentEnchantment {
    public CritDamageEnchantment() {
        super(Rarity.RARE);
        this.setName("crit_damage");
        this.setRegistryName(Critical.MOD_ID, "crit_damage");
    }

    @Override
    public float getPercent(int level) {
        return super.getPercent(level) * 2.0F;
    }

    @Override
    protected boolean canApplyTogether(Enchantment enchantment) {
        return !(enchantment instanceof CritDamageEnchantment);
    }
}
