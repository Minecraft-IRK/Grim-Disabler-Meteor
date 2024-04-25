package com.iroak.addon.modules;

import com.iroak.addon.Addon;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.MovementType;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;

public class TridentFly extends Module {

    private int currentTick = 0;
    private boolean isActive = false;
    private final SettingGroup settingsModule = settings.getDefaultGroup();

    public TridentFly() {
        super(Addon.CATEGORY, "TridentFly", "Fly with Trident Spamming Riptide III. (Active GrimDisabler)");
    }

    private final Setting<Integer> tridentDelay = settingsModule.add(
        new IntSetting.Builder()
            .name("Delay")
            .description("delay (in ticks) between trident uses")
            .sliderRange(0, 20)
            .range(0, 20)
            .defaultValue(0)
            .build()
    );

    @Override
    public void onActivate() {
        isActive = true;
    }

    @Override
    public void onDeactivate() {
        isActive = false;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (currentTick >= tridentDelay.get() && isActive) {
            currentTick = 0;
            assert mc.player != null;
            float f = mc.player.getYaw();
            float g = mc.player.getPitch();
            float h = -MathHelper.sin(f * (float) (Math.PI / 180.0)) * MathHelper.cos(g * (float) (Math.PI / 180.0));
            float k = -MathHelper.sin(g * (float) (Math.PI / 180.0));
            float l = MathHelper.cos(f * (float) (Math.PI / 180.0)) * MathHelper.cos(g * (float) (Math.PI / 180.0));
            float m = MathHelper.sqrt(h * h + k * k + l * l);
            float n = 3.0F;
            h *= n / m;
            k *= n / m;
            l *= n / m;
            mc.player.addVelocity(h, k, l);
            if (mc.player.isOnGround()) {
                mc.player.move(MovementType.SELF, new Vec3d(0.0, 1.1999999F, 0.0));
            }
        } else {
            currentTick++;
        }
    }

}
