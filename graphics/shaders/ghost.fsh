#version 120

uniform sampler2D tex;
uniform vec4 hue;

varying vec3 light;
varying vec2 uv;

void main(){
	vec4 color = texture2D(tex, uv);

        gl_FragColor = vec4(vec3(.4, .4, .4)+hue.rgb*(color.r+color.g+color.b)/6., hue.a);
}

