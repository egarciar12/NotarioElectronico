package model;

public class Usuario {
	
	private String correo;
	private String nombre;
	private String apellidos;
	private String password;
	
	public Usuario(String correo, String nombre, String apellidos,
			String password) {
		super();
		this.correo = correo;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.password = password;
	}
	
	
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	

}
