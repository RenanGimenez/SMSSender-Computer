package model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class CheckTimeout {

	
	
	public static void main() {
		 InetAddress phone;
		 final int TIMEOUT = 5*1000 + 0; // 5s timeout
		
			
		
		try {
			phone = InetAddress.getByName("192.168.1.9");
			while(phone.isReachable(TIMEOUT))
				Thread.sleep(TIMEOUT);
		} catch (IOException |InterruptedException e) {
			Platform.runLater(
					  () -> {
						  Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Connection lost");
							alert.setHeaderText("Connection lost");
							alert.setContentText("Click OK to close the program");
							alert.showAndWait();
							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() == ButtonType.OK){
							   System.exit(0);
							}
					  }
					);
		}

	}

}
