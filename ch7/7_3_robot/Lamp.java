public class Lamp {
    private Model cube, cube2, cube3, cube4, cube5, cube6, lightCube1, lightCube2, sphere, jointSphere, tableLeg, ttWall, ttFloor, ttWindow, ttCeiling, ttBackground;
    private SGNode lampRoot;

    private SGNode createLamp(num) {//, int lArmAngleZ, lArmAngleY, uArmAngleZ, headAngleZ)
        //if num == 1:
            //System.out.println("***");
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
            float headLightAngle = 20f;

            float lowerYAngle = 0;
            float lowerZAngle = 30;

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
            lamp1MoveTranslate = new TransformNode("lamp1MoveTranslate",Mat4Transform.translate(-5f,0,0));

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

                rotateLamp = new TransformNode("rotateLamp", Mat4Transform.rotateAroundY(0));

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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

            System.out.println("||||||||||||||||||||||||||||||||||||");

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
                rotateUpper = new TransformNode("rotateUpper (" + this.upperAngle + ")", Mat4Transform.rotateAroundZ(this.upperAngle)); //Mat4Transform.rotateAroundZ(-30));
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

            System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{}}}}}}}}}}}}}}}}}}}}}}}}}");

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