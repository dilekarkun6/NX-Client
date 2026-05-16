# NX Client

A Fabric utility mod for Minecraft 1.21.4 — built by **Novatex**.

> The name *NX* comes from the first and last letters of **N**ovate**x**.

![NX Client](src/main/resources/assets/nxclient/icon.png)

## Features

### Combat
| Module | Description |
|---|---|
| **KillAura** | Attacks ALL nearby living entities simultaneously |
| **AntiKnockback** | Cancels server-sent knockback velocity packets |
| **Criticals** | Every melee hit becomes a critical via `attackEntity` packet injection |

### Movement
| Module | Description |
|---|---|
| **Fly** | Free survival flight |
| **BoatFly** | Ride a boat and fly with it. Jump = up, release = slow descent |
| **Speed** | Sprint-like ground speed (no momentum drift) |
| **NoFall** | Spoofs `onGround=true` in move packets — no fall damage |
| **Sprint** | Auto-sprint when moving forward |
| **Step** | Step up full blocks without jumping |
| **Spider** | Climb walls by pressing into them |
| **Blink** | Freezes outgoing position packets; releases them on toggle off |

### Player
| Module | Description |
|---|---|
| **AutoEat** | Switches to food, holds use key, restores slot when full |
| **NoHunger** | Keeps food and saturation at max (client-side) |
| **AutoTotem** | Keeps a Totem of Undying in your offhand automatically |
| **AutoArmor** | Auto-equips the best armor piece for each slot |
| **InvWalk** | Walk and look around while inventory is open |
| **Freecam** | Detaches camera — server thinks you're standing still |

### Render
| Module | Description |
|---|---|
| **ESP** | Highlights all living entities through walls |
| **FullBright** | Bypasses gamma limit — full visibility everywhere |
| **XRay** | Hides non-ore blocks so ores show through walls |
| **HUD** | Shows active modules on screen, color-coded by category |

### Misc
| Module | Description |
|---|---|
| **AutoFish** | Casts and recasts your fishing rod automatically |
| **Reach** | 2-packet teleport attack for extended reach |
| **FastPlace** | Places items with no cooldown between clicks |
| **FastBreak** | Removes the between-block break cooldown |
| **Scaffold** | Auto-places blocks under your feet while walking |
| **ChestStealer** | Auto-pulls chest items, with ignore list (default: apple/elma) |

## Controls
- **Right Shift** — Open the ClickGUI in-game
- **ESC → NX Client** — Open from the pause menu
- **Drag panel headers** in the GUI to move them around
- **Click a module** to toggle it

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

## Roadmap: NX Bot Network *(planned next phase)*

A separate sub-project — a Java bot framework using **MCProtocolLib** (or Mineflayer if we go Node) that lets you spawn N bots into your own server. Each bot will:

- Auto-register/login via chat (e.g. `/register password password`) if the server runs an Auth plugin
- Accept commands from the player ("chop 10 logs", "follow me", "stop")
- Inventory check + auto-deliver items to the owner
- Chat replies powered by a locally-hosted open-source LLM (e.g. **Ollama** or **HuggingFace Inference**) — no paid API keys
- Configurable names (random or chosen)

This is **not part of NX Client itself** — it will live in a separate repo (`NX-Bots`) and connect to any vanilla/Spigot/Paper server. The Fabric client mod will stay focused on in-game utilities.

---

## Credits & References

NX Client is open source and respects the projects we learned from. Specific techniques and patterns were studied from the following open-source clients — **none of the code below is copy-pasted; it has been re-implemented**, but credit where it's due:

| Reference | License | What we learned from it |
|---|---|---|
| [**Minecraft-Reverse-Engineering**](https://github.com/miracmirza/Minecraft-Reverse-Engineering) by **miracmirza** | No license (used with attribution) | The original spark: Fly velocity control, ESP via `Entity.isGlowing()` mixin, packet-teleport Reach pattern. This was the first project we studied. |
| [**Meteor Client**](https://github.com/MeteorDevelopment/meteor-client) | GPL-3.0 | Module/Category architecture, ClickGUI panel concept, packet-based NoFall via `PlayerMoveC2SPacket` `onGround` spoofing, FullBright through `SimpleOption` accessor, XRay `shouldDrawSide` mixin idea. |
| [**Wurst Client**](https://github.com/Wurst-Imperium/Wurst7) | GPL-3.0 | KillAura multi-target attack loop, AutoEat hold-use-key pattern, Criticals via `ClientPlayerInteractionManager.attackEntity` mixin, AutoTotem/AutoArmor slot-swap pattern, ChestStealer QUICK_MOVE pattern. |
| [**Aoba Client**](https://github.com/coltonk9043/Aoba-Client) | GPL-3.0 | Movable/pinnable ClickGUI windows — our draggable category panel layout is inspired by Aoba's approach. RShift keybind convention. |
| [**Lambda Client**](https://github.com/lambda-client/lambda) | GPL-3.0 | Blink module pattern — intercepting outgoing packets at the `ClientConnection.send()` level and queueing them. Freecam packet-block approach. |
| [**3arthh4ck Fabric**](https://github.com/3arthh4ckDevelopment/3arthh4ck-Fabric) | MIT | Packet manipulation patterns and overall Fabric utility-mod structure. |

If a code path closely follows a specific upstream implementation, the matching project is mentioned in the source file's comment. Anything I missed crediting is unintentional — please open an issue.

## License
[GNU Affero General Public License v3.0](LICENSE)
