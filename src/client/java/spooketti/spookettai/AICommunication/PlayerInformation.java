package spooketti.spookettai.AICommunication;

import net.minecraft.client.MinecraftClient;

public class PlayerInformation {
    MinecraftClient client = MinecraftClient.getInstance();
    public String getPlayerCoord()
    {
        if(client.player != null)
        {
            return Math.round(client.player.getX()) + ", " + Math.round(client.player.getY()) + ", " + Math.round(client.player.getZ());
        }
        return " ";
    }
}
