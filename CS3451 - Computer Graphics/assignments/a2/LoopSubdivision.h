#ifndef __LoopSubdivision_h__
#define __LoopSubdivision_h__
#include <unordered_map>
#include <vector>
#include "Mesh.h"

inline void LoopSubdivision(TriangleMesh<3>& mesh)
{
	std::vector<Vector3>& old_vtx=mesh.Vertices();
	std::vector<Vector3i>& old_tri=mesh.Elements();
	std::vector<Vector3> new_vtx;		////vertex array for the new mesh
	std::vector<Vector3i> new_tri;		////element array for the new mesh
	
	new_vtx=old_vtx;	////copy all the old vertices to the new_vtx array
	


	////step 1: add mid-point vertices and triangles
	////for each old triangle, 
	////add three new vertices (in the middle of each edge) to new_vtx 
	////add four new triangles to new_tri

	/*your implementation here*/
	std::unordered_map<Vector2i, int> edge_odd_vtx_map;
	std::unordered_map<Vector2i,Vector2i>edge_tri_map;
	std::unordered_map<int, std::vector<int>>vtx_vtx_map;


	for (int t = 0; t < old_tri.size(); ++t) {
		Vector3i& tri = old_tri[t];
		int mid_indices[3];

		for (int i = 0; i < 3; ++i) {
			int v = tri[(i + 2) % 3];

			/*Vector2i edge;
			if (i < 2) {
				edge(0) = tri[i];
				edge(1) = tri[i+1];
			}
			else {
				edge(0) = tri[2];
				edge(1) = tri[0];
			}*/

	
			Vector2i edge(tri[i], tri[(i + 1) % 3]);
			
			if (edge(0) > edge(1)) {
				Vector2i newEdge(edge(1), edge(0));
				edge = newEdge;
			}
			if (edge_odd_vtx_map.find(edge) == edge_odd_vtx_map.end()) {
				Vector3 mid_point = (new_vtx[edge(0)] + new_vtx[edge(1)]) / 2.0;
				new_vtx.push_back(mid_point);

				mid_indices[i] = static_cast<int>(new_vtx.size()) - 1;
				edge_odd_vtx_map[edge] = mid_indices[i];

				edge_tri_map[edge] = Vector2i(v, -1);
				

			}
			else {
				mid_indices[i] = edge_odd_vtx_map[edge];
				if (edge_tri_map[edge][1] == -1) { 
					edge_tri_map[edge][1] = v;
				}
			}
			//edge_tri_map[edge].push_back(t);
		}
		new_tri.push_back({ tri[0], mid_indices[0], mid_indices[2] });
		new_tri.push_back({ tri[1], mid_indices[1], mid_indices[0] });
		new_tri.push_back({ tri[2], mid_indices[2], mid_indices[1] });
		new_tri.push_back({ mid_indices[0], mid_indices[1], mid_indices[2] });
	}
	
	


	////step 2: update the position for each new mid-point vertex: 
	////for each mid-point vertex, find its two end-point vertices A and B, 
	////and find the two opposite-side vertices on the two incident triangles C and D,
	////then update the new position as .375*(A+B)+.125*(C+D)

	/*your implementation here*/
	
	for (auto& e : edge_odd_vtx_map) {
		const Vector2i& edge = e.first;
		int odd_idx = e.second;
		//int A = edge(0);
		//int B = edge(1);
		//int C, D;
		Vector3 A = new_vtx[edge[0]];
		Vector3 B = new_vtx[edge[1]];
		Vector3 C = new_vtx[edge_tri_map[edge][0]];
		Vector3 D;
		if (edge_tri_map[edge][1] != -1) {
			D = new_vtx[edge_tri_map[edge][1]];
		}
		else {
			D = Vector3(0, 0, 0);
		}
		/*
		const std::vector<int>& triangles = edge_tri_map[edge];
		int triangle_0_index = triangles[0];
		int triangle_1_index = triangles[1];

		for (int i = 0; i < 3; ++i) {
			if (old_tri[triangle_0_index](i) != A && old_tri[triangle_0_index](i) != B) {
				C = old_tri[triangle_0_index](i);
			}
		}

		for (int i = 0; i < 3; ++i) {
			if (old_tri[triangle_1_index](i) != A && old_tri[triangle_1_index](i) != B) {
				D = old_tri[triangle_1_index](i);
			}
		}

		Vector3 vertex_A = old_vtx[A];
		Vector3 vertex_B = old_vtx[B];
		Vector3 vertex_C = old_vtx[C];
		Vector3 vertex_D = old_vtx[D];*/

		//Vector3 new_position = 0.375 * (vertex_A + vertex_B) + 0.125 * (vertex_C + vertex_D);


		
		Vector3 new_position = 0.375 * (A + B) + 0.125 * (C + D);
		
		new_vtx[odd_idx] = new_position;
	}



	////step 3: update vertices of each old vertex
	////for each old vertex, find its incident old vertices, and update its position according its incident vertices

	/*your implementation here*/

	for (const auto& tri : old_tri) {
		for (int i = 0; i < 3; ++i) {
			vtx_vtx_map[tri[i]].push_back(tri[(i + 1) % 3]);
			vtx_vtx_map[tri[i]].push_back(tri[(i + 2) % 3]);
		}
	}

	for (auto& pair : vtx_vtx_map) {
		std::sort(pair.second.begin(), pair.second.end());
		pair.second.erase(std::unique(pair.second.begin(), pair.second.end()), pair.second.end());
	}

	std::vector<Vector3>smoothed_vtx_positions(old_vtx.size());
	for (int v = 0; v < old_vtx.size(); ++v) {

		const std::vector<int>& nbs = vtx_vtx_map[v]; 

		float beta;
		if (nbs.size() == 3) {
			beta = 3.0 / 16;
		}
		else {
			beta = 3.0 / (8 * nbs.size());
		}

		Vector3 sum_of_nbs(0, 0, 0);
		for (int nb : nbs) {
			sum_of_nbs += old_vtx[nb];
		}

		smoothed_vtx_positions[v] = (1 - nbs.size() * beta) * old_vtx[v] + beta * sum_of_nbs;
	}

	for (int v = 0; v < old_vtx.size(); ++v) {
		new_vtx[v] = smoothed_vtx_positions[v];
	}
	
	

	////update subdivided vertices and triangles onto the input mesh
	old_vtx=new_vtx;
	old_tri=new_tri;
}

#endif