package spooketti.spookettai;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import spooketti.spookettai.AICommunication.HTTPToGemini;
import spooketti.spookettai.AIControl.InventoryManagerAI;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class SpookettAIClient implements ClientModInitializer {
	HTTPToGemini gemini = new HTTPToGemini();
//    Robot robot;

    @Override
	public void onInitializeClient() {
//        try {
//            robot = new Robot();
//        } catch (AWTException e) {
//            throw new RuntimeException(e);
//        }
        MinecraftClient mc = MinecraftClient.getInstance();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            InventoryManagerAI.
        });

//		GameProfile mcProfile = mc.player.getGameProfile();
		ClientReceiveMessageEvents.CHAT.register((message,signedMessage,profile,params,instant) -> {
			if(profile != null) {
                assert mc.player != null;
                assert signedMessage != null;
                String rawText = signedMessage.getSignedContent();
                System.out.println(rawText);
                if(!gemini.isChatForAI(rawText)) {
                    return;
                }

                CompletableFuture.runAsync(() -> {
                    String geminiResponse = " ";
                    try {
                        geminiResponse = gemini.chatToGemini(message.getString());
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
//                    copyToClipboard("[SpookettiAI] " + geminiResponse);
//                    robot.keyPress(KeyEvent.VK_T);
//                    robot.keyRelease(KeyEvent.VK_T);
//                    robot.keyPress(KeyEvent.VK_CONTROL);
//                    robot.keyPress(KeyEvent.VK_V);
//                    robot.keyRelease(KeyEvent.VK_CONTROL);
//                    robot.keyRelease(KeyEvent.VK_V);
//                    robot.keyPress(KeyEvent.VK_ENTER);
//                    robot.keyRelease(KeyEvent.VK_ENTER);

//                    mc.player.networkHandler.sendChatMessage("[SpookettiAI] " + geminiResponse);
//                    MinecraftClient.getInstance().setScreen();
                    MinecraftClient.getInstance().getNetworkHandler().sendChatMessage("[SpookettiAI] " + geminiResponse);

                });


//                if (!profile.getId().equals(mc.player.getGameProfile().getId())) {
//                    mc.player.networkHandler.sendChatMessage("[SpookettAI] " + message.getString());
//                }
            }
		});

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}

    public static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);  // Create a StringSelection object with the text
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // Get the system clipboard
        clipboard.setContents(stringSelection, null);  // Copy the string to the clipboard
    }


}