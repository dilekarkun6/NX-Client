package com.nxbots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NXBots {

    private static final String[] NAME_POOL = {
            "Helper", "Worker", "Scout", "Builder", "Friend",
            "Miner", "Logger", "Farmer", "Guard", "Runner",
            "Pixel", "Pebble", "Star", "Echo", "Nova"
    };

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: nxbots <host> <port> <count> [--names a,b,c] [--auth-pass password]");
            System.out.println("Example: nxbots 127.0.0.1 25565 5 --auth-pass mypassword");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        int count = Integer.parseInt(args[2]);

        List<String> customNames = new ArrayList<>();
        String authPass = null;

        for (int i = 3; i < args.length - 1; i++) {
            if (args[i].equals("--names")) {
                for (String name : args[i + 1].split(",")) {
                    customNames.add(name.trim());
                }
            } else if (args[i].equals("--auth-pass")) {
                authPass = args[i + 1];
            }
        }

        Random rng = new Random();
        ExecutorService pool = Executors.newFixedThreadPool(count);
        List<Bot> bots = new ArrayList<>();

        System.out.println("[NXBots] Connecting " + count + " bots to " + host + ":" + port);

        for (int i = 0; i < count; i++) {
            String name;
            if (i < customNames.size()) {
                name = customNames.get(i);
            } else {
                name = NAME_POOL[rng.nextInt(NAME_POOL.length)] + rng.nextInt(1000);
            }
            Bot bot = new Bot(name, host, port, authPass);
            bots.add(bot);
            pool.submit(bot::connect);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[NXBots] Shutting down all bots...");
            for (Bot bot : bots) bot.disconnect();
            pool.shutdownNow();
        }));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
