import java.util.*;

class Move{
  public int[] from = new int[2];
  public int[] to = new int[2]; 
  public int[] middle = new int[2];

  Move(int fi, int fj, int ti, int tj){
    from[0] = fi;
    from[1] = fj;
    to[0] = ti;
    to[1] = tj;
    middle[0] = (fi + ti) / 2;
    middle[1] = (fj + tj) / 2;
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

class Main {
  public static GameState current = new GameState();
  public static GameState option_state;
  public static Move least_move;
  public static int least_cost;
  public static int option_cost;
  public static Vector<Move> options = new Vector<>();
  public static Vector<Move> move_trail = new Vector<>();
  public static boolean found = false;

  public static void main(String[] args) {

    while (!found){
      options = current.ListMoveOptions();
      least_cost = 90;
      if (options.size() == 0){
        break;
      }
      for (int i=0;i<options.size();i++){       // find the least cost option
        option_state = current.MakeMove(options.get(i));
        option_cost = option_state.Cost();
        // System.out.println("considering move:")
        // options.get(i).PrintMove();
        // System.out.println("this has a cost of: " + Integer.toString(option_cost) + '\n');
        if (option_cost < least_cost){
          least_cost = option_cost;
          least_move = options.get(i);
        }
      } // found least cost option
      move_trail.add(least_move);
      current = current.MakeMove(least_move);
      if (current.IsGoal()){
        found = true;
      }
    }// finished finding path

    System.out.println("finished with " + Integer.toString(move_trail.size()) 
      + " moves and goal found: " + Boolean.toString(found));

    GameState justadecoration = new GameState();
    for (int i=0;i<move_trail.size();i++){
      move_trail.get(i).PrintMove();
      justadecoration = justadecoration.MakeMove(move_trail.get(i));
      justadecoration.PrintBoard();
      System.out.println();
    }

  }
}




