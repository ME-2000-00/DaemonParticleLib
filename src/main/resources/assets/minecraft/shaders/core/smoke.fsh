#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;

in vec2 texCoord0;

out vec4 fragColor;




void main() {
    vec2 uv = vec2(1.0) - texCoord0;
//    uv.y = 1.0 - uv.y;
//    uv.x = 1.0 - uv.x;

    vec4 color = texture(Sampler0, uv);
    if (color.a == 0.0) {
        discard;
    }
    // screen texture
    fragColor = color * ColorModulator;

    // depth texture
    // fragColor = vec4(vec3(color.r / 10), 1.0) * ColorModulator;
}
