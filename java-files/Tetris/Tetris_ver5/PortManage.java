import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.UnsupportedOperationException;

public class PortManage {
    private String port;
    private String osName;

    public PortManage(int port) {

        this.port = Integer.toString(port);
        this.osName = System.getProperty("os.name").toLowerCase();
    }

    public void openPort(){

        try {
            String command = null;
            if (osName.contains("win")) command = "netsh advfirewall firewall add rule name=\"Open Port " + port + "\" dir=in action=allow protocol=TCP localport=" + port;
            if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac") ) command = "sudo iptables -A INPUT -p tcp --dport " + port + " -j ACCEPT";
            if (command == null) throw new UnsupportedOperationException("Unsupported op system");
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("[SERVER] " + port + " ポートを開きました");

        } catch (IOException | InterruptedException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    public void closePort(){

        try {
            String command = null;
            if (osName.contains("win")) command = "netsh advfirewall firewall delete rule name=\"Open Port " + port + "\"";
            if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac") ) command = "sudo iptables -A INPUT -p tcp --dport " + port + " -j DROP";
            if (command == null) throw new UnsupportedOperationException("Unsupported op system");
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("[SERVER] " + port + " ポートを閉じました");

        } catch (IOException | InterruptedException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }
}
