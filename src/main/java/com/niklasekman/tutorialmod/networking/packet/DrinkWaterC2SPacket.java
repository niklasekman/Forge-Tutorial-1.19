package com.niklasekman.tutorialmod.networking.packet;

import com.niklasekman.tutorialmod.thirst.PlayerThirstProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DrinkWaterC2SPacket {
    private static final String MESSAGE_DRINK_WATER = "message.tutorialmod.drink_water";
    private static final String MESSAGE_NO_WATER = "message.tutorialmod.no_water";
    private static final String MESSAGE_THIRST_LEVEL = "message.tutorialmod.thirst_level";
    public DrinkWaterC2SPacket() {

    }

    public DrinkWaterC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // We are on the server
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            // Check if player is near water
            if(isNearWater(player, level,2)) {
                // Notify player that water has been drunk
                player.sendSystemMessage(Component.translatable(MESSAGE_DRINK_WATER).withStyle(ChatFormatting.DARK_AQUA));
                // increase thirst level
                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(playerThirst -> {
                    playerThirst.addThirst(1);
                    // output thirst level
                    player.sendSystemMessage(Component.literal("Thirst " + playerThirst.getThirst()));
                });
                // play drinking sound
                level.playSound(null, player.getOnPos(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.5f, level.random.nextFloat() * 0.1F + 0.9F);

            } else {
                // Notify player that there is no water
                player.sendSystemMessage(Component.translatable(MESSAGE_NO_WATER).withStyle(ChatFormatting.RED));
                // output thirst level
                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(playerThirst -> {
                    // output thirst level
                    player.sendSystemMessage(Component.literal("Thirst " + playerThirst.getThirst()));
                });
            }
        });
        return true;
    }

    private boolean isNearWater(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(blockState -> blockState.is(Blocks.WATER)).toArray().length > 0;
    }
}
