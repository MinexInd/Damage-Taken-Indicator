# 🛠️ Damage Taken Indicator - Player's Config Guide

This mod is designed to be fully customizable! You can change colors, sizes, and even how damage numbers add up, all while the game is running.

---

## 📂 Where is my Config?
Once you run the mod for the first time, a file will be created at:
`.minecraft/config/damage-taken-indicator.json`

Open this file with any text editor (like Notepad, TextEdit, or VS Code) to start customizing!

---

## 🎨 Visual Settings

### `damageColor`, `healingColor`, & `criticalColor`
Choose the colors that appear for different health changes.
- **Accepted Colors:** `RED`, `GREEN`, `GOLD`, `BLUE`, `YELLOW`, `WHITE`, `AQUA`, `PURPLE`.
- **Damage (Default: RED):** Shown when you lose health.
- **Healing (Default: GREEN):** Shown when you gain health or absorption.
- **Critical (Default: GOLD):** Shown for big hits that pass your threshold.

### `scale`
How big should the numbers be?
- **Default:** `1.0`
- **Tip:** Set to `1.5` for a punchier look, or `0.8` if you want them subtle.

---

## 📍 Positioning

### `xOffset` & `yOffset`
Move the numbers to fit your custom HUD.
- **xOffset:** Positive numbers move text **Right**, negative moves it **Left**.
- **yOffset:** Positive numbers move text **Down**, negative moves it **Up**.
- **Default:** `0` (Center of the screen).

---

## ⚔️ Combat Logic

### `criticalThreshold`
What counts as a "Big Hit"?
- **Value:** Number of points (2 points = 1 heart).
- **Default:** `4.0` (2 hearts).
- **Effect:** Hits larger than this use the `criticalColor` and appear **50% larger**.

### `enableAccumulation`
Should fast hits add up?
- **Options:** `true` (Enabled), `false` (Disabled).
- **Default:** `true`
- **Example:** If you take 1 damage 5 times quickly, you'll see the number grow from `1.0` to `5.0` instead of 5 separate `1.0`s.

### `accumulationResetTicks`
How long to wait before starting a new total?
- **Value:** Ticks (20 ticks = 1 second).
- **Default:** `40` (2 seconds).
- **Tip:** If you're fighting fast mobs, increase this to see even bigger totals!

---

## ⚡ Live Reloading (The Best Part!)
You **do not** need to restart Minecraft to see your changes.
1. Change a setting in the `.json` file.
2. **Save the file.**
3. Tab back into Minecraft—the next damage you take will use your new settings immediately!

---
*Happy Modding! If you find a bug, please report it on the mod page.*
