package es.TFG.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;



public class NotarioElectronicoDAOImpl implements NotarioElectronicoDAO{
		
	private static NotarioElectronicoDAOImpl instance;
	
	private NotarioElectronicoDAOImpl(){
		
	}
	
	public static NotarioElectronicoDAOImpl getInstance(){
		if(instance==null){
			instance = new NotarioElectronicoDAOImpl();
		}
		return instance;
	}

	@Override
	public void registroUsuario(String correo, String nombre, String apellido,
			String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existeUsuario(String correo, String password) {
		
	    try {
           
            
            Connection conn;
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+"localhost"+"notarioelectronico", "root", "");

            Statement estado = conn.createStatement();
            System.out.println("Conexion establecida");
            String peticion ="select * from usuario where usuario='"+correo+"'";
            ResultSet result = estado.executeQuery(peticion);
            
            
            
          
            
		} catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } 
		
		
		return false;
	}

}
