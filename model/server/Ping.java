package model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Ping implements Runnable {
	
	private InetAddress phone;
	private static final int TIMEOUT = 5000;
	public Ping(String ipAddress) {
		try {
			phone = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			phone = null;
		}
	}
	@Override
	public void run() {
		try {
			while(phone.isReachable(TIMEOUT))
				Thread.sleep(TIMEOUT);
		} catch (IOException |InterruptedException e) {
			
		}
		Platform.runLater(
				  () -> {
					  Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Connection lost");
						alert.setHeaderText("Connection lost");
						alert.setContentText("Click OK to close the program");
						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.OK){
						   System.exit(0);
						}
				  }
				);
	}

}
