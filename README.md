# NX Client

A powerful Fabric utility mod for Minecraft 1.21.4.

![NX Client](src/main/resources/assets/nxclient/icon.png)

## Features

### Combat
| Module | Description |
|---|---|
| KillAura | Auto attacks the nearest living entity in range |
| AntiKnockback | Reduces knockback received from hits |

### Movement
| Module | Description |
|---|---|
| Fly | Lets you fly freely in survival mode |
| Speed | Increases horizontal movement speed |
| NoFall | Prevents all fall damage |
| Sprint | Always sprints when moving forward |
| Step | Step up full blocks without jumping |
| Blink | Temporarily freeze and re-sync position |

### Render
| Module | Description |
|---|---|
| ESP | Highlights all living entities through walls |
| FullBright | Removes darkness — full brightness everywhere |
| HUD | Displays active modules on screen |

### Player
| Module | Description |
|---|---|
| AutoEat | Automatically eats food when hungry |
| NoHunger | Keeps food level full (client-side) |

### Misc
| Module | Description |
|---|---|
| AutoFish | Automatically casts and recasts your fishing rod |
| Reach | Attacks entities from extreme distances via packet |

## Controls
- **Right Shift** — Open NX Client GUI in-game
- **ESC** (in-game pause menu) — Click "NX Client" button

## Build

```bash
./gradlew build
```

Output: `build/libs/nxclient-1.0.0.jar`

## Requirements
- Minecraft 1.21.4
- Fabric Loader ≥ 0.16.10
- Fabric API

## Credits & References
Some techniques (Fly velocity control, ESP mixin, Reach packet teleport) were studied and adapted from:  
[Minecraft-Reverse-Engineering](https://github.com/miracmirza/Minecraft-Reverse-Engineering) by **miracmirza** — used as a reference/learning resource.

## License
[GNU Affero General Public License v3.0](LICENSE)
