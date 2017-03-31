# 8 Puzzles

### Summary:
**The problem.** The 8-puzzle problem is a puzzle invented and popularized by Noyes Palmer Chapman in the 1870s. It is played on a 3-by-3 grid with 8 square blocks labeled 1 through 8 and a blank square. Your goal is to rearrange the blocks so that they are in order. You are permitted to slide blocks horizontally or vertically into the blank square. The following shows a sequence of legal moves from an initial board position (left) to the goal position (right).
 
<p align = center>
<img src = ./pics/1.png>
</p>

**Best-first search.** We now describe an algorithmic solution to the problem that illustrates a general artificial intelligence methodology known as the A* search algorithm. We define a state of the game to be the board position, the number of moves made to reach the board position, and the previous state. First, insert the initial state (the initial board, 0 moves, and a null previous state) into a priority queue. Then, delete from the priority queue the state with the minimum priority, and insert onto the priority queue all neighboring states (those that can be reached in one move). Repeat this procedure until the state dequeued is the goal state. The success of this approach hinges on the choice of priority function for a state. We consider two priority functions:
+ __Hamming priority function.__ The number of blocks in the wrong position, plus the number of moves made so far to get to the state. Intutively, states with a small number of blocks in the wrong position are close to the goal state, and we prefer states that have been reached using a small number of moves.
+ __Manhattan priority function.__ The sum of the distances (sum of the vertical and horizontal distance) from the blocks to their goal positions, plus the number of moves made so far to get to the state.

For example, the Hamming and Manhattan priorities of the initial state below are 5 and 10, respectively.
<p align = center>
<img src = ./pics/2.png>
</p>

We make a key oberservation: to solve the puzzle from a given state on the priority queue, the total number of moves we need to make (including those already made) is at least its priority, using either the Hamming or Manhattan priority function. (For Hamming priority, this is true because each block out of place must move at least once to reach its goal position. For Manhattan priority, this is true because each block must move its Manhattan distance from its goal position. Note that we do not count the blank tile when computing the Hamming or Manhattan priorities.)

Consequently, as soon as we dequeue a state, we have not only discovered a sequence of moves from the initial board to the board associated with the state, but one that makes the fewest number of moves.


**A critical optimization.** After implementing best-first search, you will notice one annoying feature: states corresponding to the same board position are enqueued on the priority queue many times. To prevent unnecessary exploration of useless states, when considering the neighbors of a state, don't enqueue the neighbor if its board position is the same as the previous state.

<p align = center>
<img src = ./pics/3.png>
</p>


**Note**: The key component of constructing the priority queue is to record the status of every step since the beginning, we put every step associates with their moves into the priority queue and pick the minimum one. In order to do this, we create an inner class which contains current moves and a reference to the previous move. Thus, we will be able to calculate moves for each step and compare them.
