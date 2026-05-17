# NX Client

A Fabric utility mod for Minecraft 1.21.4 — built by **Novatex**.

> The name *NX* comes from the first and last letters of **N**ovate**x**.

![NX Client](src/main/resources/assets/nxclient/icon.png)

## Features

### Combat
| Module | Description |
|---|---|
| **KillAura** | Hits all nearby living entities at once. In AnarchyBypass mode: single target, with rotation, slower attack rate |
| **AntiKnockback** | Cancels server-sent knockback velocity packets |
| **Criticals** | Every melee hit becomes a critical via `MinecraftClient.doAttack` HEAD packet injection |

### Movement
| Module | Description |
|---|---|
| **Fly** | Free survival flight |
| **BotFly** | Ride a boat and fly with it. Jump = up, release = slow descent |
| **Speed** | Sprint-like ground speed (no momentum drift) |
| **NoFall** | Sends `OnGroundOnly(true)` packets every tick while falling — server keeps resetting fall distance |
| **Sprint** | Auto-sprint when moving forward |
| **Step** | Step up full blocks without jumping |
| **Spider** | Climb walls by pressing into them |

### Player
| Module | Description |
|---|---|
| **AutoEat** | Switches to food, holds use key, restores slot when full |
| **NoHunger** | Keeps food and saturation at max (client-side) |
| **AutoTotem** | Keeps a Totem of Undying in your offhand |
| **AutoArmor** | Auto-equips the best armor piece for each slot |
| **Freecam** | Detached camera with NoClip — pass through anything; returns to start on disable |

### Render
| Module | Description |
|---|---|
| **ESP** | Highlights all living entities through walls |
| **FullBright** | Bypasses gamma limit — full visibility everywhere |
| **XRay** | Hides non-ore blocks, makes ore faces always visible |
| **HUD** | Shows active modules on screen, color-coded by category |

### Misc
| Module | Description |
|---|---|
| **AnarchyBypass** | Master toggle. When ON, combat/movement modules become slower and more anticheat-friendly |
| **AutoFish** | Casts and recasts your fishing rod automatically |
| **Reach** | 2-packet teleport attack for extended reach |
| **FastPlace** | Places items with no cooldown between clicks |
| **FastBreak** | Removes the between-block break cooldown |
| **Scaffold** | Auto-places blocks under your feet while walking |
| **ChestStealer** | Auto-pulls chest items, with ignore list (default: apple/elma) |

## Controls
- **Right Shift** — Open the ClickGUI in-game
- **ESC → NX Client** — Open from the pause menu
- **Drag panel headers** — Move panels around
- **Left-click a module** — Toggle on/off
- **Right-click a module** — Expand its settings (slider via left half = decrease, right half = increase)

## Build
```bash
./gradlew build
```
Output: `build/libs/nxclient-1.0.0.jar`

## Requirements
- Minecraft 1.21.4
- Fabric Loader ≥ 0.16.10
- Fabric API

---

## NX Bots — multi-bot framework *(separate sub-project, foundation laid)*

`bots/` contains a standalone Java app using **MCProtocolLib**. It can already:
- Connect N bots to a server
- Auto `/register <pass> <pass>` + `/login <pass>`
- Use custom or random names

See `bots/README.md` for build and run instructions. Pathfinding, command parsing, and Ollama AI chat replies are the next phases.

---

## Anarchy Bypass

**AnarchyBypass** is a master toggle in the Misc category. When on, modules adjust to be less obviously detectable on anticheats like NCP, Vulcan, Grim, Spartan, etc.:

- **KillAura**: switches from multi-target burst to single nearest target, with proper rotation packets and a slower (≥12 tick) cooldown — closer to vanilla click rate
- More modules will adapt to bypass mode in future updates

Tuning per server/anticheat is recommended — `AnarchyBypass` is a sane default, not a magic bullet.

---

## Credits & References

NX Client is open source and respects the projects we learned from. Specific techniques and patterns were studied from the following open-source clients — **none of the code below is copy-pasted; it has been re-implemented**, but credit where it's due:

| Reference | License | What we learned from it |
|---|---|---|
| [**Minecraft-Reverse-Engineering**](https://github.com/miracmirza/Minecraft-Reverse-Engineering) by **miracmirza** | No license (used with attribution) | The original spark: Fly velocity control, ESP via `Entity.isGlowing()` mixin, packet-teleport Reach pattern. The first project we studied. |
| [**Meteor Client**](https://github.com/MeteorDevelopment/meteor-client) | GPL-3.0 | Module/Category architecture, ClickGUI panel concept, the canonical NoFall pattern (send `PlayerMoveC2SPacket.OnGroundOnly(true, false)` while falling), FullBright through `SimpleOption` accessor, XRay `shouldDrawSide` mixin idea. |
| [**Wurst Client**](https://github.com/Wurst-Imperium/Wurst7) | GPL-3.0 | KillAura multi-target attack loop, AutoEat hold-use-key pattern, Criticals via `doAttack` HEAD mixin, AutoTotem/AutoArmor slot-swap pattern, ChestStealer QUICK_MOVE pattern. |
| [**Aoba Client**](https://github.com/coltonk9043/Aoba-Client) | GPL-3.0 | Movable/pinnable ClickGUI windows — our draggable category panel layout is inspired by Aoba. RShift keybind convention, right-click-to-expand-settings pattern. |
| [**MasterMind-Fabric**](https://github.com/Snowiiii/MasterMind-Fabric) | — | KillAura anti-cheat bypass techniques (rotation packets, controlled cooldown timing). Inspired the AnarchyBypass mode. |
| [**Lambda Client**](https://github.com/lambda-client/lambda) | GPL-3.0 | Blink module pattern — intercepting outgoing packets at the `ClientConnection.send()` level (used in Freecam's packet-block approach). |
| [**3arthh4ck Fabric**](https://github.com/3arthh4ckDevelopment/3arthh4ck-Fabric) | MIT | Packet manipulation patterns and overall Fabric utility-mod structure. |

For the bot project:

| Reference | License | Used for |
|---|---|---|
| [**MCProtocolLib**](https://github.com/GeyserMC/MCProtocolLib) | MIT | The protocol layer for `bots/` — speaks Minecraft's wire format without rendering. |
| [**Mineflayer**](https://github.com/PrismarineJS/mineflayer) | MIT | (Reference only) Conceptual design for high-level bot APIs — pathfinding, inventory, chat. |

If a code path closely follows a specific upstream implementation, the matching project is mentioned in the source file's comment. Anything I missed crediting is unintentional — please open an issue.

## License
[GNU Affero General Public License v3.0](LICENSE)
