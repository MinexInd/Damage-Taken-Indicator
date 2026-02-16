package net.minex;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class DamageTakenIndicatorClient implements ClientModInitializer {

    private float lastHealth = -1;
    private float displayedValue = 0; // Can be damage (positive) or healing (negative)
    private int displayTicksLeft = 0;
    private static final int DISPLAY_DURATION = 20;
    
    // Logic for accumulation
    private int lastHitTimer = 0;
    private boolean isDamage = true; // true = damage, false = healing

    @Override
    public void onInitializeClient() {
        DamageIndicatorConfig.load();

        // Register tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                lastHealth = -1;
                displayTicksLeft = 0;
                lastHitTimer = 0;
                return;
            }

            // Auto-reload config if file changed
            DamageIndicatorConfig.checkReload();

            float currentHealth = client.player.getHealth() + client.player.getAbsorptionAmount();

            if (lastHealth == -1) {
                lastHealth = currentHealth;
            }

            float diff = lastHealth - currentHealth;

            // Damage Detected (Health Decreased)
            if (diff > 0.001) {
                if (DamageIndicatorConfig.instance.enableAccumulation && displayTicksLeft > 0 && isDamage) {
                    displayedValue += diff;
                } else {
                    displayedValue = diff;
                }
                isDamage = true;
                displayTicksLeft = DISPLAY_DURATION;
                lastHitTimer = DamageIndicatorConfig.instance.accumulationResetTicks;
            }
            // Healing Detected (Health Increased)
            else if (diff < -0.001) {
                float healAmount = -diff;
                if (DamageIndicatorConfig.instance.enableAccumulation && displayTicksLeft > 0 && !isDamage) {
                    displayedValue += healAmount;
                } else {
                    displayedValue = healAmount;
                }
                isDamage = false;
                displayTicksLeft = DISPLAY_DURATION;
                lastHitTimer = DamageIndicatorConfig.instance.accumulationResetTicks;
            }

            lastHealth = currentHealth;

            if (displayTicksLeft > 0) {
                displayTicksLeft--;
            }
            if (lastHitTimer > 0) {
                lastHitTimer--;
            }
        });

        // Register render event
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (displayTicksLeft > 0) {
                MinecraftClient client = MinecraftClient.getInstance();
                TextRenderer textRenderer = client.textRenderer;
                
                String symbol = isDamage ? "-" : "+";
                String text = symbol + String.format("%.1f", displayedValue);
                
                // Configurable Scale
                float scale = DamageIndicatorConfig.instance.scale;
                
                // Critical Hit Check
                boolean isCritical = isDamage && displayedValue >= DamageIndicatorConfig.instance.criticalThreshold;
                if (isCritical) {
                    scale *= 1.5f; // Make critical hits 50% larger
                }

                int width = textRenderer.getWidth(text);
                int height = client.getWindow().getScaledHeight();
                int screenWidth = client.getWindow().getScaledWidth();
                
                float ageInTicks = (DISPLAY_DURATION - displayTicksLeft);
                float animationProgress = ageInTicks / (float)DISPLAY_DURATION;
                
                // Position logic
                float startY = (height / 2.0f) + 10 + DamageIndicatorConfig.instance.yOffset;
                float endY = (height / 2.0f) - 20 + DamageIndicatorConfig.instance.yOffset;
                
                // Healing floats DOWN, Damage floats UP
                if (!isDamage) {
                    float temp = startY;
                    startY = endY;
                    endY = temp;
                }

                float currentY = startY + (endY - startY) * animationProgress;
                int x = ((screenWidth - width) / 2) + DamageIndicatorConfig.instance.xOffset;
                int y = (int) currentY;

                // Colors from Config (Strings)
                int baseColor;
                if (isDamage) {
                    baseColor = DamageIndicatorConfig.getColor(isCritical ? DamageIndicatorConfig.instance.criticalColor : DamageIndicatorConfig.instance.damageColor);
                } else {
                    baseColor = DamageIndicatorConfig.getColor(DamageIndicatorConfig.instance.healingColor);
                }
                
                // Fade out
                float alpha = 1.0f;
                if (animationProgress > 0.5f) {
                    alpha = 1.0f - ((animationProgress - 0.5f) * 2);
                }
                
                int alphaInt = (int)(alpha * 255);
                alphaInt = Math.max(0, Math.min(255, alphaInt));
                
                int color = (alphaInt << 24) | (baseColor & 0x00FFFFFF);

                // Render with Scale
                drawContext.getMatrices().pushMatrix();
                drawContext.getMatrices().translate(x + (width / 2.0f), y + (textRenderer.fontHeight / 2.0f));
                drawContext.getMatrices().scale(scale, scale);
                drawContext.getMatrices().translate(-(x + (width / 2.0f)), -(y + (textRenderer.fontHeight / 2.0f)));
                
                drawContext.drawTextWithShadow(textRenderer, text, x, y, color);
                
                drawContext.getMatrices().popMatrix();
            }
        });
    }
}
