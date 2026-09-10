package com.example.autohub;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import java.util.List;

public class AutoHubMod implements ClientModInitializer {

    private static final double RANGE = 35.0;
    private static final List<String> WHITELIST = List.of("zontiiikkkk");
    private boolean triggered = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) {
                triggered = false;
                return;
            }

            if (triggered) return;

            for (AbstractClientPlayerEntity otherPlayer : client.world.getPlayers()) {
                if (otherPlayer == client.player) continue;

                String name = otherPlayer.getName().getString().toLowerCase();
                if (WHITELIST.contains(name)) continue;

                double distance = client.player.distanceTo(otherPlayer);
                if (distance <= RANGE) {
                    client.player.networkHandler.sendCommand("hub");
                    triggered = true;
                    break;
                }
            }
        });
    }
}
