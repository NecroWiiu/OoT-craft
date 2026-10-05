package com.ootcraft.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class OotCraftClient implements ClientModInitializer {
    public static boolean sm64Mode = false;
    public static final Identifier MARIO_SKIN = new Identifier("ootcraft", "textures/entity/mario.png");

    private static KeyBinding zTargetKey;
    private static KeyBinding modeKey;

    private HostileEntity target;
    private boolean wasOnGround = true;
    private int ticksSinceLanding = 99;
    private int jumpChain = 0;

    @Override
    public void onInitializeClient() {
        zTargetKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ootcraft.ztarget", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.ootcraft"));
        modeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ootcraft.sm64mode", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, "category.ootcraft"));
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void tick(MinecraftClient mc) {
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.world == null) {
            target = null;
            return;
        }

        while (modeKey.wasPressed()) {
            sm64Mode = !sm64Mode;
            jumpChain = 0;
            p.sendMessage(Text.literal(sm64Mode ? "SM64 mode: ON" : "SM64 mode: OFF"), true);
        }

        updateZTarget(mc, p);
        if (sm64Mode) updateTripleJump(p);
        wasOnGround = p.isOnGround();
    }

    // Hold Z: camera locks onto the nearest visible hostile mob, like OoT's Z-targeting.
    private void updateZTarget(MinecraftClient mc, ClientPlayerEntity p) {
        if (!zTargetKey.isPressed()) {
            target = null;
            return;
        }
        if (target == null || !target.isAlive() || target.squaredDistanceTo(p) > 24 * 24) {
            target = findTarget(mc, p);
        }
        if (target == null) return;

        Vec3d from = p.getEyePos();
        Vec3d to = target.getEyePos();
        double dx = to.x - from.x, dy = to.y - from.y, dz = to.z - from.z;
        double flat = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, flat));
        p.setYaw(MathHelper.lerpAngleDegrees(0.4f, p.getYaw(), yaw));
        p.setPitch(MathHelper.lerp(0.4f, p.getPitch(), pitch));
    }

    private HostileEntity findTarget(MinecraftClient mc, ClientPlayerEntity p) {
        List<HostileEntity> list = mc.world.getEntitiesByClass(
                HostileEntity.class, p.getBoundingBox().expand(20), e -> e.isAlive() && p.canSee(e));
        HostileEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (HostileEntity e : list) {
            double d = e.squaredDistanceTo(p);
            if (d < bestDist) {
                bestDist = d;
                best = e;
            }
        }
        return best;
    }

    // SM64 mode: chain jumps quickly for a bigger 2nd and 3rd jump.
    private void updateTripleJump(ClientPlayerEntity p) {
        boolean onGround = p.isOnGround();
        if (!wasOnGround && onGround) ticksSinceLanding = 0;
        else if (ticksSinceLanding < 99) ticksSinceLanding++;

        Vec3d v = p.getVelocity();
        boolean justJumped = wasOnGround && !onGround && v.y > 0.3;
        if (justJumped) {
            jumpChain = ticksSinceLanding <= 12 ? Math.min(jumpChain + 1, 3) : 1;
            double mult = jumpChain == 2 ? 1.25 : jumpChain == 3 ? 1.6 : 1.0;
            p.setVelocity(v.x, v.y * mult, v.z);
            if (jumpChain == 3) jumpChain = 0;
        }
    }
}
