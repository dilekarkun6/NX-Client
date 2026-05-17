# NX Bots

> Multi-bot framework for NX Client — standalone Java app that connects N bots to your Minecraft server.

## What this is

A separate Java project (not part of the NX Client Fabric mod). Each bot is its own protocol connection via **MCProtocolLib** — they don't need the game client to run.

## Status

| Feature | State |
|---|---|
| Connect to server, join world | ✅ Skeleton ready (Bot.java) |
| Auto `/register pass pass` + `/login pass` | ✅ Implemented |
| Configurable / random names | ✅ Implemented |
| Multiple bots in parallel | ✅ Implemented (thread pool) |
| `follow <player>` command from owner | ⏳ TODO |
| `chop <count>` wood-cutting | ⏳ TODO |
| Inventory check + auto-deliver | ⏳ TODO |
| Ollama AI chat replies | ⏳ TODO |

## Build

```bash
cd bots
gradle shadowJar
# Output: build/libs/nxbots-all.jar
```

## Run

```bash
java -jar build/libs/nxbots-all.jar <host> <port> <count> [options]
```

### Examples

Connect 3 bots to localhost, no auth:
```bash
java -jar nxbots-all.jar 127.0.0.1 25565 3
```

Connect 5 bots with auth and custom names:
```bash
java -jar nxbots-all.jar mc.example.com 25565 5 \
  --names "Helper,Worker,Scout,Builder,Friend" \
  --auth-pass "yourpassword"
```

The bots will:
1. Connect to the server in offline mode (so the server must allow `online-mode=false` in `server.properties`)
2. Once joined, send `/register <pass> <pass>` then `/login <pass>` if `--auth-pass` is given
3. Stay idle until commands are added

## What's next

The skeleton works — bots will join your server. Coming next:

- **Tick loop & physics** — pathfinding requires reading world state and sending move packets
- **Owner command parsing** — bot listens for `[Owner] /msg <botname> follow` etc.
- **Tree-finding & wood-chopping**
- **Ollama HTTP client** for chat — points at `http://localhost:11434/api/generate`

## Why not a Fabric mod

A Fabric mod runs inside one Minecraft client. To have 5 player entities on the server, you need 5 client connections — each is a separate `MinecraftProtocol` session. The game client only manages one. MCProtocolLib speaks the wire protocol without rendering anything, which is what we want.

## License

[AGPL-3.0](../LICENSE) — same as NX Client.
