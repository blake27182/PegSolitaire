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

  public boolean GreaterThan(GameState gs){
    int my_val = HeuristF();
    int their_val = gs.HeuristF();

    return (my_val > their_val);
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

  public int NumIsolated(){
    int num_isolated = 0;
    for (int i=0;i<7;i++){
      for (int j=0;j<7;j++){
        boolean check_l = false;
        boolean check_r = false;
        boolean check_u = false;
        boolean check_d = false;
        if (board[i][j] == 1){
          check_u = (i==0 || board[i-1][j] != 1);
          check_d = (i==6 || board[i+1][j] != 1);
          check_l = (j==0 || board[i][j-1] != 1);
          check_r = (j==6 || board[i][j+1] != 1);
        }
        if (check_l && check_r && check_u && check_d){
          num_isolated++;
        }
      }
    }
    return num_isolated;
  }

  public int HeuristF(){
    int pegs_gone = 0;
    for (int i=0;i<7;i++){
      for (int j=0;j<7;j++){
        if (board[i][j] == 0){
          pegs_gone++;
        }
      }
    }
    return Cost() + pegs_gone + NumIsolated();
  }

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
}

class Main {
  public static GameState current = new GameState();
  public static Vector<Move> move_trail = new Vector<>();
  public static Vector<GameState> state_trail = new Vector<>();
  public static boolean found = false;

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

  public static void SortHelper(int low, int high, ArrayList<GameState> list){
    int med = (high + low) / 2;
    if (high - low > 1){
      SortHelper(low, med, list);
      SortHelper(med+1, high, list);
    }

    ArrayList<GameState> templist = new ArrayList<>();
    int i = low;
    int j = med+1;

    // sort big to small
    while(i<=med || j<=high){
      if (i > med){                     // use j
        templist.add(list.get(j++));
      } else if (j > high){             // use i
        templist.add(list.get(i++));
      } else {
        if (list.get(i).GreaterThan(list.get(j))){
          templist.add(list.get(i++));
        } else {
          templist.add(list.get(j++));
        }
      }
    }

    // put the values back in list
    for (int l=0;l<templist.size();l++){
      list.set(l+low, templist.get(l));
    }
  }

  public static void SortStack(Stack st){
    // sort the stack using f(n) = g(n) + h(n)
    // g(n) = how many pegs are missing
    // h(n) = ∑ peg * distance to center

    // copy to templist
    ArrayList<GameState> templist = new ArrayList<>();
    while (!st.empty()){
      templist.add((GameState)st.pop());
    }

    // sort templist
    SortHelper(0, templist.size()-1, templist);

    // copy it back to the stack
    for (int i=0;i<templist.size();i++){
      st.push(templist.get(i));
    }
  }

  public static void PrintStack(Stack<GameState> st){
    for (int i=st.size()-1;i>=0;i--){
      System.out.println("h_cost: " + Integer.toString(st.get(i).HeuristF()));
      System.out.println("isolated: " + Integer.toString(st.get(i).NumIsolated()));
      st.get(i).PrintBoard();
      System.out.println();
    }
  }

  public static void main(String[] args) {
    Stack<GameState> stack = new Stack<>();
    Dictionary map = new Hashtable();
    Vector<Move> options = new Vector<>();
    stack.push(current);


    // depth first *style* search
    while (!stack.empty()){
    	current = (GameState)stack.pop();
      // current.PrintBoard();
      // System.out.println();
      if (current.IsGoal()){
        break;
      }

      // push all the available nodes to the stack in no particular order
      options = current.ListMoveOptions();
      for (int i=0;i<options.size();i++){
        GameState child = current.MakeMove(options.get(i));
        stack.push(child);
        map.put(child, current);
      }
      SortStack(stack);
    }
    System.out.println("finished stack");



    GameState next = current;
    while (next != null){
      state_trail.add(next);
      next = (GameState)map.get(next);
    }

    for (int i=state_trail.size();i>=0;i--){
      state_trail.get(i).PrintBoard();
      System.out.println();
    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ testing area vvv
    // System.out.println(options);
    // current.PrintBoard();
    // options = current.ListMoveOptions();
    // System.out.println(options);

    // for (int i=0;i<10;i++){
    //   current = stack.pop();
    //   options = current.ListMoveOptions();
    //   for (int j=0;j<options.size();j++){
    //     stack.push(current.MakeMove(options.get(j)));
    //   }
    // }

    // System.out.println("before sort: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    // PrintStack(stack);

    // SortStack(stack);
    // System.out.println("after sort: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    // PrintStack(stack);

  }
}


