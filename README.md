# OoTCraft (Fabric 1.20.1)

Ocarina-of-Time-style gameplay inside Minecraft, plus an SM64 mode.
No Nintendo or Mojang assets are in this project. Everything bundled is original.

## What is in v0.1
- Z-Target (hold Z): the camera locks onto the nearest visible hostile mob.
- Ocarina (right-click): plays flute notes. Sneak + right-click plays Sun's Song, which flips day and night.
- Master Sword: strong, fireproof sword. Recipe: 2 netherite ingots over a stick, vertically.
- SM64 mode (press K): swaps your own skin to a Mario-style skin and enables chained triple jumps
  (jump again within about half a second of landing).
- Optional SM64 texture pack built from your own ROM (see tools/).

## Build on Windows
1. Install JDK 17:  winget install EclipseAdoptium.Temurin.17.JDK
2. Install Gradle: winget install Gradle.Gradle   (open a new terminal afterwards)
3. Double-click build.bat (or run: gradle build)
4. Copy build\libs\ootcraft-0.1.0.jar into your Prism instance mods folder
   (Prism: right-click the instance > Folder > .minecraft\mods).
5. The instance needs Fabric Loader for 1.20.1 and Fabric API 0.92.2+1.20.1 in the same mods folder.

## SM64 texture pack (local only)
See the header of tools/make_sm64_pack.py. Decomp builds run under WSL or Linux.
