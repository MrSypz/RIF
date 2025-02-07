package sypztep.rif;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.Arrays;
import java.util.List;

@Config(name = Rif.MODID)
public class ModConfig implements ConfigData {
    @Comment("Excluded the damage intake that by-pass Iframe")
    @ConfigEntry.Category("gameplay")
    public static List<String> dmgReceiveExcludedEntities = Arrays.asList(
            "minecraft:slime", "minecraft:magma_cube"
    );

    @Comment("Excluded the Entities that by-pass Iframe")
    @ConfigEntry.Category("gameplay")
    public static List<String> attackExcludedEntities = List.of(
            "minecraft:warden"
    );

    @ConfigEntry.Category("gameplay")
    @Comment("How weak a player's attack can be before it gets nullified, from 0 (0%, cancels multiple attacks on the same tick) to 1 (100%, players cannot attack), or -0.1 (disables this feature)")
    public static float attackCancelThreshold = 0.15f;

    @ConfigEntry.Category("gameplay")
    @Comment("How weak a player's attack can be before the knockback gets nullified, from 0 (0%, cancels multiple attacks on the same tick) to 1 (100%, no knockback), or -0.1 (disables this feature)")
    public static float knockbackCancelThreshold = 0.75f;

    @ConfigEntry.Category("gameplay")
    @Comment("How many ticks of i-frames does an entity get when damaged, from 10 (default) 0.5 sec, to 2^31-1 (nothing can take damage)")
    public static int iFrameDuration = 10;

    @ConfigEntry.Category("gameplay")
    @Comment("Time threshold in milliseconds for counting rapid attacks. Lower values make it harder to trigger rapid attack penalties")
    public static long rapidAttackThreshold = 250;

    @ConfigEntry.Category("gameplay")
    @Comment("Damage reduction per rapid attack (0.2 = 20% reduction per hit). Set to 0 to disable")
    public static float rapidAttackDamageFalloff = 0.2f;

    @ConfigEntry.Category("gameplay")
    @Comment("Maximum number of rapid attacks tracked for damage falloff")
    public static int maxRapidAttackCount = 5;

    @ConfigEntry.Category("gameplay")
    @Comment("Minimum damage multiplier for rapid attacks (0.2 = 20% minimum damage)")
    public static float minRapidAttackDamage = 0.2f;

    @ConfigEntry.Category("gameplay")
    @Comment("Time in milliseconds before rapid attack count starts resetting")
    public static long rapidAttackResetTime = 1000;
}