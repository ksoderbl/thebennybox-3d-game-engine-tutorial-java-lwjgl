#version 330

in vec2 texCoord0;

uniform vec3 baseColor;
uniform vec3 ambientLight;
uniform sampler2D sampler;

out vec4 fragColor;

void main()
{
    vec4 totalLight = vec4(ambientLight, 1);
    vec4 color = vec4(baseColor, 1);
    vec4 textureColor = texture2D(sampler, texCoord0.xy);

    if (textureColor != vec4(0,0,0,0)) {
        color *= textureColor;
    }

    fragColor = color * totalLight;
}
