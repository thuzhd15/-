package tankgameserver;
import java.net.*;
import java.io.*;
import java.util.*;
public class MyServer{
  public static ArrayList<Socket> socketservers = new ArrayList<Socket>();
  public static void main(String[] args) throws IOException{
    MyServer my = new MyServer();
    ServerSocket servSock = new ServerSocket(6789);
    public static int nClientNum = 0;
    while(true){
      while(nClientNum>0){
        Socket socketServer = servSock.accept();
        socketservers.add(socketServer);
        nClientNum ++;
        Mythread thread;
        thread = new Mythread(socketServer, nClientNum,b);
        thread.start();
      }
      /*while(nClientNum > 0){
        for(int j = 0;j<socketservers.size();j++){
          try{
            PrintStream outToClient = new PrintStream(socketservers.get(j).getOutputStream());
            outToClient.println();
          }catch(IOException e){}
        }
        try{
          Thread.sleep(500); 
        }catch(Exception e){
          e.printStackTrace();
        }
      }*/
    }
  }
}
class Mythread extends Thread{
  Socket socket;
  int num;
  Bang b;
  int score;
  LinkedList<String> list;
  public Mythread(Socket ss){
    socket = ss;
  }
  public void run(){
    try{
      String content;
      BufferedReader brInFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      Scanner inFromClient = new Scanner(brInFromClient);
      while(inFromClient.hasNextLine()){
        content = inFromClient.nextLine();
        Scanner subscanner = new Scanner(content);
        PrintStream ps = new PrintStream(socket.getOutputStream());
      }
      socket.close();
    }catch(IOException e){}
  }
}



    
    
