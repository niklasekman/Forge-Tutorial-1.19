package com.niklasekman.tutorialmod.client;

public class ClientThirstData {
    private static int playerThirst;

    public static int getPlayerThirst() {
        return playerThirst;
    }

    public static void setPlayerThirst(int playerThirst) {
        ClientThirstData.playerThirst = playerThirst;
    }
}
