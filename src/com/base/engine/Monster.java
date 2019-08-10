package com.base.engine;

public class Monster
{
	public static final float SCALE = 0.7f;
	public static final float SIZEY = SCALE;
	public static final double constant = (56.0 / 29.0);
	public static final float SIZEX = (float)((double)SIZEY / (constant * 2.0));
    public static final float START = 0;
    
    // use these if loading textures with slick-util
    //public static final float OFFSET_X = 0.05f;
    //public static final float OFFSET_Y = 0.01f;
    
    public static final float OFFSET_X = 0.0f;
    public static final float OFFSET_Y = 0.0f;
    
    public static final float TEX_MIN_X = -OFFSET_X;
    public static final float TEX_MAX_X = -1 - OFFSET_X;
    public static final float TEX_MIN_Y = -OFFSET_Y;
    public static final float TEX_MAX_Y = 1 - OFFSET_Y;
    
	
	private static Mesh mesh;
    private Material material;
    private Transform transform;
	
	public Monster(Transform transform)
	{
		this.transform = transform;
		material = new Material(new Texture("SSWVA1.png"), new Vector3f(1, 1, 1)); 
		
        if (mesh == null)
        {
            Vertex[] vertices = new Vertex[] {

                    // front
                    new Vertex(new Vector3f(-SIZEX, START,  START), new Vector2f(TEX_MAX_X, TEX_MAX_Y)),
                    new Vertex(new Vector3f(-SIZEX, SIZEY,  START), new Vector2f(TEX_MAX_X, TEX_MIN_Y)),
                    new Vertex(new Vector3f(SIZEX,  SIZEY,  START), new Vector2f(TEX_MIN_X, TEX_MIN_Y)),
                    new Vertex(new Vector3f(SIZEX,  START,  START), new Vector2f(TEX_MIN_X, TEX_MAX_Y))
            };
            int[] indices = new int[]{
                    0,1,2,
                    0,2,3
            };
            mesh = new Mesh(vertices, indices);
        }
	}
	
	public void update()
	{
	}
	
	public void render()
	{
        Shader shader = Game.getLevel().getShader();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
	}
	
	
}
