The general outline of this project was originally built from the 7_3_robot classes, and then was changed and expanded from there. A lot of the template code ended up being
redundant so it maybe would be been better to start from an empty file (noted for next time).

This code makes use of separate classes in order to produce different parts of the scene.
It also relies on multiple shaders (e.g: A separate vertex shader for the moving background which takes into account a TexCoord offset,
    and a separate fragment shader to take into account the window alpha value).



Improvements:

1. Multiple Light Sources - Almost got there with this but had issues with the fragment shaders and didn't have time to finish implementing.
    Code has been left in which explains how I did this (Using an abstract Light class which is extended by a SpotLight and PointLight class).
    I also created a LightNode class which could of been used to create the objects, which would then be placed into the scene graph.

    I also edited the Light and Model classes in order to do this, so I left these in under the file names NonImplementedFiles/MultipleLightsLight.java and NonImplementedFiles/MultipleLightsModel.java

    I left the code for multiple light sources in Hatch_GLEventListener.java, but commented out.

2. Switching Off Lights - This would have followed on from creating the light sources - the general outline of what I was going to do can be found in MultipleLightsLight.java
    and the SpotLight & PointLight classes themselves.

4. Wobbling egg - I implemented a Z-axis wobble for the egg, however early on had issues with implementing this into the egg scene graph
    along with the jumping and rotating. I didn't have time to fix the scene graph but would be something fixed relatively easily in future.

4. Separate Lamp Class - The current lamp objects are all defined in the Hatch_GLEventListener.java class, however with some more time I would have
    implemented this in a separate file.
        An attempt at this can be found in NonImplementedFiles/Lamp.java