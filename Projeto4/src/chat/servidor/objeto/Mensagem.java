package chat.servidor.objeto;

import java.io.Serializable;
import java.util.ArrayList;

public class Mensagem implements Serializable {

	private String remetente;
    private String destinatario;
    private String texto;
    private Action action;
    private ArrayList<String> usuariosOnline;
    private boolean isPlayer1 = true;
    private String nameThreadServer;

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public ArrayList<String> getUsuariosOnline() {
        return usuariosOnline;
    }

    public void setUsuariosOnline(ArrayList<String> usuariosOnline) {
        this.usuariosOnline = usuariosOnline;
    }

	public boolean isPlayer1() {
		return isPlayer1;
	}

	public void setPlayer1(boolean isPlayer1) {
		this.isPlayer1 = isPlayer1;
	}

	public String getNameThreadServer() {
		return nameThreadServer;
	}

	public void setNameThreadServer(String nameThreadServer) {
		this.nameThreadServer = nameThreadServer;
	}


	public enum Action {
        CONNECT, DISCONNECT, SEND, USERS_ONLINE, SEND_ONE, SEND_BROKER
    }
}
