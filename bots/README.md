# NX Bots — Multi-bot framework for NX Client

> **Status: Skeleton.** This is the entry point for spawning bot clients on your own server. It is intentionally a separate Java project (not part of the NX Client Fabric mod) because bots cannot run inside a single Minecraft instance — they each need their own protocol connection.

## What this will be

A standalone Java app that spawns N bots and joins them to a server (your own Spigot/Paper/Vanilla server you've port-forwarded).

Each bot will support:
- **Auto-register / login**: send `/register password password` on first join, `/login password` thereafter
- **Following**: `follow <player>` — pathfind to a player and stay near them
- **Wood-cutting**: `chop <count>` — find trees, chop until reaching `<count>` logs, then return and drop them
- **Inventory check**: `inv` — report what each bot is holding
- **Chat replies**: lightweight LLM-backed responses (no paid API). Default: **Ollama** running locally with a small model like `llama3.2:1b` or `phi3:mini`. Fallback to canned responses if Ollama isn't reachable.
- **Custom names**: random or comma-separated list from CLI

## Why it's separate

A Fabric mod runs *inside* one Minecraft client process. Spawning 5 extra "players" requires 5 extra network connections — that's 5 separate `MinecraftClient` instances, which Fabric can't host. The standard solution is **MCProtocolLib** (or **Mineflayer** for Node.js) which speaks the wire protocol directly without rendering a game.

## Planned stack

- **Java 21** (same as NX Client)
- **MCProtocolLib** (Steveice10) — Minecraft protocol implementation
- **Ollama HTTP client** for AI chat — calls `http://localhost:11434/api/generate` with the chosen model

## How to run *(when implemented)*

```bash
# 1. Have Ollama running locally with a small model:
ollama pull llama3.2:1b
ollama serve

# 2. Build NX Bots
cd bots
./gradlew shadowJar

# 3. Run with: server IP, port, bot count, optional name list
java -jar build/libs/nxbots-all.jar 127.0.0.1 25565 5 \
  --names "Helper,Worker,Scout,Builder,Friend" \
  --auth-pass "your-server-register-password"
```

Then in-game:
```
[You whisper]    /msg Helper follow me
[You whisper]    /msg Worker chop 10
```

## What's here right now

A `README.md` (this file) and a placeholder `NXBots.java`. The actual MCProtocolLib integration, command parser, pathfinder, and Ollama client will be filled in across the next few iterations.

## License

Same as NX Client — [AGPL-3.0](../LICENSE).
