package com.awakenedredstone.durabilityview.config;

import com.awakenedredstone.durabilityview.DurabilityView;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClothConfig {
    public Screen build(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(Text.translatable("title.durability-view.config"));
        builder.setParentScreen(parent);

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.durability-view.general"));
        ConfigCategory blacklist = builder.getOrCreateCategory(Text.translatable("category.durability-view.blacklist"));
        ConfigCategory whitelist = builder.getOrCreateCategory(Text.translatable("category.durability-view.whitelist"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startEnumSelector(Text.translatable("option.durability-view.mode"), DisplayMode.class, DurabilityView.getConfig().mode)
                .setDefaultValue(DisplayMode.DEFAULT)
                .setSaveConsumer(mode -> DurabilityView.getConfig().mode = mode)
                .build());

        blacklist.addEntry(entryBuilder.startStrList(Text.translatable("option.durability-view.blacklist"), DurabilityView.getConfig().blacklist)
                .setDefaultValue(Collections.emptyList())
                .setSaveConsumer(list -> DurabilityView.getConfig().blacklist = list)
                .build());

        whitelist.addEntry(entryBuilder.startStrList(Text.translatable("option.durability-view.whitelist"), DurabilityView.getConfig().whitelist)
                .setDefaultValue(Collections.emptyList())
                .setSaveConsumer(list -> DurabilityView.getConfig().whitelist = list)
                .build());

        builder.setSavingRunnable(DurabilityView.CONFIG_MANAGER::save);
        return builder.build();
    }
}
