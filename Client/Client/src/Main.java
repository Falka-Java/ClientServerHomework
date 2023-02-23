import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private Socket client = null;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Scanner scanner;
    private int port =9001;
    private String message = "";

    public static void main(String[] args){
        Main client = new Main();
        client.setConnection();


    }
    private void setConnection() {
        try{
            client = new Socket("127.0.0.1", port);
            System.out.println("Connection established");
            scanner = new Scanner(System.in);

            outputStream = new ObjectOutputStream(client.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(client.getInputStream());


            do{

                System.out.print("Enter your horoscope: ");
                message = scanner.nextLine();
                if(message!=null){
                    sendMessage(message);
                }

                if(!message.equals("exit_message")){
                    try{
                        message = (String)inputStream.readObject();
                        System.out.printf("Server > %s\n", message);
                    }catch (ClassNotFoundException ex){
                        System.out.println(ex.getMessage());
                    }
                }
            }while (!message.equals("exit_message"));
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    inputStream.close();
                if (client != null)
                    inputStream.close();
            }catch (IOException err){
                System.out.println(err.getMessage());
            }
        }
    }

    private void sendMessage(String message){
        try{
            outputStream.writeObject(message);
            outputStream.flush();
        }catch (IOException err){
            System.out.println(err.getMessage());
        }
    }
}