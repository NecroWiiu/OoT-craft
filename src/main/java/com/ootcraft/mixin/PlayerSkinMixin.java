package com.ootcraft.mixin;

import com.ootcraft.client.OotCraftClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class PlayerSkinMixin {
    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    private void ootcraft$marioSkin(CallbackInfoReturnable<Identifier> cir) {
        if (OotCraftClient.sm64Mode && (Object) this == MinecraftClient.getInstance().player) {
            cir.setReturnValue(OotCraftClient.MARIO_SKIN);
        }
    }
}
