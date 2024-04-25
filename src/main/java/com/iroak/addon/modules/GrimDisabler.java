package com.iroak.addon.modules;

import com.iroak.addon.Addon;
import net.minecraft.util.Hand;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;


public class GrimDisabler extends Module {

    private int currentTick = 0;

    private final SettingGroup settingsModule = settings.getDefaultGroup();

    public GrimDisabler() {
        super(Addon.CATEGORY, "Grim Disabler", "Disables GrimAC move checks (need a trident with Riptide III)");
    }

    private final Setting<Integer> tridentDelay = settingsModule.add(
        new IntSetting.Builder()
            .name("Trident delay")
            .description("delay (in ticks) between trident uses")
            .sliderRange(0, 20)
            .range(0, 20)
            .defaultValue(0)
            .build()
    );

    private final Setting<Boolean> pauseOnEat = settingsModule.add(
        new BoolSetting.Builder()
            .name("Pause on eat")
            .description("Pauses when eating.")
            .defaultValue(false)
            .build()
    );

    @Override
    public void onActivate() {
        currentTick = tridentDelay.get();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (currentTick >= tridentDelay.get()){
            currentTick = 0;
            assert mc.player != null;
            int tridentSlot = InvUtils.findInHotbar(Items.TRIDENT).slot();
            int oldSlot = mc.player.getInventory().selectedSlot;
            if (tridentSlot == -1 || (pauseOnEat.get() && mc.player.isUsingItem())) return;
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(tridentSlot));
            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(oldSlot));
        } else {
            currentTick++;
        }
    }
}
