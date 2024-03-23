# Fire and Water Game

This project is a platform game developed in Java. The game provides an environment where the player interacts with the character to earn points using simple graphics.

## Running the Code

Normal launch:
```bash
java FireAndWaterGame.java
```
Launch from save file: (path can be specified instead of save.txt)
```bash
java FireAndWaterGame.java save.txt
```

## Game Content

The general content of the game includes:

- **Character Control**: The player can move the character right, left, and upward using the arrow keys on the keyboard.
- **Obstacles and Platforms**: The game area contains platforms of different colors. Some platforms can be stood on, while passing through others can harm the character.
- **Monsters**: Enemy monsters move in the game. The player needs to avoid or defeat them.
- **Points**: The player can increase their score by collecting points scattered throughout the game area.
- **Special Spheres**: Various special spheres are available in the game. Collecting these spheres provides different abilities.

## How to Play?

- **Character Movement**: Use the arrow keys on the keyboard to move the character.
- **Jumping**: Press the up arrow key to make the character jump.
- **Shooting**: Press the space bar to shoot bullets from your character.
- **Color Selection**: Change the character's color by pressing keys 1, 2, and 3.
- **Pausing the Game**: Press "P" to pause and resume the game.
- **Saving and Loading**: Press "K" to save and later load the game state.

## Special Spheres (WILDCARD)

There are five different special spheres in the game:

1. **Defense Sphere**: Monsters bounce back when they touch the character. Lasts for 20 seconds.
2. **Power Sphere**: Doubles the firing rate of bullets. Lasts for 5 seconds.
3. **Jump Sphere**: Increases jumping height. Lasts for 10 seconds.
4. **Hunter Sphere**: When collected, one monster dies.
5. **Freeze Sphere**: Freezes monsters. Lasts for 5 seconds.

## Starting the Game

The project can be run using a Java IDE or by compiling it. When the game starts, a window opens, and the player can begin playing.

## Screen Shot

![Game Screen](https://github.com/erendrcnn/FireAndWaterGame/assets/70805475/84716512-c152-4e39-a0bc-2836fa82e597)



## Development Environment

- Developed using the Java programming language and tested with Java 17.
- The Swing library is used for creating the graphical user interface.
- Java's core libraries are used to manage game mechanics and interactions.

## License

This project is open source and licensed under the [MIT License](LICENSE). Anyone is allowed to use or modify this project for their own purposes.
