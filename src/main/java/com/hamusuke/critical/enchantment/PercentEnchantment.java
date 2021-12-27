package com.hamusuke.critical.enchantment;

import com.hamusuke.critical.Critical;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class PercentEnchantment extends Enchantment {
    protected PercentEnchantment(Rarity weight) {
        super(weight, EnumEnchantmentType.ALL, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD});
    }

    @Override
    public String getName() {
        return "enchantment." + Critical.MOD_ID + "." + this.name;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    public float getPercent(int level) {
        return (float) level * 3.8F;
    }

    @Override
    public String getTranslatedName(int level) {
        return new TextComponentTranslation(this.getName()).appendText(" ").appendText(String.format("%.1f%%", this.getPercent(level))).setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText();
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return true;
    }
}
