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
