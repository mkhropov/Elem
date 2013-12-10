/**
 * Block is a class describing a minimal single 3D voxel
*/
import org.lwjgl.opengl.GL11;



public class Block {
    // this properties are given for example
    int x, y, z; //in a chunk
    int T; //temperature
    Material m; //to be changed to a class
    int liquidLevel;
    int liquidId;
    Gase gase;
//    void[] items;
//    void[] creatures;

    public Block(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.m = null;
    }

	public void Draw() {
		// Don't draw anything if block is empty
		if (this.m == null) return;

		// draw quads
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+0.0);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+0.0);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+0.0);
		GL11.glNormal3d(0.0, 0.0, -1.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+0.0);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+0.0);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+0.0);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+1.0);
		GL11.glNormal3d(-1.0, 0.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+1.0);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+0.0);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+0.0);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+1.0);
		GL11.glNormal3d(0.0, -1.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+1.0);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+0.0);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+0.0);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+1.0);
		GL11.glNormal3d(1.0, 0.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+1.0);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+0.0, this.z+1.0);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+0.0, this.z+1.0);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+1.0);
		GL11.glNormal3d(0.0, 0.0, 1.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+1.0);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+0.0);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+0.0);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+1.0, this.y+1.0, this.z+1.0);
		GL11.glNormal3d(0.0, 1.0, 0.0);
		GL11.glColor3d(m.color.R, m.color.G, m.color.B);
		GL11.glVertex3d(this.x+0.0, this.y+1.0, this.z+1.0);
		GL11.glEnd();
	}

    void setMaterial(Material m) {
		this.m = m;
    }
}
