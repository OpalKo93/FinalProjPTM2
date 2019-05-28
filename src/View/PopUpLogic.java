package View;

import java.util.Observable;
import java.util.Observer;

import ViewModel.ViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PopUpLogic implements Observer {

	ViewModel vm;
	@FXML
	TextField varIP,varPort;
	
	public void setViewModel(ViewModel vm) {
		this.vm=vm;
		vm.ip.bind(varIP.textProperty());
		vm.port.bind(varPort.textProperty());
	}
	public void connect() {
		vm.connect();

	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}
