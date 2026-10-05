package com.ootcraft.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class MasterSwordMaterial implements ToolMaterial {
    public static final MasterSwordMaterial INSTANCE = new MasterSwordMaterial();

    @Override public int getDurability() { return 4000; }
    @Override public float getMiningSpeedMultiplier() { return 9.0f; }
    @Override public float getAttackDamage() { return 5.0f; } // total sword damage = 3 + 5
    @Override public int getMiningLevel() { return 4; }
    @Override public int getEnchantability() { return 15; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.ofItems(Items.NETHERITE_INGOT); }
}
