# Seam Carver

### Summary:
Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. A vertical seam in an image is a path of pixels connected from the top to the bottom with one pixel in each row. (A horizontal seam is a path of pixels connected from the left to the right with one pixel in each column.) Below left is the original 505-by-287 pixel image; below right is the result after removing 150 vertical seams, resulting in a 30% narrower image. Unlike standard content-agnostic resizing techniques (e.g. cropping and scaling), the most interesting features (aspect ratio, set of objects present, etc.) of the image are preserved.See picture below:

![](http://coursera.cs.princeton.edu/algs4/assignments/HJoceanSmall.png) ![](http://coursera.cs.princeton.edu/algs4/assignments/HJoceanSmallShrunk.png)

&nbsp;

**Finding and removing a seam involves three parts and a tiny bit of notation:**

**_Notation_**. In image processing, pixel (x, y) refers to the pixel in column x and row y, with pixel (0, 0) at the upper left corner and pixel (W − 1, H − 1) at the bottom right corner.  Warning: this is the opposite of the standard mathematical notation used in linear algebra where (i, j) refers to row i and column j and with Cartesian coordinates where (0, 0) is at the lower left corner.

1. **Energy calculation**. The first step is to calculate the energy of each pixel, which is a measure of the importance of each pixel—the higher the energy, the less likely that the pixel will be included as part of a seam (as we'll see in the next step). In this assignment, you will implement the dual-gradient energy function, which is described below. Here is the dual-gradient energy function of the surfing image above:
<p align = center>
<img src = http://coursera.cs.princeton.edu/algs4/assignments/HJoceanSmallEnergy.png>
</p>
The energy is high (white) for pixels in the image where there is a rapid color gradient (such as the boundary between the sea and sky and the boundary between the surfing Josh Hug on the left and the ocean behind him). The seam-carving technique avoids removing such high-energy pixels.

2. **Seam identification**. The next step is to find a vertical seam of minimum total energy. This is similar to the classic shortest path problem in an edge-weighted digraph except for the following:

+ The weights are on the vertices instead of the edges.
+ We want to find the shortest path from any of the W pixels in the top row to any of the W pixels in the bottom row.
+ The digraph is acyclic, where there is a downward edge from pixel (x, y) to pixels (x − 1, y + 1), (x, y + 1), and (x + 1, y 1), assuming that the coordinates are in the prescribed range.
<p align = center>
<img src=http://coursera.cs.princeton.edu/algs4/assignments/HJoceanSmallVerticalSeam.png>
</p>
3. **Seam removal**. The final step is to remove from the image all of the pixels along the seam.


&nbsp;
**Note** : The key to solve the shortest path problem in a DAG(directed arcylic graph) is to use topological order of each node and calculate the shortest path by relaxing each edge according topological order, the pseudocode and example is below:
<p align = center>
<img src=http://d1hyf4ir1gqw6c.cloudfront.net//wp-content/uploads/TopologicalSort.png>
</p>
``` 
1) Initialize dist[] = {INF, INF, ….} and dist[s] = 0 where s is the source vertex.
2) Create a toplogical order of all vertices.
3) Do following for every vertex u in topological order.
………..Do following for every adjacent vertex v of u
………………if (dist[v] > dist[u] + weight(u, v))
………………………dist[v] = dist[u] + weight(u, v)
```




[Reference of Topological Algorithm](http://www.geeksforgeeks.org/shortest-path-for-directed-acyclic-graphs/)

[Reference of this project](http://www.cs.princeton.edu/courses/archive/spring13/cos226/assignments/seamCarving.html)
