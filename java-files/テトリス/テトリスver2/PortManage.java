import java.io.IOException;

public class PortManage {
    private String port;

    public PortManage(int port) {

        this.port = Integer.toString(port);
    }

    public void openPort(){

        try {
            String command = "netsh advfirewall firewall add rule name=\"Open Port " + port + "\" dir=in action=allow protocol=TCP localport=" + port;
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

            System.out.println(port + " ポートを開きました");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closePort(){

        try {
            String command = "netsh advfirewall firewall delete rule name=\"Open Port " + port + "\"";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

            System.out.println(port + " ポートを閉じました");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
