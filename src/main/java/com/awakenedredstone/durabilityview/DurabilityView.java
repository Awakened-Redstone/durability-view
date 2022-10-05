package com.awakenedredstone.durabilityview;

import com.awakenedredstone.durabilityview.config.Config;
import com.awakenedredstone.durabilityview.config.ConfigManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DurabilityView implements ClientModInitializer {
	public static final String MOD_ID = "durability-view";
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ConfigManager CONFIG_MANAGER = new ConfigManager();

	public static Config getConfig() {
		return CONFIG_MANAGER.getConfig();
	}

	@Override
	public void onInitializeClient() {
		CONFIG_MANAGER.loadOrCreateConfig();
		LOGGER.info("Hello Fabric world!");
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if (stack.isDamageable() && !context.isAdvanced()) {
				Identifier identifier = Registry.ITEM.getId(stack.getItem());
				String global = String.format("%s:*", identifier.getNamespace());

				if (getConfig().blacklist.contains(identifier.toString()) || (getConfig().blacklist.contains(global) && !getConfig().whitelist.contains(identifier.toString()))) return;

				int durability = stack.getMaxDamage() - stack.getDamage();
				lines.add(Text.literal(""));
				switch (getConfig().mode) {
					case DEFAULT -> lines.add(Text.translatable("item.durability", durability, stack.getMaxDamage()).formatted(Formatting.DARK_GRAY));
					case PERCENTAGE -> lines.add(Text.translatable("item.durabilityPercent", (int) Math.floor(((float) durability / (float) stack.getMaxDamage()) * 100)).formatted(Formatting.DARK_GRAY));
				}
			}
		});
	}
}
