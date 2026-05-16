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
| **Criticals** | Every melee hit becomes a critical via mini-jump packet |

### Movement
| Module | Description |
|---|---|
| **Fly** | Free survival flight |
| **BotFly** | Slow, oscillating flight — harder for anticheats to flag |
| **Speed** | Sprint-like ground speed boost (no momentum drift) |
| **NoFall** | Spoofs `onGround=true` in move packets — no fall damage |
| **Sprint** | Auto-sprint when moving forward |
| **Step** | Step up full blocks without jumping |
| **Blink** | Freezes outgoing position packets; releases them on toggle off |

### Render
| Module | Description |
|---|---|
| **ESP** | Highlights all living entities through walls |
| **FullBright** | Bypasses gamma limit — full visibility everywhere |
| **HUD** | Shows active modules on screen, color-coded by category |

### Player
| Module | Description |
|---|---|
| **AutoEat** | Switches to food, holds use key, restores slot when full |
| **NoHunger** | Keeps food and saturation at max (client-side) |

### Misc
| Module | Description |
|---|---|
| **AutoFish** | Casts and recasts your fishing rod automatically |
| **Reach** | 2-packet teleport attack for extended reach |

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

## Credits & References

NX Client is open source and respects the projects we learned from. Specific techniques and patterns were studied from the following open-source clients — **none of the code below is copy-pasted; it has been re-implemented**, but credit where it's due:

| Reference | License | What we learned from it |
|---|---|---|
| [**Minecraft-Reverse-Engineering**](https://github.com/miracmirza/Minecraft-Reverse-Engineering) by **miracmirza** | — | The original spark: Fly velocity control, ESP via `Entity.isGlowing()` mixin, packet-teleport Reach pattern. This was the first project we studied. |
| [**Meteor Client**](https://github.com/MeteorDevelopment/meteor-client) | GPL-3.0 | Module/Category architecture, ClickGUI panel concept, packet-based NoFall via `PlayerMoveC2SPacket` `onGround` spoofing, FullBright through `SimpleOption` accessor. |
| [**Wurst Client**](https://github.com/Wurst-Imperium/Wurst7) | GPL-3.0 | KillAura multi-target attack loop, AutoEat hold-use-key pattern, Criticals via mini-jump packet sequence. |
| [**Aoba Client**](https://github.com/coltonk9043/Aoba-Client) | GPL-3.0 | Movable/pinnable ClickGUI windows — our draggable category panel layout is inspired by Aoba's approach. RShift keybind convention. |
| [**Lambda Client**](https://github.com/lambda-client/lambda) | LGPL-3.0 | Blink module pattern — intercepting outgoing packets at the `ClientConnection.send()` level and queueing them. |
| [**3arthh4ck Fabric**](https://github.com/3arthh4ckDevelopment/3arthh4ck-Fabric) | — | Packet manipulation patterns and overall Fabric utility-mod structure. |

If a code path closely follows a specific upstream implementation, the matching project is mentioned in the source file's comment. Anything I missed crediting is unintentional — please open an issue.

## License
[GNU Affero General Public License v3.0](LICENSE)
