package com.mirania.helperguimod;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;

public class GuiData {

    private final ChatFormatting titleColor = ChatFormatting.GRAY;

    private final ChatFormatting normalColor = ChatFormatting.WHITE;

    private final long fullDayIngameTicks = 24000;

    @Nullable
    public Component getPlayerPosition() {
        if (Minecraft.getInstance().getCameraEntity() == null) {
            return null;
        }

        final Entity camera = Minecraft.getInstance().getCameraEntity();
        final String output = "%sxyz: %s%.0f / %.0f / %.0f".formatted(
                this.titleColor,
                this.normalColor,
                camera.getX(),
                camera.getY(),
                camera.getZ()
        );
        return Component.literal(output);
    }

    @Nullable
    public Component getPlayerDirection() {
        if (Minecraft.getInstance().getCameraEntity() == null) {
            return null;
        }

        final Direction direction = Minecraft.getInstance().getCameraEntity().getDirection();
        final String output = "%sFacing: %s%s (%s)".formatted(
                this.titleColor,
                this.normalColor,
                direction,
                this.convertToAxis(direction)
        );
        return Component.literal(output);
    }

    @Nullable
    public Component getBiome() {
        if (Minecraft.getInstance().getCameraEntity() == null) {
            return null;
        }

        final Entity camera = Minecraft.getInstance().getCameraEntity();
        final String output = "%sBiome: %s%s".formatted(
                this.titleColor,
                this.normalColor,
                this.convertToReadableName(camera.getLevel().getBiome(camera.blockPosition()))
        );
        return Component.literal(output);
    }

    @Nullable
    public Component getIngameTime() {
        if (Minecraft.getInstance().level == null) {
            return null;
        }

        final long dayIngameTicks = Minecraft.getInstance().level.getDayTime() % this.fullDayIngameTicks;
        final String output = "%sTime: %s%s (%s)".formatted(
                this.titleColor,
                this.normalColor,
                this.convertToReadableTime(dayIngameTicks),
                this.convertToDayOrNight(dayIngameTicks)
        );
        return Component.literal(output);
    }

    @Nullable
    public Component getLight() {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.getCameraEntity() == null || mc.level == null) {
            return null;
        }

        final String output = "%sLight: %s%d".formatted(
                this.titleColor,
                this.normalColor,
                mc.level.getChunkSource().getLightEngine().getRawBrightness(mc.getCameraEntity().blockPosition(), 0)
        );
        return Component.literal(output);
    }

    private String convertToAxis(final Direction direction) {
        return switch (direction) {
            case NORTH -> "-z";
            case SOUTH -> "+z";
            case WEST -> "-x";
            case EAST -> "+x";
            case UP -> "+y";
            case DOWN -> "-y";
        };
    }

    private String convertToReadableName(final Holder<Biome> biomeHolder) {
        return biomeHolder.unwrap().map(
                knownBiome -> {
                    final String[] location = knownBiome.location().toString().split(":");
                    return location.length > 1 ? location[location.length - 1] : location[0];
                },
                unknownBiome -> "unknown?"
        );
    }

    // ticks are 0-23999. tick 0 is 06:00 AM.
    public String convertToReadableTime(final long dayIngameTicks) {
        final long hour = (dayIngameTicks / 1000 + 6) % 24; // 23912 -> hour 23 -> add 6 and wrap around -> hour 5
        final double minutes = Math.floor((dayIngameTicks % 1000) * 0.06); // 23912 -> remainder 912 -> 91.2% of an hour -> minute 54
        return "%02d:%02.0f".formatted(hour, minutes);
    }

    public String convertToDayOrNight(final long dayIngameTicks) {
        final boolean isNight = dayIngameTicks >= 13000 && dayIngameTicks <= 23000;
        return isNight ? "night" : "day";
    }

}
