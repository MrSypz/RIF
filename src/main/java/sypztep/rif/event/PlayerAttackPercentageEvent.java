package sypztep.rif.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import sypztep.rif.ModConfig;
import sypztep.rif.util.PlayerAttackCallback;
import sypztep.rif.util.PlayerEntityAccessor;

public class PlayerAttackPercentageEvent implements PlayerAttackCallback {
    @Override
    public ActionResult attackEntity(PlayerEntity player, Entity target) {
        if (player.getEntityWorld().isClient)
            return ActionResult.PASS;

        float str = player.getAttackCooldownProgress(0);
        if (str <= ModConfig.attackCancelThreshold)
            return ActionResult.FAIL;
        if (str <= ModConfig.knockbackCancelThreshold) {
            PlayerEntityAccessor playerAccessor = (PlayerEntityAccessor) player;
            playerAccessor.setSwingingHand(true);
        }

        return ActionResult.PASS;
    }
}
