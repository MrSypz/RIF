package sypztep.rif.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.rif.ModConfig;
import sypztep.rif.util.EntityHurtCallback;
import sypztep.rif.util.PlayerAttackCallback;
import sypztep.rif.util.PlayerEntityAccessor;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityAccessor {
	@Unique
	private boolean swinging = false;

	@Unique
	private long lastAttackTime = 0L;

	@Unique
	private int rapidAttackCount = 0;

	@Override
	public void setSwingingHand(boolean swinging) {
		this.swinging = swinging;
	}

	@Override
	public boolean isSwingingHand() {
		return this.swinging;
	}

	@Inject(at = @At("HEAD"), method = "attack", cancellable = true)
	private void onPlayerAttack(final Entity target, CallbackInfo ci) {
		ActionResult result = PlayerAttackCallback.EVENT.invoker().attackEntity((PlayerEntity)(Object)this, target);
		if (result == ActionResult.FAIL) {
			ci.cancel();
		}
		if (ModConfig.rapidAttackDamageFalloff <= 0) return;

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastAttackTime < ModConfig.rapidAttackThreshold) {
			rapidAttackCount = Math.min(rapidAttackCount + 1, ModConfig.maxRapidAttackCount);
		} else {
			rapidAttackCount = Math.max(0, rapidAttackCount - 1);
		}
		lastAttackTime = currentTime;
	}

	@Inject(at = @At("TAIL"), method = "applyDamage", cancellable = true)
	private void onEntityHurt(DamageSource source, float amount, CallbackInfo ci) {
		ActionResult result = EntityHurtCallback.EVENT.invoker().hurtEntity((PlayerEntity) (Object) this, source, amount);
		if (result == ActionResult.FAIL) {
			ci.cancel();
		}
	}

	@ModifyVariable(
			method = "attack",
			at = @At(value = "STORE", ordinal = 0),
			ordinal = 0
	)
	private float modifyAttackDamage(float f) {
		if (ModConfig.rapidAttackDamageFalloff <= 0) return f;

		PlayerEntity player = (PlayerEntity)(Object)this;
		ItemStack mainHandItem = player.getStackInHand(Hand.MAIN_HAND);

		if (mainHandItem.isEmpty()) {
			float damageMultiplier = 1.0f - (ModConfig.rapidAttackDamageFalloff * rapidAttackCount);
			return f * Math.max(ModConfig.minRapidAttackDamage, damageMultiplier);
		}
		return f;
	}


	@Inject(at = @At("HEAD"), method = "tick")
	private void onTick(CallbackInfo ci) {
		if (System.currentTimeMillis() - lastAttackTime > ModConfig.rapidAttackResetTime) {
			rapidAttackCount = 0;
		}
	}
}