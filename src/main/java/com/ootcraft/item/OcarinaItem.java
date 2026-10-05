package com.ootcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class OcarinaItem extends Item {
    // Uses the vanilla flute sound, so no external audio is needed.
    private static final SoundEvent FLUTE = SoundEvent.of(new Identifier("minecraft", "block.note_block.flute"));

    public OcarinaItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean sunsSong = user.isSneaking();
        float pitch = sunsSong ? 1.5f : 0.7f + world.random.nextInt(8) * 0.1f;
        world.playSound(null, user.getX(), user.getY(), user.getZ(), FLUTE, SoundCategory.PLAYERS, 1.0f, pitch);

        if (sunsSong) {
            user.getItemCooldownManager().set(this, 60);
            if (world instanceof ServerWorld sw) {
                long t = sw.getTimeOfDay();
                long day = t / 24000L * 24000L;
                long tod = t % 24000L;
                // Daytime -> dusk. Night -> next morning.
                sw.setTimeOfDay(tod < 12500L ? day + 13000L : day + 24000L + 1000L);
            }
            user.sendMessage(Text.literal("Sun's Song"), true);
        } else {
            user.getItemCooldownManager().set(this, 8);
        }
        return TypedActionResult.success(stack, world.isClient());
    }
}
