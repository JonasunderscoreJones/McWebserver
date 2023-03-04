package me.jonasjones.mcwebserver.mixin;

import me.jonasjones.mcwebserver.McWebserver;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class WebserverStopMixin {
	@Inject(at = @At("HEAD"), method = "shutdown")
	private void init(CallbackInfo info) {
		McWebserver.LOGGER.info("Stopping Webserver...");
		McWebserver.mcserveractive = false;
	}
}
