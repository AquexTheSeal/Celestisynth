# Celestisynth Changelog

## Release v1.20.1-1.3.2 [Final 1.x Release] | [Forge]

### Important

- This release is a final release for 1.x. We will be focusing on the 2.x rewrite going forward. The only exception to this is if a bug renders the mod completely unplayable or something of a similar scale happens.
- Weapon damage configs were scrapped due to time constraints, since you can just use KubeJS (or other scripting APIs/KJS addons) to easily change the damage values.

### Additions/Changes

- Updated to the latest version of Forge (47.4.5)
- Updated weapon chants.
- Updated Keres overlay.
- Added config option to disable Keres griefing. Made Keres respect mob griefing gamerule.
- Slightly buffed Rainfall Serenity's base damage.
- Renamed Keres advancement.
- Made Star Monoliths *slightly* more common in Nether Fortresses.

### Bug Fixes

- Fixed mixinextras being a missing hard-dependency (it is now correctly Jar-in-Jar'd).
- Fixed Starlit Factory being unminable by default (added to #NEEDS_DIAMOND_PICKAXE and #MINEABLE_WITH_PICKAXE tags)
- Fixed Keres overlay breaking when scaling to window size on occasion.
- Monoliths no longer spawn in the Overworld.
- Monoliths are no longer persistent (i.e. they can naturally despawn). They now also correctly despawn when the difficulty is set to peaceful.
- Monoliths can now spawn in the fortresses added by Yung's Better Fortresses. Their spawning is determined by the "celestisynth:nether_monolith_spawn" tag.
- Removed prototype weapon skin code entirely (broke stuff, wasn't actually used in prod).
- Fixed log spam due to Celestisynth abilities being used (removed stray logger calls).
- Overlay issues with certain mods that affect hud rendering like Malum *should* be fixed.
- General backend refactor.

### Known Issues (skipped due to time constraints)

- Data API causes weapons to be unusable.
- Stellarity causes the Keres to be unable to break blocks with its Carnage Incarnate skill regardless of the newly-set config option.
- Rainfall Serenity's damage becomes broken (OP) when Projectile Damage Attribute is installed.
- Legendary Tooltips causes weapon tooltips to be formatted incorrectly (centered). For now, just slap these values into your LT config:
```cfg
name_separator = false
compact_tooltips = false
render_item_model = "NONE"
```

### Delegated to 2.x due to rework + time constraints:

- Fix for starlit Factory shaking all UI elements (primarily REI/JEI).
- **Base** weapon damage configs.