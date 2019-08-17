package com.base.game;

import com.base.engine.core.Time;
import com.base.engine.core.Transform;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Vertex;

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
    //public static final float OFFSET_FROM_GROUND = -0.075f;
    
    public static final float OFFSET_X = 0.0f;
    public static final float OFFSET_Y = 0.0f;
    public static final float OFFSET_FROM_GROUND = 0.0f;
    
    public static final float TEX_MIN_X = -OFFSET_X;
    public static final float TEX_MAX_X = -1 - OFFSET_X;
    public static final float TEX_MIN_Y = -OFFSET_Y;
    public static final float TEX_MAX_Y = 1 - OFFSET_Y;
    
    public static final int STATE_IDLE = 0;
    public static final int STATE_CHASE = 1;
    public static final int STATE_ATTACK = 2;
    public static final int STATE_DYING = 3;
    public static final int STATE_DEAD = 4;
    
    public static final float MOVE_SPEED = 1.0f; // TODO: adjust
    public static final float MOVEMENT_STOP_DISTANCE = 1.5f;
    public static final float MONSTER_WIDTH = 0.2f;
    public static final float MONSTER_LENGTH = 0.2f;
    
    public static final float SHOOT_DISTANCE = 1000.0f;
    
	private static Mesh mesh;
    private Material material;
    private Transform transform;
    private int state;
	
	public Monster(Transform transform)
	{
		this.transform = transform;
		this.state = STATE_ATTACK;
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
	
	private void idleUpdate(Vector3f orientation, float distance)
	{
		
	}

	private void chaseUpdate(Vector3f orientation, float distance)
	{
		//System.out.println("orientation: " + orientation.toString());
		//System.out.println("distance: " + Float.toString(distance));
		
		if (distance > MOVEMENT_STOP_DISTANCE)
		{
			float moveAmount = -MOVE_SPEED * (float)Time.getDelta();
			
			Vector3f oldPos = transform.getTranslation();
			Vector3f newPos = transform.getTranslation().add(orientation.mul(moveAmount));
			
			Vector3f collisionVector = Game.getLevel().checkCollision(oldPos, newPos, MONSTER_WIDTH, MONSTER_LENGTH);
			
			Vector3f movementVector = collisionVector.mul(orientation);
			
			if (movementVector.length() > 0)
			{
				transform.setTranslation(transform.getTranslation().add(movementVector.mul(moveAmount)));
			}
			
			// open door if colliding with something
			if (movementVector.sub(orientation).length() != 0)
			{
				Game.getLevel().openDoors(transform.getTranslation());
			}
		}
	}

	private void attackUpdate(Vector3f orientation, float distance)
	{
		Vector2f lineStart = new Vector2f(transform.getTranslation().getX(), transform.getTranslation().getZ());
		Vector2f castDirection = new Vector2f(orientation.getX(), orientation.getZ());
		Vector2f lineEnd = lineStart.add(castDirection.mul(SHOOT_DISTANCE));
		
		Vector2f collisionVector = Game.getLevel().checkIntersections(lineStart, lineEnd);
		
		if (collisionVector == null)
		{
			System.out.println("We've missed everything!");
		}
		else {
			System.out.println("We've hit something!");
		}
		
		state = STATE_CHASE;
	}

	private void dyingUpdate(Vector3f orientation, float distance)
	{
		
	}

	private void deadUpdate(Vector3f orientation, float distance)
	{
		
	}

	private void alignWithGround()
	{
		transform.getTranslation().setY(OFFSET_FROM_GROUND);
	}
	
	private void faceCamera(Vector3f directionToCamera)
	{
		double x = (double)directionToCamera.getX();
		double z = (double)directionToCamera.getZ();
		
		double angle = -Math.toDegrees(Math.atan2(x, z));
		
		float angleToFaceTheCamera = (float)angle; 
		
		transform.getRotation().setY(angleToFaceTheCamera);
	}
	
	public void update()
	{
		Vector3f directionToCamera = transform.getTranslation().sub(Transform.getCamera().getPos());
		float distance = directionToCamera.length();
		//TODO, what if distance is 0?
		Vector3f orientation = directionToCamera.div(distance);

		alignWithGround();
		faceCamera(orientation);
		
		switch (state)
		{
		case STATE_IDLE:   idleUpdate(orientation, distance); break;
		case STATE_CHASE:  chaseUpdate(orientation, distance); break;
		case STATE_ATTACK: attackUpdate(orientation, distance); break;
		case STATE_DYING:  dyingUpdate(orientation, distance); break;
		case STATE_DEAD:   deadUpdate(orientation, distance); break;
		}
	}
	
	public void render()
	{
        Shader shader = Game.getLevel().getShader();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
	}
	
	
}
