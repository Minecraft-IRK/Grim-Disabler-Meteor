package com.iroak.addon;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.iroak.addon.modules.Discord;
import com.iroak.addon.modules.GrimDisabler;
import com.iroak.addon.modules.TridentFly;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;


public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("iroaK");

    @Override
    public void onInitialize() {
        LOG.info("Initializing iroaK Addon.");
        Modules.get().add(new GrimDisabler());
        Modules.get().add(new TridentFly());
        Modules.get().add(new Discord());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.iroak.addon";
    }

}
