package com.niklasekman.tutorialmod.event;

import com.niklasekman.tutorialmod.TutorialMod;
import com.niklasekman.tutorialmod.thirst.PlayerThirst;
import com.niklasekman.tutorialmod.thirst.PlayerThirstProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = TutorialMod.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onLivingHurt(LivingHurtEvent event) {
            if(event.getSource().getEntity() instanceof Player player) {
                if(player.getMainHandItem().getItem() == Items.COD) {
                    player.sendSystemMessage(Component.literal(player.getName().getString() + " slapped " + event.getEntity().getName().getString() + " with a cod"));
                }
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof Player) {
                if(!event.getObject().getCapability(PlayerThirstProvider.PLAYER_THIRST).isPresent()) {
                    event.addCapability(new ResourceLocation(TutorialMod.MOD_ID, "properties"), new PlayerThirstProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            if(event.isWasDeath()) {
                event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(oldStore -> {
                    event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerThirst.class);
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if(event.side == LogicalSide.SERVER) {
                event.player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    if(thirst.getThirst() > 0 && event.player.getRandom().nextFloat() < 0.005f) {
                        thirst.removeThirst(1);
                        event.player.sendSystemMessage(Component.literal("Removed thirst"));
                    }
                });
            }
        }
    }
}
