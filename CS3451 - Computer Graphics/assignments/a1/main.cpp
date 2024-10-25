//#####################################################################
// Main
// CS3451 Computer Graphics Starter Code
// Contact: Bo Zhu (bo.zhu@gatech.edu)
//#####################################################################
#include <iostream>
#include <random>

#include "OpenGLMesh.h"
#include "OpenGLCommon.h"
#include "OpenGLWindow.h"
#include "OpenGLViewer.h"

/////////////////////////////////////////////////////////////////////
//// TODO: put your name in the string               
/////////////////////////////////////////////////////////////////////

const std::string author="Waiyuk Kwong";

/////////////////////////////////////////////////////////////////////
//// These are helper functions we created to generate circles and triangles by testing whether a point is inside the shape or not.
//// They can be used in the paintGrid function as "if the pixel is inside, draw some color; else skip."
//// You may create your own functions to draw your own shapes

//// The paintGrid function is implemented as a GLSL fragment shader. 
//// The GLSL grammar is C-style, and if you are curious about its full picture (which we will start to learn the details in Week 3), 
//// you may find more information on https://www.khronos.org/files/opengl43-quick-reference-card.pdf (Page 6 - 7 would probably be helpful!)
//// You don't need advanced GLSL features for this assignment (reading the starter code should be enough).
//// You can also test it (copy the whole string) in Shadertoy: https://www.shadertoy.com/new    
/////////////////////////////////////////////////////////////////////

const std::string draw_pixels=To_String(
const float M_PI=3.1415926535; 

// The side length of the minimum unit (or the new "pixels")
const float PIXEL_SIZE=1.; 

// To check if a point is inside a circle
bool inCircle(vec2 p, vec2 center, float radius) {
	vec2 to_center=p - center;
	if (dot(to_center, to_center) < radius * radius) {
		return true;
	}
	return false;
}

// To check if a point is inside a triangle
bool inTriangle(vec2 p, vec2 p1, vec2 p2, vec2 p3) {
	if (dot(cross(vec3(p2 - p1, 0), vec3(p - p1, 0)), cross(vec3(p2 - p1, 0), vec3(p3 - p1, 0))) >= 0. &&
		dot(cross(vec3(p3 - p2, 0), vec3(p - p2, 0)), cross(vec3(p3 - p2, 0), vec3(p1 - p2, 0))) >= 0. &&
		dot(cross(vec3(p1 - p3, 0), vec3(p - p3, 0)), cross(vec3(p1 - p3, 0), vec3(p2 - p3, 0))) >= 0.) {
		return true;
	}
	return false;
}

// To convert from Polar Coordinates to Cartesian coordinates
vec2 polar2cart(float angle, float length) {
	return vec2(cos(angle) * length, sin(angle) * length);
}

/////////////////////////////////////////////////////////////////////////
// Feel free to add more functions if needed!                          
/////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
// TODO: replace the code below with your own code                 //
// Useful variables:											   //
// iTime: the passed seconds from the start of the program         //
// iResolution: the size of the window (default: 1280*960)         //
/////////////////////////////////////////////////////////////////////

// Return the rgba color of the grid at position (x, y) 
vec4 paintGrid(float x, float y) {

	vec2 p = vec2(x, y) * PIXEL_SIZE;

	vec2 center = vec2(iResolution / PIXEL_SIZE / 2.); // window center
	float circle_radius = 200.; // The radius of the circle = 20


	float distance = length(p - vec2(iResolution / 2.));//from current pixel to the center of screen
	float max_distance = length(vec2(iResolution / 2.));
	float gradient = distance / max_distance;


	float gradient2 = pow(gradient, 0.5);



	vec2 p1 = center + vec2(2*120,3.8* 85);
	vec2 p2 = p1 + 0.3*vec2(50, 0.0);
	vec2 p3 = p1 + 0.3*vec2(3.5*50, -2*50);

	vec2 p4 = p3;
	vec2 p5 = p1 + 0.6*vec2(3.5 * 50, 0.0);
	vec2 p6 = p5 - 0.3 * vec2(50, 0.0);


	vec2 p7 = center + vec2(3.2 * 100, 3 * 100);
	vec2 p8 = p7 + 0.3 * vec2(45, 0.0);
	vec2 p9 = p7 + 0.3 * vec2(3.5 * 45, -2 * 45);

	vec2 p10 = p9;
	vec2 p11 = p7 + 0.6 * vec2(3.5 * 45, 0.0);
	vec2 p12 = p11 - 0.3 * vec2(45, 0.0);
	



	vec4 light_blue = vec4(vec3(184, 243, 255) / 255., 1.);//light blue
	vec3 blue = vec3(173.0 / 255.0, 216.0 / 255.0, 230.0 / 255.0);
	vec3 orange = vec3(1.0, 0.5, 0.0); 
	vec3 yellow = vec3(1.0, 1.0, 0.0); 

	vec3 color1 = mix(orange, yellow, gradient);

	vec3 color2 = mix(yellow, blue, gradient2);




	bool inCircle1 = inCircle(vec2(x, y), center, circle_radius);

	bool inTrangle1 = inTriangle(vec2(x, y), p1, p2, p3);
	bool inTrangle2=inTriangle(vec2(x, y), p4, p5, p6);

	bool inTrangle3 = inTriangle(vec2(x, y), p7, p8, p9);
	bool inTrangle4 = inTriangle(vec2(x, y), p10, p11, p12);
	

	if (inTrangle1 || inTrangle2 || inTrangle3 || inTrangle4) {
		return vec4(0.0, 0.0, 0.0, 1.0);
	}



	else if (p.y < iResolution.y / 2.) {
		//return light_blue;
		return vec4(color2, 1.0);
	}
	else if (inCircle1) {
		return vec4(1.0, 0.0, 0.0, 1.0); // RGBA for red color
	}
	else {
		return vec4(color1, 1.0);
	}
	/*
	else if (inTrangle1 && inTrangle2) {
		return vec4(1.0);
	}
	else if (inTrangle1 || inTrangle2) {
		return vec4(vec3(217, 249, 255) / 255., 1.); 
	}*/

}

// The function called in the fragment shader
void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
	// To divide the screen into the grids for painting!
	fragColor=paintGrid(floor(fragCoord.x / PIXEL_SIZE), floor(fragCoord.y / PIXEL_SIZE));
}

);

class ScreenDriver : public OpenGLViewer
{
	OpenGLScreenCover* screen_cover=nullptr;
	clock_t startTime=clock();

public:
	virtual void Initialize()
	{
		OpenGLViewer::Initialize();
	}

	//// Initialize the screen covering mesh and shaders
	virtual void Initialize_Data()
	{
		OpenGLShaderLibrary::Instance()->Create_Screen_Shader(draw_pixels, "shaderToy");
		screen_cover=Add_Interactive_Object<OpenGLScreenCover>();
		Set_Polygon_Mode(screen_cover, PolygonMode::Fill);
		Uniform_Update();

		screen_cover->Set_Data_Refreshed();
		screen_cover->Initialize();
		screen_cover->Add_Shader_Program(OpenGLShaderLibrary::Get_Shader("shaderToy"));
	}

	//// Update the uniformed variables used in shader
	void Uniform_Update()
	{
		screen_cover->setResolution((float)Win_Width(), (float)Win_Height());
		screen_cover->setTime(GLfloat(clock() - startTime) / CLOCKS_PER_SEC);
	}

	//// Go to next frame 
	virtual void Toggle_Next_Frame()
	{
		Uniform_Update();
		OpenGLViewer::Toggle_Next_Frame();
	}

	////Keyboard interaction
	virtual void Initialize_Common_Callback_Keys()
	{
		OpenGLViewer::Initialize_Common_Callback_Keys();
	}

	virtual void Run()
	{
		OpenGLViewer::Run();
	}
};

int main(int argc,char* argv[])
{
	if(author==""){std::cerr<<"***** The author name is not specified. Please put your name in the author string first. *****"<<std::endl;return 0;}
	else std::cout<<"Assignment 1 demo by "<<author<<" started"<<std::endl;
	
	ScreenDriver driver;
	driver.Initialize();
	driver.Run();	
}
