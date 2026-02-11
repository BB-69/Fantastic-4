# INDEX

> This file is a guide made purely for the team members of this project.

- [INDEX](#index)
- [DIRECTORY \& PURPOSE](#directory--purpose)
  - [core/](#core)
  - [util/](#util)
  - [input/](#input)
  - [states/](#states)
  - [nodes/](#nodes)

# DIRECTORY & PURPOSE

This section will only show **purposes, practices, essentials, NOT EVERYTHING IS IN THIS GUIDE**. Some other functionalities will be explained (or self-explanatory) inside the files themselves.

If you feel the need some parts of [this guide](/doc/GUIDE.md) should be changed or improved, please contact me and provide suggestions, or directly make an issue in [issues page.](https://github.com/BB-69/Fantastic-4/issues)

| Symbol | Meaning                              |
| ------ | ------------------------------------ |
| ✅     | Can be altered freely                |
| ⚠️     | Can be altered, only if truly needed |
| ❌     | For core system (Don't touch this)   |
| ♻️     | Reusable components                  |
| 📘     | Blueprint components                 |

> Above symbols can be ignored **if you are a god-level developer and you know what you're doing.**

```
src/
│
│ ( game assets )
├ assets/
│ ├ fonts/
│ ├ sounds/
│ └ textures/
│
│ ( game source )
└ games/
  │
  │ ( game main system )
  ├ core/
  │
  │ ( static utlities )
  ├ util/
  │
  │ ( input handlers )
  ├ input/
  │
  │ ( game states/scenes )
  ├ states/
  │
  │ ( game nodes/components )
  ├ nodes/
  │
  │
  ├ ❌ Main.java       -> ***[ main entry ]***
  ├ ❌ GameCanvas.java -> handle render space
  ├ ⚠️ Game.java       -> init input system + start state
  ├ ❌ GameLoop.java   -> handle game loop + paint loop
  └ ❌ Engine.java     -> state loop entry (component logic)
```

## core/

This directory consists of essentials and the backbone of this game's logic system. Handling most backend of the component logics. Some can be **called globally**, some are **reusables**, some are **component blueprints** that can be derived from and used to **create new game objects and systems**.

```
core/
├ audio/
│ ├ Sound.java
│ └ SoundData.java
│
├ graphics/
│ ├ Sprite.java
│ ├ Animation.java
│ ├ Animator.java
│ ├ AnimatedSprite.java
│ ├ CanRotate.java
│ └ Layer.java
│
├ node/
│ ├ event/
│ │ └ Button.java
│ ├ ui/
│ │ └ Text.java
│ ├ Node.java
│ ├ Entity.java
│ └ Area.java
│
├ signal/
│ ├ Signal.java
│ └ SignedSignal.java
│
├ GameState.java
├ StateManager.java
├ NodeManager.java
├ LayerManager.java
└ AssetManager.java
```

> **audio**

- ⚠️♻️ [Sound](/src/game/core/audio/Sound.java) - Basic utility component for playing sounds.
- ⚠️ [SoundData](/src/game/core/audio/SoundData.java) - Store and caches sound data.
  - **CONSTRUCTOR**
    - `name: String` - path name to the file after [assets/sounds/](/src/assets/sounds/)
  - Always call `dispose()` everytime you are sure the instance won't be used later anymore.

> **graphics**

- ⚠️♻️ [Sprite](/src/game/core/graphics/Sprite.java) - Store and caches image data. Has basic utilities for modifying properties. Has it's own logic update and draw callables.
  - **CONSTRUCTOR**
    - `textureName: String` - path name to the file after [assets/textures/](/src/assets/textures/)
      - **Handles `null` input.** This will skip `image` `name` `width` `height` initialization. **Not recommended** unless you are deriving from this class and you need to skip initialization of this class.
- ⚠️♻️ [Animation](/src/game/core/graphics/Animation.java) - Store frames and metadata only.
- ⚠️♻️ [Animator](/src/game/core/graphics/Animator.java) - Basic utility component for playing animations.
- ⚠️♻️ [AnimatedSprite](/src/game/core/graphics/AnimatedSprite.java) - Same as [Sprite,](/src/game/core/graphics/Sprite.java) but support animations.
- ⚠️📘 [CanRotate [Interface]](/src/game/core/graphics/CanRotate.java)
- ❌ [Layer](/src/game/core/graphics/Layer.java) - Helper class for painting order. Managed by [LayerManager](/src/game/core/LayerManager.java) and [NodeManager.](/src/game/core/NodeManager.java)

> **node**

- ⚠️♻️📘 [Node](/src/game/core/node/Node.java) - Base class of all derived game object classes and game objects.
- ⚠️♻️📘 [Entity](/src/game/core/node/Entity.java) - Derived from [Node](/src/game/core/node/Node.java) - More physic properties.
- ⚠️♻️📘 [Area](/src/game/core/node/Area.java) - Derived from [Entity](/src/game/core/node/Entity.java) - Used to detect if **mouse cursor** is in the area.
- ⚠️♻️📘 [Button](/src/game/core/node/ui/Button.java) - Derived from [Area](/src/game/core/node/Area.java) - Fires signal on button click.
- ⚠️♻️📘 [Text](/src/game/core/node/ui/Text.java) - Derived from [Node](/src/game/core/node/Node.java)

> **signal**

- ⚠️♻️ [Signal](/src/game/core/signal/Signal.java) - Utility component for sending signals.
- ⚠️♻️ [SignedSignal](/src/game/core/signal/SignedSignal.java) - [Signal](/src/game/core/signal/Signal.java) but with assured specified signal name as 1st argument.

> **managers**

- ⚠️📘 [GameState](/src/game/core/GameState.java)
- ❌ [StateManager](/src/game/core/StateManager.java) - Manage state update loops & state changes.
  - Call `setState()` whenever you want to change the **entire state** to another.
- ❌ [NodeManager](/src/game/core/NodeManager.java) - Handles update loop for every game [Nodes](/src/game/core/node/Node.java)
  - Call `getNodeManagerInstance().addNode()` everytime you just created new nodes, so it can be handled by NodeManager **otherwise your object won't be processed and appear in-game.**
  - If you are setting said **nodes as one of your node's children** via `this.addChild(node)` or `node.setParent(this)` then `getNodeManagerInstance().addNode()` will be handled automatically, so no need to call it again.
  - The opposite also applies for `getNodeManagerInstance().removeNode()` `node.setParent(null)` `this.removeChild(node)`
- ❌ [LayerManager](/src/game/core/LayerManager.java) - Handles painting order for every game [Nodes](/src/game/core/node/Node.java)
  - The higher value of each `Node.layer`, the more it goes above other nodes.
- ❌ [AssetManager](/src/game/core/AssetManager.java) - Fetches and caches assets.
  - `getTexture()` will try and fetch image textures from [assets/textures/](/src/assets/textures/), which will **cause RuntimeException if it's not already existed** at least in storage caching. Consider using `getTextureSafe()` if you just want to check and get textures that's already existed in storage caching.
  - `addTexture()` will only add to storage caching and **does not create a new file at runtime.**

## util/

This directory contains static utilities that can be called globally.

- ❌ [Time](/src/game/util/Time.java) - only properties `deltaTime` `FIXED_DELTA` and method `getTextCurrent()` shall be called.
- ✅ [Log](/src/game/util/Log.java)
- ✅ [Id](/src/game/util/Id.java)
- ✅ Everything in [Debug/](/src/game/util/Debug/) directory is alterable and should be for creating convenience tools and ease of debugging experience for the team.

## input/

This directory contains **custom input handlers** that actively listen to input events on game runtime. You can **call methods globally** to check input events on `fixedUpdate()` loop.

> **Only call methods under region `= QUERY API =`**

- ❌ [KeyInput](/src/game/input/KeyInput.java)
- ❌ [MouseInput](/src/game/input/MouseInput.java)

## states/

This directory contain states that can be managed in the game logic. A state contains all the nodes needed for the session. **It is not recommended to be used as a game object/node.** Have it only act like a scene space of that session.

## nodes/

This directory contains game nodes that are made and used exclusively for this game. Everything in here is **✅ alterable at will,** but requires communication in-between team members of this project.
