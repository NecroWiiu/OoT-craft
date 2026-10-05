package com.ootcraft;

import net.fabricmc.api.ModInitializer;

public class OotCraft implements ModInitializer {
    public static final String MOD_ID = "ootcraft";

    @Override
    public void onInitialize() {
        ModItems.init();
    }
}
