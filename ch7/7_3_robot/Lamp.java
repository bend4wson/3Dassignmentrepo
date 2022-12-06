import gmaths.*;
//import Math;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;


//   public void initialise(GL3 gl) {
//     createRandomNumbers();

//     Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
//     Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
//     Shader shaderStatic = new Shader(gl, "vs_tt_05_static.txt", "fs_tt_05.txt");
//     Shader shaderWindow = new Shader(gl, "vs_tt_05_static.txt", "fs_tt_05_window.txt");

//     //int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
//     int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
//     int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
//     int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
//     int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/woodCeiling3.jpg");
//     //int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
//     int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");
//     int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/ear0xuu2.jpg");
//     int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/cloud2.jpg");
//     int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/medievalTown.jpg");
//     int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/mar0kuu2_specular.jpg");
//     int[] textureId11 = TextureLibrary.loadTexture(gl, "textures/woodWallColours.jpg");
//     int[] textureId12 = TextureLibrary.loadTexture(gl, "textures/woodFloor2.jpg");
//     int[] textureId13 = TextureLibrary.loadTexture(gl, "textures/woodCeiling3.jpg");
//     int[] textureId14 = TextureLibrary.loadTexture(gl, "textures/glass.jpg");

//     //lampLight1.setCamera(camera);
//     lampLight1 = new Light(gl);
//     lampLight1.setCamera(camera);
//     light2 = new Light(gl);
//     light2.setCamera(camera);
//     light3 = new Light(gl);
//     light3.setCamera(camera);
//     light4 = new Light(gl);
//     light4.setCamera(camera);

// //----------------------//----------------------//----------------------//----------------------//----------------------
//     // double elapsedTime = getSeconds() - startTime;
//     // shader.use(gl);
//     // double t = elapsedTime*0.1;
//     // float offsetX = (float)(t - Math.floor(t));
//     // float offsetY = 0.0f;
//     // shader.setFloat(gl, "offset", offsetX, offsetY);
    
//     // shader.setInt(gl, "first_texture", 0);
//     // shader.setInt(gl, "second_texture", 1);

//     // gl.glActiveTexture(GL.GL_TEXTURE0);
//     // gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1);
//     // gl.glActiveTexture(GL.GL_TEXTURE1);
//     // gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2);
  
//     // gl.glBindVertexArray(vertexArrayId[0]);
//     // gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
//     // gl.glBindVertexArray(0);
    
//     // gl.glActiveTexture(GL.GL_TEXTURE0);
// //----------------------//----------------------//----------------------//----------------------//----------------------
//     //lightShader = new Shader(gl, "vs_light_01.txt", "fs_light_01.txt");
//     //lampLight1 = new Model(gl, camera, light, lightShader, material, modelMatrix, mesh, textureId4, textureId4);


//     Material material = new Material(new Vec3(1f, 1f, 1f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
//     Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
//     ttFloor = new Model(gl, camera, lampLight1, shaderStatic, material, modelMatrix, mesh, textureId12);
//     ttWindow = new Model(gl, camera, lampLight1, shaderWindow, material, modelMatrix, mesh, textureId14);
//     ttWall = new Model(gl, camera, lampLight1, shaderStatic, material, modelMatrix, mesh, textureId11);
//     ttBackground = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId9);
//     ttCeiling = new Model(gl, camera, light3, shaderStatic, material, modelMatrix, mesh, textureId13);

//     mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
//     shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
//     material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
//     modelMatrix = Mat4.multiply(Mat4Transform.scale(4, 4, 4), Mat4Transform.translate(0, 0.5f, 0));
//     cube = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId4, textureId4);
//     cube2 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);
//     cube3 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);
//     cube4 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);
//     cube5 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);
//     cube6 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId3, textureId3);

//     tableLeg = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId13, textureId13);

//     mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
//     shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
//     material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
//     modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
//     sphere = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId1, textureId2);
//     jointSphere = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);
//   }

public class Lamp {
    private Model cube, cube2, cube3, cube4, cube5, cube6, lightCube1, lightCube2, sphere, jointSphere, tableLeg, ttWall, ttFloor, ttWindow, ttCeiling, ttBackground;
    private SGNode lampRoot;

    private SGNode createLamp(num) {//, int lArmAngleZ, lArmAngleY, uArmAngleZ, headAngleZ)
        //if num == 1:
            //System.out.println("***");
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
            // float headLightAngle = 20f;


            SGNode lampRoot = new NameNode("lamp-branch-structure");
            //translateLamp = new TransformNode("lamp 1 transform",Mat4Transform.translate(xPosition,0,0));
            lArmTransform = new TransformNode("lArm 1 transform",Mat4Transform.translate(xPosition,0,0));
            // rotateUpper = new TransformNode("Rotate Upper Lamp1",Mat4Transform.translate(xPosition,0,0));
            // rotateLowerZ = new TransformNode("Rotate Lower Lamp1Z",Mat4Transform.translate(xPosition,0,0));
            rotateHead = new TransformNode("Rotate Head", Mat4Transform.translate(xPosition, 0, 0));


            // rotateHead = new TransformNode("rotateHead",Mat4Transform.translate(xPosition,0,0));
            
            
            //branchRoot = new NameNode("sphere-branch-structure");

                                                                                    //Prev (xPosition, 0, 0)
            rotateLamp = new TransformNode("rotateLamp",Mat4Transform.translate(xPosition,0,0));
            lamp1MoveTranslate = new TransformNode("lamp1MoveTranslate",Mat4Transform.translate(lamp1MoveVec));

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            NameNode base = new NameNode("base");
                // m = new Mat4(1);
                // m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
                // TransformNode lArmTranslate = new TransformNode("lArmTranslate", m);

                
                //m = new Mat4(1);
                Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
                //Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
                m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
                TransformNode baseTransform = new TransformNode("baseTransform", m);
                ModelNode baseShape = new ModelNode("baseShape(Cube)", cube5);
                //TransformNode baseRotate = new TransformNode("base rotate", m);

            //rotateLowerZ = new TransformNode("Rotate Lower",Mat4Transform.translate(xPosition,0,0));

                rotateLamp = new TransformNode("rotateLamp", Mat4Transform.rotateAroundY(rotateLampY));

            // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

            NameNode lArm = new NameNode("lArm"); 
                TransformNode lArmPosTransform = new TransformNode("lArmPosTransform", Mat4Transform.translate(0, 0, 0));

                //m = new Mat4(1);
                m = Mat4Transform.rotateAroundZ(lowerZAngle);
                rotateLowerZ = new TransformNode("rotateLowerZ", m);
                //rotateLowerZ = new TransformNode("rotateLowerZ", m);

                //m = new Mat4(1);
                m = Mat4Transform.rotateAroundY(lowerYAngle);
                rotateLowerY = new TransformNode("rotateLowerY", m);
                //rotateLowerY = new TransformNode("rotateLowerY", m);


                //m = new Mat4(1);
                m = Mat4Transform.scale(lArmScale,lArmHeight,lArmScale);
                m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); //-4.9f-1.2f,height1,0));//(lArmFloorLen/2),height1,this.lArmTranslateZ));

                //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));    (baseHeight+(lArmHeight/2.0f))
                TransformNode lArmTransform = new TransformNode("lArmTransform", m);
                ModelNode lArmShape = new ModelNode("lArmShape(Cube)", cube2);

                TransformNode translateTopLArm = new TransformNode("translateTopLArm", Mat4Transform.translate(0, (baseHeight+lArmHeight), 0));

            //Translate to origin, scale, etc.
            //When scaling something, always refer it to f.
            //Rotate should be in spearate node (if it affects nodes underneath)

            //Define a translate with respect to the origin of the node underneath it 
            //(i.e:(0,0,0) is the node underneath), so you just have to translate by the 
            //length of the current object you're defining.

            //rotateUpper = new TransformNode("Rotate Upper", Mat4Transform.translate(xPosition,0,0));

            // System.out.println("||||||||||||||||||||||||||||||||||||");

            NameNode joint = new NameNode("joint");
                //Mat4Transform.translate((bodyWidth*0.5f)+(armScale*0.5f),bodyHeight,0));
                //leftArmRotate = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));

                //m = new Mat4(1);
                m = Mat4Transform.scale(jointScale,jointScale,jointScale);
                // float height = (baseHeight+lArmHeight+(float)(jointScale/2));
                m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));            // -9, 2.85f, 0));
                TransformNode jointTransform = new TransformNode("jointTransform", m);
                ModelNode jointShape = new ModelNode("jointShape", jointSphere);
                

            NameNode uArm = new NameNode("uArm"); 
                rotateUpper = new TransformNode("rotateUpper (" + upperAngle + ")", Mat4Transform.rotateAroundZ(upperAngle)); //Mat4Transform.rotateAroundZ(-30));
                //rotateUpper = new TransformNode("rotateUpper (" + this.upperAngle + ")", Mat4Transform.rotateAroundZ(this.upperAngle));


                // m = new Mat4(1);
                // //float rotationUpperXOffset = (uArmHeight * (float)Math.sin(this.upperAngle));
                // //System.out.println("@@@ " + rotationUpperXOffset + " @@@ " + (float)Math.sin(this.upperAngle));
                // m = Mat4.multiply(m, Mat4Transform.translate(1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f));//(float)uArmHeight/2, 0f));//(float)(lArmHeight+(uArmHeight/2)+(jointScale/2)), 0f)); //1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f));//0f, 0.5f, 0f)); //1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f));         //(lArmHeight+(uArmHeight/2)), 0f));
                // uArmTranslate = new TransformNode("uArmTranslate", m);

            //m = new Mat4(1);
                //m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)(uArmHeight/2), 0f)); //-7f, -0.15f, 0f));
                m = Mat4Transform.scale(uArmScale,uArmHeight,uArmScale);
                m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); //1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f)); //0f, 0.5f, 0f));
                //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
                //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
                TransformNode uArmTransform = new TransformNode("uArmTransform", m);
                ModelNode uArmShape = new ModelNode("uArmShape", cube2);

                TransformNode translateTopUArm = new TransformNode("translateTopUArm", Mat4Transform.translate(0f, (float)(uArmHeight), 0f));
            
            NameNode head = new NameNode("head");
            //   m = new Mat4(1);
            // // Mat4 m = Mat4Transform.scale(headDepth,headHeight,headWidth);
            //   //headHeight = baseHeight+lArmHeight+jointScale+uArmHeight+(headHeight/2.0f)+2.5f;
            //   m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)((uArmHeight/2)), 0f)); //-3.5f,(baseHeight+lArmHeight+jointScale+uArmHeight+(headHeight/2.0f))+2.5f,0));
            //   TransformNode translateHead = new TransformNode("translateHead", m);

                // m = new Mat4(1);
                // m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(headLightAngle)); //(baseHeight+lArmHeight+(jointScale/2)-1)
                
                
                
                rotateHead = new TransformNode("rotateHead", Mat4Transform.rotateAroundZ(headLightAngle));
                //rotateHead = new TransformNode("rotateHead", Mat4Transform.rotateAroundZ(headLightAngle));

                //m = new Mat4(1);
                m = Mat4Transform.scale(headDepth,headHeight,headWidth);
                m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));
                TransformNode headTransform = new TransformNode("headTransform", m);
                ModelNode headShape = new ModelNode("headShape", cube4);
            // NameNode lampLight1 = new NameNode("lamp light 1");
            //     m = new Mat4(1);
            //     m = Mat4.multiply(m, Mat4Transform.translate((float)(headDepth/1.9f), 0f, 0f));
            //     TransformNode translateLampLight1 = new TransformNode("lamp light 1 translate", m);

            //     m = new Mat4(1);
            //     m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(headLightAngle));
            //     rotateLampLight1 = new TransformNode("lamp light 1 rotate", m);

            //     m = new Mat4(1);
            //     m = Mat4.multiply(m, Mat4Transform.scale(lampLight1Scale, lampLight1Scale, lampLight1Scale));
            //     TransformNode lampLight1Transform = new TransformNode("lamp light 1 transform", m);
            //       ModelNode lampLight1Shape = new ModelNode("Cube(light)", lampLight1);

            // System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{}}}}}}}}}}}}}}}}}}}}}}}}}");

            lampRoot.addChild(lamp1MoveTranslate);
            //Do I need lamp1MoveTranslate here?
            lamp1MoveTranslate.addChild(base); //rotateLamp);
                //rotateLamp.addChild(base);
                base.addChild(baseTransform);
                    baseTransform.addChild(baseShape);
                base.addChild(rotateLamp); //lArmTranslate);
                    rotateLamp.addChild(lArm);
                        lArm.addChild(lArmPosTransform);
                        // \/ Translates to origin for the rotation
                        lArmPosTransform.addChild(rotateLowerY); //translateTopBase);
                        //translateTopBase.addChild(rotateLowerY);
                            rotateLowerY.addChild(rotateLowerZ);
                            rotateLowerZ.addChild(lArmTransform);
                                lArmTransform.addChild(lArmShape);
                            rotateLowerZ.addChild(translateTopLArm);
                                translateTopLArm.addChild(joint);
                                joint.addChild(jointTransform);
                                    jointTransform.addChild(jointShape);
                                joint.addChild(uArm);//translateMiddleJoint);
                                    //translateMiddleJoint.addChild(uArm);
                                    uArm.addChild(rotateUpper);
                                        rotateUpper.addChild(uArmTransform);
                                        uArmTransform.addChild(uArmShape);
                                        rotateUpper.addChild(translateTopUArm);
                                        translateTopUArm.addChild(head);
                                            head.addChild(rotateHead);
                                            rotateHead.addChild(headTransform);
                                                headTransform.addChild(headShape);
            System.out.println("END OF SCENE GRAPH");
            //lamp1Root.update();
            // System.out.println("***");
            return lampRoot;
    }
}