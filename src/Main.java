import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    newRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2000);
    }

private static void newRequest() throws IOException {
    URL helloServer = new URL("http://localhost:5050/jedd");
    URLConnection helloConnection = helloServer.openConnection();
    BufferedReader in = new BufferedReader(
            new InputStreamReader(
                    helloConnection.getInputStream()));
    String inputLine;

    while ((inputLine = in.readLine()) != null)
        System.out.println(inputLine);
    in.close();
    }
}

