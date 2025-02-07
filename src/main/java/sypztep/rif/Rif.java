package sypztep.rif;

import net.fabricmc.api.ModInitializer;
import sypztep.rif.event.HurtDurationEvent;
import sypztep.rif.event.KnockBackThresoEvent;
import sypztep.rif.event.PlayerAttackPercentageEvent;
import sypztep.rif.util.EntityHurtCallback;
import sypztep.rif.util.EntityKnockbackCallback;
import sypztep.rif.util.PlayerAttackCallback;

public class Rif implements ModInitializer {
    public static final String MODID = "rif";

    @Override
    public void onInitialize() {
        EntityHurtCallback.EVENT.register(new HurtDurationEvent());
        EntityKnockbackCallback.EVENT.register(new KnockBackThresoEvent());
        PlayerAttackCallback.EVENT.register(new PlayerAttackPercentageEvent());
    }
}
