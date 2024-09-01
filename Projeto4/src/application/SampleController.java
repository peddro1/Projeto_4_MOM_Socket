package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.jms.JMSException;

import broker.Consumidor;
import chat.servidor.objeto.Mensagem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SampleController {

    @FXML
    private AnchorPane anchor;
    
    @FXML
    private Button addUser;

    @FXML
    private ListView<String> contatos;
    
	@FXML
	private TextArea chat;
	
	@FXML
	private ListView<String> listUsers;
	
	@FXML
	private TextField message;
	
	@FXML
	private Button saveUser;
	
	@FXML
	private Button sendButton;
	
	@FXML
	private Button statusButton;
	
	@FXML
	private TextField username;
	
    @FXML
    private Button removeUser;
	
    private Stage dialogStage;
    
    private Socket socket;
    private String remetente;
    
    private Consumidor consumidor;
	
	@FXML
	void saveUser(ActionEvent event) {
		 this.statusButton.setDisable(false);
	     this.remetente = username.getText();

	        try {
	            //Conectando ao Servidor do Chat
	            socket = new Socket("127.0.0.1", 54321);
	            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());

	            //Enviando a primeira mensagem informando conexão (apenas para passar o nome do cliente)
	            Mensagem mensagem = new Mensagem();
	            mensagem.setRemetente(remetente);
	            mensagem.setTexto("Entrou");
	            mensagem.setAction(Mensagem.Action.CONNECT);

	            //Instanciando uma ThreadCliente para ficar recebendo mensagens do Servidor
	            ThreadClient thread = new ThreadClient(remetente, socket, chat, listUsers, statusButton);
	            thread.setName("Thread Cliente " + remetente);
	            thread.start();

	            //Saída de Dados do Cliente
	            saida.writeObject(mensagem); //Enviando mensagem para Servidor

	        } catch (IOException ex) {
	            //Logger.getLogger(FXMLChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
	        }
		
	}
	
    @FXML
    void openChat(ActionEvent event) {
    }
	
	@FXML
	void sendMessage(ActionEvent event) {
		 try {
			 if (contatos.getSelectionModel().getSelectedItem() != null) {
		            Mensagem mensagem = new Mensagem();
		            mensagem.setRemetente(this.remetente);
		            mensagem.setTexto(this.message.getText());
	
		            //Caso tenha selecionado algum usuário
		            
	                mensagem.setAction(Mensagem.Action.SEND_ONE);
	                mensagem.setDestinatario((String)contatos.getSelectionModel().getSelectedItem());
	           
	
		            //Saída de Dados do Cliente
		            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
		            saida.writeObject(mensagem); //Enviando mensagem para Servidor
	
		            this.chat.appendText(mensagem.getRemetente() + " >> " + message.getText() + "\n");
		            this.message.setText("");
		            //this.contatos.getSelectionModel().clearSelection();
		            this.listUsers.getSelectionModel().clearSelection();
			 	}

	        } catch (IOException ex) {
	            //Logger.getLogger(FXMLChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
	        }
	}
	
	@FXML
	void toggleStatus(ActionEvent event) throws JMSException {
		if(this.statusButton.getText().equals("ONLINE")) {
			this.statusButton.setText("OFFLINE");
			this.message.setDisable(true);
			this.sendButton.setDisable(true);
		}
		else {
			this.statusButton.setText("ONLINE");
			this.consumidor = new Consumidor(this.remetente, this.chat);
			this.message.setDisable(false);
			this.sendButton.setDisable(false);
		}
			
	}
	
    @FXML
    void addUser(ActionEvent event) {
    	if (listUsers.getSelectionModel().getSelectedItem() != null) {
    		contatos.getItems().add((String)listUsers.getSelectionModel().getSelectedItem());  
        }
    }
    
    @FXML
    void removeUser(ActionEvent event) {
    	if (contatos.getSelectionModel().getSelectedItem() != null) {
    		contatos.getItems().remove((String)contatos.getSelectionModel().getSelectedItem());  
        }
    }
}
