/* I declare that this code is my own work*/
/* Author Ben Dawson bcdawson1@sheffield.ac.uk */

import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Lamp {
    private Model cube, cube2, cube3, cube4, cube5, cube6, lightCube1, lightCube2, sphere, jointSphere, tableLeg, ttWall, ttFloor, ttWindow, ttCeiling, ttBackground;
    private SGNode lampRoot;

    private SGNode createLamp(num) {
            if (num == 1){
                Vec3 lamp1MoveVec = new Vec3(-5f, 0f, 0f);
                int rotateLampY = 0;
                float lowerYAngle = 0;
                float lowerZAngle = 30;
                float upperAngle = -60;
                float headLightAngle = 20f;
            }

            else if (num == 2) {
                Vec3 lamp1MoveVec = new Vec3(5f, 0f, 0f);
                int rotateLampY = 0;
                float lowerYAngle = 0;
                float lowerZAngle = -30;
                float upperAngle = 60;
                float headLightAngle = -20f;
            }

            float baseHeight = 0.35f;
            float baseScale = 1.5f;
            float jointScale = 0.75f;
            float headHeight = 0.5f;
            float headWidth = 0.75f;
            float headDepth = 1.5f;
            float uArmHeight = 2.5f;
            float uArmScale = 0.35f;
            float lArmHeight = 2.5f;
            float lArmScale = 0.35f;
            float lampLight1Scale = 0.25f;


            SGNode lampRoot = new NameNode("lamp-branch-structure");
            lArmTransform = new TransformNode("lArm 1 transform",Mat4Transform.translate(xPosition,0,0));
            rotateHead = new TransformNode("Rotate Head", Mat4Transform.translate(xPosition, 0, 0));                                                             //Prev (xPosition, 0, 0)
            rotateLamp = new TransformNode("rotateLamp",Mat4Transform.translate(xPosition,0,0));
            lamp1MoveTranslate = new TransformNode("lamp1MoveTranslate",Mat4Transform.translate(lamp1MoveVec));

            NameNode base = new NameNode("base");
                Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
                m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
                TransformNode baseTransform = new TransformNode("baseTransform", m);
                ModelNode baseShape = new ModelNode("baseShape(Cube)", cube5);

                rotateLamp = new TransformNode("rotateLamp", Mat4Transform.rotateAroundY(rotateLampY));

            NameNode lArm = new NameNode("lArm"); 
                TransformNode lArmPosTransform = new TransformNode("lArmPosTransform", Mat4Transform.translate(0, 0, 0));

                m = Mat4Transform.rotateAroundZ(lowerZAngle);
                rotateLowerZ = new TransformNode("rotateLowerZ", m);

                m = Mat4Transform.rotateAroundY(lowerYAngle);
                rotateLowerY = new TransformNode("rotateLowerY", m);

                m = Mat4Transform.scale(lArmScale,lArmHeight,lArmScale);
                m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f));

                TransformNode lArmTransform = new TransformNode("lArmTransform", m);
                ModelNode lArmShape = new ModelNode("lArmShape(Cube)", cube2);

                TransformNode translateTopLArm = new TransformNode("translateTopLArm", Mat4Transform.translate(0, (baseHeight+lArmHeight), 0));

            NameNode joint = new NameNode("joint");
                m = Mat4Transform.scale(jointScale,jointScale,jointScale);
                m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));            // -9, 2.85f, 0));
                TransformNode jointTransform = new TransformNode("jointTransform", m);
                ModelNode jointShape = new ModelNode("jointShape", jointSphere);
                

            NameNode uArm = new NameNode("uArm"); 
                rotateUpper = new TransformNode("rotateUpper (" + upperAngle + ")", Mat4Transform.rotateAroundZ(upperAngle));
                m = Mat4Transform.scale(uArmScale,uArmHeight,uArmScale);
                m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); 
                TransformNode uArmTransform = new TransformNode("uArmTransform", m);
                ModelNode uArmShape = new ModelNode("uArmShape", cube2);

                TransformNode translateTopUArm = new TransformNode("translateTopUArm", Mat4Transform.translate(0f, (float)(uArmHeight), 0f));
            
            NameNode head = new NameNode("head");
                rotateHead = new TransformNode("rotateHead", Mat4Transform.rotateAroundZ(headLightAngle));
                m = Mat4Transform.scale(headDepth,headHeight,headWidth);
                m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));
                TransformNode headTransform = new TransformNode("headTransform", m);
                ModelNode headShape = new ModelNode("headShape", cube4);

            lampRoot.addChild(lamp1MoveTranslate);
                lamp1MoveTranslate.addChild(base); 
                    base.addChild(baseTransform);
                        baseTransform.addChild(baseShape);
                    base.addChild(rotateLamp); 
                        rotateLamp.addChild(lArm);
                            lArm.addChild(lArmPosTransform);
                            lArmPosTransform.addChild(rotateLowerY); 
                                rotateLowerY.addChild(rotateLowerZ);
                                rotateLowerZ.addChild(lArmTransform);
                                    lArmTransform.addChild(lArmShape);
                                rotateLowerZ.addChild(translateTopLArm);
                                    translateTopLArm.addChild(joint);
                                    joint.addChild(jointTransform);
                                        jointTransform.addChild(jointShape);
                                    joint.addChild(uArm);
                                        uArm.addChild(rotateUpper);
                                            rotateUpper.addChild(uArmTransform);
                                            uArmTransform.addChild(uArmShape);
                                            rotateUpper.addChild(translateTopUArm);
                                            translateTopUArm.addChild(head);
                                                head.addChild(rotateHead);
                                                rotateHead.addChild(headTransform);
                                                    headTransform.addChild(headShape);
            return lampRoot;
    }
}