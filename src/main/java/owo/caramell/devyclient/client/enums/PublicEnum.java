package owo.caramell.devyclient.client.enums;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class PublicEnum {
    // Credits goes to Mojang.
    // Modified by CRML Studios.
    // Used for creating the "simplified" status bars
    @Environment(value= EnvType.CLIENT)
    public enum HeartType {
        CONTAINER(),
        NORMAL(),
        POISONED(),
        WITHERED(),
        ABSORBING(),
        FROZEN();

        public static PublicEnum.HeartType fromPlayerState(PlayerEntity player) {
            PublicEnum.HeartType heartType = player.hasStatusEffect(StatusEffects.POISON) ? POISONED : (player.hasStatusEffect(StatusEffects.WITHER) ? WITHERED : (player.isFrozen() ? FROZEN : NORMAL));
            return heartType;
        }
    }
}
