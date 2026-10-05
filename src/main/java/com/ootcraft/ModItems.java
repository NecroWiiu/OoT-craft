package com.ootcraft;

import com.ootcraft.item.MasterSwordMaterial;
import com.ootcraft.item.OcarinaItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item OCARINA = register("ocarina",
            new OcarinaItem(new FabricItemSettings().maxCount(1)));

    public static final Item MASTER_SWORD = register("master_sword",
            new SwordItem(MasterSwordMaterial.INSTANCE, 3, -2.0f, new FabricItemSettings().fireproof()));

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(OotCraft.MOD_ID, id), item);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(e -> e.add(OCARINA));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(e -> e.add(MASTER_SWORD));
    }
}
