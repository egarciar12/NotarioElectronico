package es.TFG.dao;

public interface NotarioElectronicoDAO {
	
	public void registroUsuario(String correo, String nombre, String apellido, String password);
	
	public boolean existeUsuario(String correo, String password);
	
	

}
