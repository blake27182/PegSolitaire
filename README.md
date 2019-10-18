# PegSolitaire
Peg Solitaire is a single player game that is more like a puzzle than a game. The environment is 
completely accessable, it is static, deterministic, descrete and sequential. So it's a great problem 
for a beginner (me) to solve using several different methods.

As far as NP-complete searches, depth first is best for this because all the goal states are guaranteed 
to be in the leaves of the tree. So why waste time searching the middle? In my own DFS, the first goal
state was found in about 7.5 million visits, meaning it tried 7.5 million different moves before solving the
puzzle. The path that it returned was 31 moves long (moves are highly constrained though so actually
this number won't ever change).

My next venture was to use a really basic heuristic algorithm that calculates the cost of each node by 
the sum of all the peg's "Manhattan distances" from the center. (Picture walking blocks instead of 
floating diagonally to your destination). This should incentivise the search to keep the pegs in the middle 
of the board and hopefully by the end, have the last one in the middle (which is how you solve the puzzle).

After running this a few times, (and a lot of debugging) it's easy to notice that we end up in dead ends a 
lot. It leaves some pegs behind and isolated so they can't move. But in this search method, it's impossible 
to back up. (Much like in real life)

Ideally, we only want to make "correct" moves so we never NEED to back up and check other branches. 
The difficulty here is that even as humans, we think ahead. When you play checkers, you don't think 
"if I move here, I'll be closer to the other side so I'll just do that" you might think something like "if I move here, 
they're clearly going to jump me so I'm not gonna do that". This is kind of like a miniature depth first search 
that allows us to teleport around bad decisions that seem fine in the moment.

After implimenting this short-distance look-ahead feature, the heuristic seems to do worse in most cases.
At each move, instead of checking the best cost out of the moves available, it checks the best cost out of 
the leaves of a short DFS on a given depth. Kind of like thinking  a couple steps ahead at each move during
a game of checkers. I tested a few different depths on this, and the best results are at 3 nodes deep. This is 
surprising, because I would've thought it would only get more accurate as I increase the checking depth.

I'm now trying to impliment an admissable heuristic for an A* search. Unfortunately it's still running, so I think 
my heuristic could use some work. The reason it's taking so long is because of nested loops. In the raw DFS, 
There was only 2 tasks per node expansion. (checking to see if it was the goal, and loading children into the 
stack). Now with A*, each node expansion has to load the children, and sort the entire stack according to the
f(n) = g(n) + h(n) formula, which means checking the cost of each node and checking the number of moves 
made to get to that node. Each of those are 2 nested for-loops iterating over the board. The sorting itself is 
a simple merge-sort, which takes O(nlogn) time.
    Technically, the cost comparison doesn't count as part of the overall time-complexity because it is constant
for every node, but I would have to be really naive to say it doesnt make the darn thing take longer.
