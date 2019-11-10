import java.util.*;

class Move{
  public int[] from = new int[2];
  public int[] to = new int[2]; 
  public int[] middle = new int[2];
  public boolean valid = true;

  Move(int fi, int fj, int ti, int tj){
    from[0] = fi;
    from[1] = fj;
    to[0] = ti;
    to[1] = tj;
    middle[0] = (fi + ti) / 2;
    middle[1] = (fj + tj) / 2;
    if (fi==0 && fj==0 && ti==0 && tj==0){
    	valid = false;
    }
  }

  public void PrintMove(){
    System.out.println("From: " + from[0] + "," + from[1] + " To: " + to[0] + "," + to[1]);
  }
}

class GameState {
  public int[][] board = new int[7][7];

  public Vector ListMoveOptions(){
    Vector<Move> options = new Vector<>();
    for (int i=0;i<7;i++){
      for (int j=0;j<7;j++){    // iterate the whole board
        if (board[i][j] == 1){  // if we are on a peg
          if (i>1 && board[i-1][j] == 1 && board[i-2][j] == 0){
            options.add(new Move(i, j, i-2, j));
          }
          if (j<5 && board[i][j+1] == 1 && board[i][j+2] == 0){
            options.add(new Move(i, j, i, j+2));
          }
          if (i<5 && board[i+1][j] == 1 && board[i+2][j] == 0){
            options.add(new Move(i, j, i+2, j));
          }
          if (j>1 && board[i][j-1] == 1 && board[i][j-2] == 0){
            options.add(new Move(i, j, i, j-2));
          }
        }
      }
    }
    return options;
  }

  public GameState MakeMove(Move move){
    GameState newState = new GameState(this);
    newState.board[move.from[0]][move.from[1]] = 0;
    newState.board[move.to[0]][move.to[1]] = 1;
    newState.board[move.middle[0]][move.middle[1]] = 0;
    return newState;
  }

  public boolean IsGoal(){
    for (int i=0;i<7;i++){
      for (int j=0;j<7;j++){
        if (i == 3 && j == 3){
          if (board[i][j] != 1){
            return false;
          }
        } else if (board[i][j] == 1){
          return false;
        }
      }
    }
    return true;
  }

  public void PrintBoard(){
    for (int i=0;i<7;i++){
      for (int j=0;j<7;j++){
        if (board[i][j] == -1){
          System.out.print(String.format("%2s", " "));
        } else if (board[i][j] == 0){
          System.out.print(String.format("%2s", "•"));
        } else {
          System.out.print(String.format("%2s", "x"));
        }
      }
      System.out.print('\n');
    }
  }

  GameState(){
    for(int i=0;i<7;i++){
      for(int j=0;j<7;j++){
        board[i][j] = 1;
      }
    }
    for (int i=0;i<2;i++){
      for (int j=0;j<2;j++){
        board[i][j] = -1;
      }
    }
    for (int i=5;i<7;i++){
      for (int j=0;j<2;j++){
        board[i][j] = -1;
      }
    }
    for (int i=0;i<2;i++){
      for (int j=5;j<7;j++){
        board[i][j] = -1;
      }
    }
    for (int i=5;i<7;i++){
      for (int j=5;j<7;j++){
        board[i][j] = -1;
      }
    }
    board[3][3] = 0;
  }

  GameState(GameState gs){
    for (int i=0;i<7;i++){
      for (int j=0;j<7;j++){
        board[i][j] = gs.board[i][j];
      }
    }
  }

  public int Cost(){
    int cost_sum = 0;
    for (int i=0;i<7;i++){
      for (int j=0;j<7;j++){
        if (board[i][j] == 1){
          cost_sum += Math.abs(i-3) + Math.abs(j-3);
        }
      }
    }
    return cost_sum;
  }
}

class MainA_star {
  public static GameState current = new GameState();
  public static Vector<Move> move_trail = new Vector<>();
  public static boolean found = false;

  public static Move FindBest(GameState gs, int maxDepth){
  	int least_cost = 90;
  	Move least_move = new Move(0,0,0,0);
  	Vector<Move> options = gs.ListMoveOptions();
  	for (int i=0;i<options.size();i++){
  		int test_cost = DFS(gs.MakeMove(options.get(i)), maxDepth);
  		if (test_cost < least_cost){
  			least_cost = test_cost;
  			least_move = options.get(i);
  		}
  	}
  	return least_move;
  }

  public static int DFS(GameState gs, int maxDepth){
  	Vector<Move> options = gs.ListMoveOptions();
  	if (maxDepth > 1 && options.size() != 0){		// recursive case
		int ll_cost = 90;
  		for (int i=0;i<options.size();i++){
  			int test_cost = DFS(gs.MakeMove(options.get(i)), maxDepth-1);
  			if (test_cost < ll_cost){
  				ll_cost = test_cost;
  			}
  		}
  		return ll_cost;
  	} else {							// base case
  		int ll_cost = 90;
  		for (int i=0;i<options.size();i++){
  			int test_cost = gs.MakeMove(options.get(i)).Cost();
  			if (test_cost < ll_cost){
  				ll_cost = test_cost;
  			}
  		}
  		return ll_cost;
  	}
  }

  public static void PrintPath(){
  	System.out.println("finished with " + Integer.toString(move_trail.size()) 
      + " moves and goal found: " + Boolean.toString(found));

    GameState justadecoration = new GameState();
    for (int i=0;i<move_trail.size();i++){
      move_trail.get(i).PrintMove();
      justadecoration = justadecoration.MakeMove(move_trail.get(i));
      justadecoration.PrintBoard();
      System.out.println();
    }
    System.out.println("last state has a cost of: " + justadecoration.Cost());
  }

  public static void main(String[] args) {
    while (!found){
    	Move next_move = FindBest(current, 3);
    	if (!next_move.valid){
    		break;
    	}
    	move_trail.add(next_move);
    	current = current.MakeMove(next_move);
    }
    PrintPath();
  }
}




