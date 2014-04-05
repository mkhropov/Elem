#version 120

uniform sampler2D tex;
uniform int channel;

varying vec3 light;
varying vec2 uv;

void main(){
	vec4 color = texture2D(tex, uv);
	float i = (color.r+color.g+color.b)/6.;
        color = vec4(.4, .4, .4, .6);
        color += (channel-1)*(channel-2)*vec4(i/2, 0, 0, 0);
        color += (channel)*(channel-2)*vec4(0, -i/2, 0, 0);
        color += (channel)*(channel-1)*vec4(0, 0, i, 0);

        gl_FragColor = color;
}

