/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package jtkdemo.sgl;

import java.awt.*;
import java.nio.FloatBuffer;

import edu.mines.jtk.sgl.*;
import edu.mines.jtk.util.Direct;
import static edu.mines.jtk.ogl.Gl.*;

/**
 * A simple cube with colored sides.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.27
 */
public class ColorCubeDemo extends Node implements Selectable {

  public void pick(PickContext pc) {
    Segment ps = pc.getPickSegment();
    for (int iside=0; iside<6; ++iside) {
      double xa = _va[12*iside+ 0];
      double ya = _va[12*iside+ 1];
      double za = _va[12*iside+ 2];
      double xb = _va[12*iside+ 3];
      double yb = _va[12*iside+ 4];
      double zb = _va[12*iside+ 5];
      double xc = _va[12*iside+ 6];
      double yc = _va[12*iside+ 7];
      double zc = _va[12*iside+ 8];
      double xd = _va[12*iside+ 9];
      double yd = _va[12*iside+10];
      double zd = _va[12*iside+11];
      Point3 p = ps.intersectWithTriangle(xa,ya,za,xb,yb,zb,xc,yc,zc);
      Point3 q = ps.intersectWithTriangle(xa,ya,za,xc,yc,zc,xd,yd,zd);
      if (p!=null)
        pc.addResult(p);
      if (q!=null)
        pc.addResult(q);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void selectedChanged() {
    System.out.println("ColorCubeDemo: "+this+" selected="+isSelected());
    dirtyDraw();
  }

  protected BoundingSphere computeBoundingSphere(boolean finite) {
    Point3 c = new Point3(0.5,0.5,0.5);
    double r = 0.5*Math.sqrt(3.0);
    return new BoundingSphere(c,r);
  }

  protected void draw(DrawContext dc) {
    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_NORMAL_ARRAY);
    glEnableClientState(GL_COLOR_ARRAY);
    glVertexPointer(3,GL_FLOAT,0,_vb);
    glNormalPointer(GL_FLOAT,0,_nb);
    glColorPointer(3,GL_FLOAT,0,_cb);
    if (isSelected()) {
      glEnable(GL_POLYGON_OFFSET_FILL);
      glPolygonOffset(1.0f,1.0f);
    }
    glDrawArrays(GL_QUADS,0,24);
    glDisableClientState(GL_NORMAL_ARRAY);
    glDisableClientState(GL_COLOR_ARRAY);
    if (isSelected()) {
      glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
      glDisable(GL_LIGHTING);
      glColor3d(1.0,1.0,1.0);
      glDrawArrays(GL_QUADS,0,24);
    }
    glDisableClientState(GL_VERTEX_ARRAY);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Vertices, normals, and colors.
  private static float[] _va = {
     0.0f, 0.0f, 0.0f,  0.0f, 0.0f, 1.0f,  0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 0.0f,
     0.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,
     0.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 0.0f, 0.0f,
     1.0f, 0.0f, 0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 1.0f,  1.0f, 0.0f, 1.0f,
     0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 1.0f,  1.0f, 1.0f, 1.0f,  1.0f, 1.0f, 0.0f,
     0.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,  1.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,
  };
  private static float[] _na = {
    -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
     0.0f,-1.0f, 0.0f,  0.0f,-1.0f, 0.0f,  0.0f,-1.0f, 0.0f,  0.0f,-1.0f, 0.0f,
     0.0f, 0.0f,-1.0f,  0.0f, 0.0f,-1.0f,  0.0f, 0.0f,-1.0f,  0.0f, 0.0f,-1.0f,
     1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,
     0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,
     0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,
  };
  private static float[] _ca = {
     0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,  0.0f, 1.0f, 1.0f,
     1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f,
     1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,  1.0f, 1.0f, 0.0f,
     1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f,
     0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,
     0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f,
  };
  private static FloatBuffer _vb = Direct.newFloatBuffer(_va);
  private static FloatBuffer _nb = Direct.newFloatBuffer(_na);
  private static FloatBuffer _cb = Direct.newFloatBuffer(_ca);

  ///////////////////////////////////////////////////////////////////////////
  // demoing

  public static void main(String[] args) {
    StateSet states = new StateSet();
    MaterialState ms = new MaterialState();
    ms.setColorMaterialFront(GL_AMBIENT_AND_DIFFUSE);
    ms.setSpecularFront(Color.white);
    ms.setShininessFront(100.0f);
    states.add(ms);

    ColorCubeDemo cc1 = new ColorCubeDemo();
    ColorCubeDemo cc2 = new ColorCubeDemo();
    cc1.setStates(states);
    cc2.setStates(states);

    TransformGroup tg1 = new TransformGroup(Matrix44.translate(-2,0,0));
    TransformGroup tg2 = new TransformGroup(Matrix44.translate( 2,0,0));
    tg1.addChild(cc1);
    tg2.addChild(cc2);

    World world = new World();
    world.addChild(tg1);
    world.addChild(tg2);

    DemoFrame frame = new DemoFrame(world);
    frame.setVisible(true);
  }
}
