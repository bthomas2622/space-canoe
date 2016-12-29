# **SPACE CANOE** by Ben Thomas
[Download Page](https://freebrunch.itch.io/space-canoe)

## **About**

Space Canoe is a 2D arcade style game in a similar vein as the classic "asteroids" created using the LibGDX framework. The player uses the left and right arrows (left and right screen touch on mobile) to paddle the canoe away from the perilous space debris. The goal is to survive as long as possible in order to obtain a high "debris dodged" score. 


### How to Play 

The player uses the *left* and *right* arrow keys in the game screen to paddle the canoe away from the space debris. The space canoe functions similarly to a real canoe where a paddle on the right side of the canoe pushes the canoe towards the left. The player canoe in "Space Canoe" is locked onto the center of the screen and rotates with paddles in 30 degree increments around the full 360 degree range of motion. Each paddle force pushes the canoe towards or away from the space debris depending on the orientation of the canoe and the side at which the paddle took place. The longer the player survives the larger the debris dodged score will be and the more perilous space will become. 

### How to "Run"

Machine must have java installed. EXE run file available for windows. JAR run file available for Mac and Linux users. Hosted on [itch.io](https://freebrunch.itch.io/space-canoe).

## How it was programmed

#### Tools
Space Canoe was programmed in Java through the **LibGDX** framework in the Android Studio IDE. The **Box2D** physics extension was utilized to create physics bodies the represent the canoe and space debris. This allows for realistic collisions and the ability to apply force to the space debris from a canoe paddle action. The **"freetype"** library within LibGDX was used to apply ttf font files from Google Fonts in the game. The **"TexturePacker"** tool within the "Tools" LibGDX extension was utilized to create spritesheets from images in order to create title animation. 

**Adobe Photoshop** was used to draw the space background, canoe space debris. 

#### Design
I set out to make a trippy rhythmic arcade game about a canoe paddling through space. I chose to lock the canoe at the center of the screen and simulate the motion of the canoe through the forces applied to the space debris around the canoe. This is easier to code because very little information needs to be tracked that is not within the game camera. I set the canoe rotation per paddle at 30 degrees because it felt like a natural representation of the real mechanics of a canoe. When a canoe is paddled the change in direction is suttle not a total turn. 30 degrees is what I found to be the minimal turn made by the player without giving the sensation of little control over canoe in game. I originally wanted this game to be a rhythm game in the realm of "Crypt of the Necrodancer", meaning the game would only accept input if it was "on the beat". I quickly found during playtesting however that a great deal of the fun in Space Canoe came from frantically paddling in fast paced space chaos. I felt I needed to abandon the rhythm gameplay at this point to allow for more fast twitch paddling. 

## Additional Information

#### Directory Structure

The *"Desktop"*, *"Android"*, and *"HTML"* folders hold the gradle build profiles and launcher classes for their respective platforms. All of the games art and sound assets are stored and referenced in the Android *"Assets"* folder. The *"src"* folder in the *"core"* folder holds the game logic files. Space Canoe uses the "screen" LibGDX feature so each java game file corresponds to a game screen. Space Canoe has 3 screens. The *"MainMenuScreen.java"* file holds the main menu screen that launches at start of game. The *"GameScreen.java"* files holds the space canoe game screen where the actual game is played. The *"GameOverScreen.java"* file is triggered when a collision happens and the player hits game over. The *"SpaceCanoe.java"* file is the game class that LibGDX defaults to to launch the game. In Space Canoe the game class simply establishes the game object and points to the main menu screen to start the game. 

#### Contributing

Anyone is welcome to re-use the code used in this project.

#### References

* [LibGDX](https://libgdx.badlogicgames.com/)
* [LibGDX WIKI](https://github.com/libgdx/libgdx/wiki)
* [GamesFromScratch LibGDX Tutorial Series](http://www.gamefromscratch.com/page/LibGDX-Tutorial-series.aspx)
* [Box2D LibGDX WIKI](https://github.com/libgdx/libgdx/wiki/Box2d)
* [BFXR Sound Effect Creator](http://www.bfxr.net/)
* [Google Fonts](https://fonts.google.com/)
* [Freesound.org](http://freesound.org/)
* [Android Studio](https://developer.android.com/studio/index.html)

#### Contact Me

For any questions please email me at _bthomas2622@gmail.com_

#### License

The content of this repository is not licensed. 