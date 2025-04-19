package spooketti.spookettai;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import spooketti.spookettai.AICommunication.HTTPToGemini;

import java.io.IOException;

public class SpookettAIClient implements ClientModInitializer {
	HTTPToGemini gemini = new HTTPToGemini();
	@Override
	public void onInitializeClient() {
		MinecraftClient mc = MinecraftClient.getInstance();
//		GameProfile mcProfile = mc.player.getGameProfile();
		ClientReceiveMessageEvents.CHAT.register((message,signedMessage,profile,params,instant) -> {
			if(profile != null) {
                assert mc.player != null;
                try {
                    gemini.postToGemini(message.getString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!profile.getId().equals(mc.player.getGameProfile().getId())) {
                    mc.player.networkHandler.sendChatMessage("[SpookettAI] " + message.getString());
                }
            }
//			MinecraftClient.getInstance().player.networkHandler.sendChatMessage("[SpookettAI] " + message.getString());
		});
//		ClientChatReceivedEvents.CHAT.register((message, signedMessage, sender) -> {
//			String content = message.getString();
//			System.out.println("[Chat Message] " + content);
//
//			// Do something with the message, like triggering an action
//			if (content.contains("hello")) {
//				System.out.println("Someone said hello!");
//			}
//		});


		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}