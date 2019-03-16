#version 330

//in vec4 color;

in vec2 texCoord0;

uniform sampler2D sampler;

out vec4 fragColor;

void main()
{
	fragColor = texture2D(sampler, texCoord0.xy);
}
