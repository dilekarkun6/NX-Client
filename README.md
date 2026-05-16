# NX Client

A Fabric utility mod for Minecraft 1.21.4 — built by **Novatex**.

> The name *NX* comes from the first and last letters of **N**ovate**x**.

![NX Client](src/main/resources/assets/nxclient/icon.png)

## Features

### Combat
| Module | Description | Settings |
|---|---|---|
| **KillAura** | Attacks ALL nearby living entities simultaneously | Range, Cooldown |
| **AntiKnockback** | Cancels server-sent knockback velocity packets | — |
| **Criticals** | Every melee hit becomes a critical via `attackEntity` mixin | — |

### Movement
| Module | Description | Settings |
|---|---|---|
| **Fly** | Free survival flight | — |
| **BotFly** | Ride a boat and fly with it. Jump = up, release = slow descent | UpSpeed, HSpeed, FallSpeed |
| **Speed** | Sprint-like ground speed (no momentum drift) | Speed |
| **NoFall** | Spoofs `onGround=true` in move packets — no fall damage | — |
| **NoClip** | Walk through blocks. Returns you to start position on disable | — |
| **Sprint** | Auto-sprint when moving forward | — |
| **Step** | Step up full blocks without jumping | — |
| **Spider** | Climb walls by pressing into them | — |

### Player
| Module | Description |
|---|---|
| **AutoEat** | Switches to food, holds use key, restores slot when full |
| **NoHunger** | Keeps food and saturation at max (client-side) |
| **AutoTotem** | Keeps a Totem of Undying in your offhand automatically |
| **AutoArmor** | Auto-equips the best armor piece for each slot |
| **Freecam** | Detaches camera — server thinks you're standing still |

### Render
| Module | Description |
|---|---|
| **ESP** | Highlights all living entities through walls |
| **FullBright** | Bypasses gamma limit — full visibility everywhere |
| **XRay** | Hides non-ore blocks so ores show through walls |
| **HUD** | Shows active modules on screen, color-coded by category |

### Misc
| Module | Description | Settings |
|---|---|---|
| **AutoFish** | Casts and recasts your fishing rod automatically | — |
| **Reach** | 2-packet teleport attack for extended reach | MaxReach |
| **FastPlace** | Places items with no cooldown between clicks | — |
| **FastBreak** | Removes the between-block break cooldown | — |
| **Scaffold** | Auto-places blocks under your feet while walking | — |
| **ChestStealer** | Auto-pulls chest items, with ignore list (default: apple/elma) | — |

## Controls
- **Right Shift** — Open the ClickGUI in-game
- **ESC → NX Client** — Open from the pause menu
- **Drag panel header** — Move panels around the screen
- **Left-click module** — Toggle on/off
- **Right-click module** — Expand to show settings (if any)
- **Left-click setting** — Left half decrements, right half increments
- **Right-click setting** — Decrement

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

## Roadmap: NX Bot Network *(planned next phase — separate project)*

A separate sub-project using **MCProtocolLib** that spawns N bots into your own server. Each bot will:

- Auto-register/login via chat (e.g. `/register password password`)
- Accept commands ("chop 10 logs", "follow me", "stop")
- Auto-deliver gathered items
- Chat replies powered by locally-hosted open-source LLM (Ollama or HuggingFace Inference)
- Configurable names (random or chosen)

This is **not part of NX Client itself** — it will live in a separate repo (`NX-Bots`) and connect to any vanilla/Spigot/Paper server, including your own port-forwarded server. The Fabric client mod stays focused on in-game utilities.

---

## Credits & References

NX Client is open source and respects the projects we learned from. Specific techniques and patterns were studied from the following open-source clients — **none of the code below is copy-pasted; it has been re-implemented**, but credit where it's due:

| Reference | License | What we learned from it |
|---|---|---|
| [**Minecraft-Reverse-Engineering**](https://github.com/miracmirza/Minecraft-Reverse-Engineering) by **miracmirza** | No license (used with attribution) | The original spark: Fly velocity control, ESP via `Entity.isGlowing()` mixin, packet-teleport Reach pattern. This was the first project we studied. |
| [**Meteor Client**](https://github.com/MeteorDevelopment/meteor-client) | GPL-3.0 | Module/Category architecture, settings system, NoFall via `PlayerMoveC2SPacket` `onGround` spoofing, FullBright via `SimpleOption` accessor, XRay `shouldDrawSide` mixin, Criticals 2-packet sequence. |
| [**Wurst Client**](https://github.com/Wurst-Imperium/Wurst7) | GPL-3.0 | KillAura multi-target loop, AutoEat hold-use-key pattern, AutoTotem/AutoArmor slot-swap, ChestStealer QUICK_MOVE pattern. |
| [**Aoba Client**](https://github.com/coltonk9043/Aoba-Client) | GPL-3.0 | Movable/pinnable ClickGUI windows — draggable panel layout and right-click expand-for-settings is inspired by Aoba. |
| [**Lambda Client**](https://github.com/lambda-client/lambda) | GPL-3.0 | Freecam packet-block approach via `ClientConnection.send()`. |
| [**3arthh4ck Fabric**](https://github.com/3arthh4ckDevelopment/3arthh4ck-Fabric) | MIT | Packet manipulation patterns and Fabric utility-mod structure. NoClip via `Entity.adjustMovementForCollisions` mixin. |

## License
[GNU Affero General Public License v3.0](LICENSE)
