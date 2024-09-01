package chat.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import broker.Produtor;
import chat.servidor.objeto.Mensagem;
import chat.servidor.objeto.Mensagem.Action;


public class ThreadServidor extends Thread{
	
	private static Map<String, Socket> clientsMap = new HashMap<>();
	private Socket socket;
	private Produtor produtor;
	
	public ThreadServidor(Socket s) {
		this.socket = s;
	}
	
	@Override
	public void run() {
		
		boolean sair = false;
		
		while (!sair) {
		
			try {
				ObjectInputStream input =  new ObjectInputStream(socket.getInputStream());
				Mensagem mensagem = (Mensagem) input.readObject();
				Action action = mensagem.getAction();
				
				switch (action) {
		            case CONNECT:
		                conectar(mensagem);
		                enviarMensagemTodos(mensagem);
		                enviarUsuariosOnline();
		                break;
		            case DISCONNECT:
		            	desconectar(mensagem);
		                enviarMensagemTodos(mensagem);
		                enviarUsuariosOnline();
		                sair = true;
		                break;
		            case SEND:
		                enviarMensagemTodos(mensagem);
		                break;
		            case SEND_ONE:
		            	enviarMensagemReservada(mensagem);
		            	break;
		            case SEND_BROKER:
		            	enviarMensagemProBroker(mensagem);
		            	break;
		            default:
		                break;
				}
				
				
			} catch (IOException | ClassNotFoundException | JMSException ex) {
				ex.printStackTrace();
				//Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
	}
	
	public void conectar(Mensagem mensagem) {
        clientsMap.put(mensagem.getRemetente(), socket);
    }

	 public void desconectar(Mensagem mensagem) throws IOException {
        clientsMap.remove(mensagem.getRemetente());
    }

    public void enviarMensagemTodos(Mensagem mensagem) throws IOException {

        for (Map.Entry<String, Socket> cliente : clientsMap.entrySet()) {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getValue().getOutputStream());
            mensagem.setNameThreadServer(this.getName());
            saida.writeObject(mensagem);
            
        }
    }

    
    public void enviarUsuariosOnline() throws IOException {
        ArrayList<String> usuariosOnline = new ArrayList();
        
        for (Map.Entry<String, Socket> cliente : clientsMap.entrySet()) {
            usuariosOnline.add(cliente.getKey());
        }
        
        Mensagem mensagem = new Mensagem();
        mensagem.setAction(Action.USERS_ONLINE);
        mensagem.setUsuariosOnline(usuariosOnline);
        
        for (Map.Entry<String, Socket> cliente : clientsMap.entrySet()) {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getValue().getOutputStream());
            saida.writeObject(mensagem);
        }
        
    }
    
    public void enviarMensagemReservada(Mensagem mensagem) throws IOException {
        for (Map.Entry<String, Socket> cliente : clientsMap.entrySet()) {
            if (mensagem.getDestinatario().equals(cliente.getKey())) {
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getValue().getOutputStream());
                saida.writeObject(mensagem);
            }
        }
    }
    
    public void enviarMensagemProBroker(Mensagem mensagem) throws JMSException {
    	this.produtor = new Produtor(mensagem.getDestinatario());
    	this.produtor.sendMessage(mensagem.getTexto(), mensagem.getRemetente());
    }
    

}
