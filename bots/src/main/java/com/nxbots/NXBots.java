package com.nxbots;

public class NXBots {

    public static void main(String[] args) {
        System.out.println("NX Bots — skeleton.");
        System.out.println("This module is not implemented yet. See README.md in this folder.");
        System.out.println();
        System.out.println("Planned: spawn N MCProtocolLib bots that join a server,");
        System.out.println("auto-register, accept commands, and chat via local Ollama.");
        if (args.length > 0) {
            System.out.println();
            System.out.println("You passed args:");
            for (String a : args) System.out.println("  - " + a);
        }
    }
}
