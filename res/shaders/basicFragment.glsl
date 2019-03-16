#version 330

in vec2 texCoord0;

uniform vec3 color;
uniform sampler2D sampler;

out vec4 fragColor;

void main()
{
    vec4 textureColor = texture2D(sampler, texCoord0.xy);

    // doesn't work: if (textureColor == 0)
    if (textureColor == vec4(0,0,0,0)) {
        fragColor = vec4(color, 1);
    }
    else {
        fragColor = textureColor * vec4(color, 1);
    }
}
