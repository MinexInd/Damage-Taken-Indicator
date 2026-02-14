package net.minex;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamageTakenIndicator implements ModInitializer {
	public static final String MOD_ID = "damage-taken-indicator";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Damage Taken Indicator initialized.");
	}
}
