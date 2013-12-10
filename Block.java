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

		System.out.println("Block at "+x+" "+y+" "+z);

		float d=1.0f;
		// draw quads
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+0.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+0.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+1.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+1.0f, this.z+0.0f);
		GL11.glEnd();

		d=0.9f;
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+0.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+1.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+1.0f, this.z+1.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+0.0f, this.z+1.0f);
		GL11.glEnd();

		d=0.8f;
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+0.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+0.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+0.0f, this.z+1.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+0.0f, this.z+1.0f);
		GL11.glEnd();

		d=0.7f;
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+0.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+1.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+1.0f, this.z+1.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+0.0f, this.z+1.0f);
		GL11.glEnd();

		d=1.1f;
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+0.0f, this.z+1.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+0.0f, this.z+1.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+1.0f, this.z+1.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+1.0f, this.z+1.0f);
		GL11.glEnd();

		d=1.2f;
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+1.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+1.0f, this.z+0.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+1.0f, this.y+1.0f, this.z+1.0f);
		GL11.glColor3f(d*m.color.R, d*m.color.G, d*m.color.B);
		GL11.glVertex3f(this.x+0.0f, this.y+1.0f, this.z+1.0f);
		GL11.glEnd();
	}

    void setMaterial(Material m) {
        this.m = m;
    }
}
