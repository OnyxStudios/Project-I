#version 150

in vec2 texCoord0;

out vec4 fragColor;

uniform sampler2D Sampler0;
uniform float Time;

const vec4 brightColor = vec4(0.75, 0.75, 0.75, 1);
const vec4 color = vec4(227 / 255, 198 / 255, 70 / 255, 1);

void main() {
    vec4 sum = vec4(0);
    float brightness = sin(Time / 12.0) * 0.5 + 0.5;

    float multFactor = 0.3;
    vec4 mixedColor = texture2D(Sampler0, texCoord0) * (color * 0.9);

    for (int i = -4; i < 4; i++) {
        for (int j = -3; j < 3; j++) {
            sum += texture2D(Sampler0, texCoord0 + vec2(j, i) * 0.004) * 0.25 * brightColor;
        }
    }

    if (mixedColor.r < 0.3) {
        fragColor = sum * sum * 0.012 + mixedColor;
    } else {
        if (mixedColor.r < 0.5) {
            fragColor = sum * sum * 0.009 * brightness + mixedColor;
        } else {
            fragColor = sum * sum * 0.0075 * brightness + mixedColor;
        }
    }
}