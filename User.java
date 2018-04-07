import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

class Writer extends Thread{
    BufferedReader con_br;
    PrintWriter sock_pw;

    public Writer(String name, PrintWriter sock_pw, BufferedReader con_br)
    {
        super(name);
        this.sock_pw = sock_pw;
        this.con_br = con_br;
    }

    public void run()
    {
        String s;
        try
        {
            while(true)
            {
                System.out.print("> ");
                s = con_br.readLine();
                if(s != null)
                    sock_pw.println(s);
                else
                    break;
            }
        }
        catch(Exception e)
        {System.err.println("Writer: Exception occured:\n" + e);}
    }
}

class User {
    public static int port = 13000;
    public static BufferedReader con_br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args)throws IOException
    {
        String[] carreras = {
            "Server",
            "client"
        };
        String respuesta = (String) JOptionPane.showInputDialog(null, "Seleccione una carrera a cursar", "Carrera", JOptionPane.DEFAULT_OPTION, null, carreras, carreras[0]);
        System.out.println("Starting " + respuesta);
        if (respuesta.equals("Server")){
            ServerSocket ssock = new ServerSocket(port);
            System.out.println("Server: Waiting for client to connect");
            Socket sock = ssock.accept();
            System.out.println("Server: Connection established");
            BufferedReader sock_br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter sock_pw = new PrintWriter(sock.getOutputStream(), true);

            Thread chat_Thread = new Writer("Server", sock_pw, con_br);
            chat_Thread.start();
            String s;
            while((s = sock_br.readLine()) != null)
            {
                System.out.println("\rServer: " + s);
                System.out.print("> ");
            }
            System.out.println("\rServer: Client has disconnected");
            sock.close();
            ssock.close();
        }else{
            System.out.print("Enter server address: ");
            String address = con_br.readLine();
            Socket sock = new Socket(address, port);
            System.out.println("Connection established");
            BufferedReader sock_br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter sock_pw = new PrintWriter(sock.getOutputStream(), true);

            Thread chat_Thread = new Writer("client", sock_pw, con_br);
            chat_Thread.start();
            String s;
            while((s = sock_br.readLine()) != null)
            {
                System.out.println("\rServer: " + s);
                System.out.print("> ");
            }
            sock.close();
        }
    }
}
