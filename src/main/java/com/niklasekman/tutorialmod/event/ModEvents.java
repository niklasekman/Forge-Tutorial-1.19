package com.niklasekman.tutorialmod.event;

import com.niklasekman.tutorialmod.TutorialMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
    }
}
