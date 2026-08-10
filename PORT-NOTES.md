# Celestisynth — Portage NeoForge 1.21.1

Portage du mod **Celestisynth 1.3.4** (Forge 1.20.1, par AquTheSeal, Meme Man,
UnanimousVoid — The Celestial Workshop, licence MIT) vers **NeoForge 1.21.1**.

## Environnement

- NeoForge **21.1.244**, ModDevGradle 2.0.91, Gradle 8.14.2, Java 21, mappings Parchment 2024.11.17
- GeckoLib **4.9.2** (`geckolib-neoforge-1.21.1`)
- playerAnimator **2.0.4+1.21.1** (artifact `player-animation-lib-forge`, qui est bien le build NeoForge)

## Changements structurels majeurs

| 1.20.1 Forge | 1.21.1 NeoForge |
|---|---|
| CapabilitySyncer (lib jarJar) + capability d'entité | `AttachmentType` NeoForge sérialisé + packet de sync custom (`EntityCapabilitySyncPacket`) déclenché sur StartTracking/login/respawn/changement de dimension, `copyOnDeath()` |
| NBT d'ItemStack `csController`/`csExtras` (tags mutables) | 2 DataComponents `CustomData` (`cs_controller`, `cs_extras`) ; les accesseurs renvoient le tag vivant via `getUnsafe()` pour préserver le contrat mutable d'origine (la logique tourne des deux côtés comme en 1.20.1) |
| SimpleChannel + 8 packets | `PayloadRegistrar` + records `CustomPacketPayload`/`StreamCodec` (9 packets avec la sync d'attachment) |
| Enchantement `PulsationEnchantment` (classe) | Data-driven : JSON généré (`data/celestisynth/enchantment/pulsation.json`) + effet déclenché depuis `LivingDamageEvent.Post` |
| `Rarity.create()` Forge + RarityMixin | Extensions d'enum NeoForge (`META-INF/enumextensions.json` + `EnumProxy`) ; le style animé CELESTIAL passe par l'`UnaryOperator<Style>` du constructeur patché — accès **paresseux** obligatoire (`CSRarityTypes.celestial()`) |
| `TierSortingRegistry` + ForgeTier | `SimpleTier` + tag `incorrect_for_celestial_tool` |
| ArmorMaterial enum | Registre `ArmorMaterial` (durabilité déplacée sur `Item.Properties.durability`) ; bonus d'attributs via `getDefaultAttributeModifiers()` |
| Recette Starlit Factory (fromJson/fromNetwork) | `MapCodec` + `StreamCodec` + `RecipeInput` dédié |
| GLM/structure modifiers `Codec` | `MapCodec` + clés `NeoForgeRegistries` |
| Overlays `IGuiOverlay` | `LayeredDraw.Layer` + `RegisterGuiLayersEvent` |
| `MenuScreens.register` dans FMLClientSetup | `RegisterMenuScreensEvent` |
| Datagen 1.20.1 (FinishedRecipe, LootDataId, FrameType…) | API 1.21 (RecipeOutput, ResourceKey<LootTable>, AdvancementHolder, BootstrapContext) ; JSON régénérés au format 1.21 (`recipe/`, `loot_table/`, `advancement/`, `data/neoforge/`) |

## Mixins

- Supprimés : `RarityMixin` (remplacé par l'extension d'enum), `BowItemMixin`
  (vanilla 1.21 gère nativement le multishot des arcs via `draw()`).
- Adaptés aux nouvelles signatures : `EntityRenderDispatcherMixin` (renderHitbox
  +3 floats), `RenderLayerMixin` (couleur ARGB int), `CreativeModeInventoryScreenMixin`
  (mouseScrolled 4 doubles), `LivingEntityRendererMixin` (descripteur renderToBuffer III),
  `EntityMixin` (plus de CapabilityProvider), `RecipeManagerAccessor` (RecipeHolder),
  `InventoryScreenMixin`, `MinecraftMixin` (composants au lieu du NBT).

## Intégrations désactivées

Better Combat, JEI (catégorie Starlit Factory), Iron's Spellbooks (items +
attributs sorts), Apotheosis. Les points d'appel ont été stubbés et les
classes de compat retirées de l'arbre source (elles restent dans l'historique
git de `forge-1.20.1`) ; à réintroduire quand ces mods/API seront disponibles
et stabilisés en 1.21.1. `SwingParticleContainer` a été conservé (déplacé dans
`api.item`) mais les particules de swing Better Combat sont inertes sans
l'intégration.

## Notes de comportement

- Le bonus de dégâts générique `EnchantmentHelper.getDamageBonus(…, MobType)` a
  disparu en 1.21 ; reproduit via la formule Sharpness 1.20.1 (0.5×niv + 0.5).
- `canBreatheUnderwater()` est devenu final : remplacé par
  `canDrownInFluidType(...) -> false` (StarMonolith, RainfallTurret).
- Les feux d'artifice d'Aquaflora utilisent le composant `FireworkExplosion`.
- Le brassage (scraps lunaires) passe par `RegisterBrewingRecipesEvent`.
