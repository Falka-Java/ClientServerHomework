import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private ServerSocket listener = null;
    private Socket acceptor = null;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private int port = 9001;
    private String message = "";

    public static void main(String[] args)   {
        Main server = new Main();
        server.listener();
    }


    private void sendMessage(String message){
        try{
            outputStream.writeObject(message);
            outputStream.flush();
        }catch (IOException err){
            System.out.println(err.getMessage());
        }
    }

    private static String getHoroscope(String zodiacName){
        try {
            File file = new File("C:\\Users\\vsh15\\Documents\\Java\\ClientServerHomework\\Server\\Server\\src\\data.txt");
            Scanner fileScanner = new Scanner(file);
            boolean isReading = false;
            String result = "";
            while(fileScanner.hasNextLine()){
                String data = fileScanner.nextLine();
                if(isReading){
                    if(data.contains("\"")){
                        result += data;
                        result = result.replace("\"","");
                        return result;
                    }
                    result += data;
                }else if(data.equals(zodiacName)){
                    isReading = true;
                }
            }
            return "Horoscope was not founded!";
        }catch (IOException ex){
            return "Error occurred while opening horoscopes database\n" +ex.getMessage();
        }
    }
    private void listener(){
        while(true){
            try {
                listener = new ServerSocket(port);
                System.out.println("Waiting for requests");
                acceptor = listener.accept();

                System.out.printf("Client connected! %s", acceptor.getInetAddress());
                inputStream = new ObjectInputStream(acceptor.getInputStream());
                outputStream = new ObjectOutputStream(acceptor.getOutputStream());
                outputStream.flush();

                do{
                    try {
                        Date d = new Date();
                        message = (String) inputStream.readObject();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        System.out.printf("%s -> %s\n", df.format(d), message);


                        sendMessage(getHoroscope(message));

                    }catch (ClassNotFoundException ex){
                        System.out.println(ex.getMessage());
                    }
                }while (!message.equals("exit"));

            }catch (IOException err){
                System.out.println(err.getMessage());
                break;
            }finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (outputStream != null)
                        inputStream.close();
                    if (listener != null)
                        inputStream.close();
                }catch (IOException err){
                    System.out.println(err.getMessage());
                    break;
                }
            }
        }
    }
}