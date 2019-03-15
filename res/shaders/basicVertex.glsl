#version 330

layout (location = 0) in vec3 position;

out vec4 color;

uniform mat4 transform;

void main(void)
{
	//gl_Position = vec4(position, 1.0);
	//color = vec3(position.x+0.5, 1.0, position.y+0.5);

	color = transform * vec4(clamp(position, 0.0, 1.0), 1.0);
	gl_Position = transform * vec4(position, 1.0);
}
