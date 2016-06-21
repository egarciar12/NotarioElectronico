package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Usuario;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import es.TFG.dao.NotarioElectronicoDAO;
import es.TFG.dao.NotarioElectronicoDAOImpl;

/**
 * Servlet implementation class Registro
 */
@WebServlet("/Registro")
public class Registro extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Registro() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String correo = "prueba";
		String password = "prueba";
		NotarioElectronicoDAO dao = NotarioElectronicoDAOImpl.getInstance();
        boolean existeUsuario = dao.existeUsuario(correo, password);
        if(existeUsuario){
        	System.out.println("El usuario existe");
        }
        else{
        	System.out.println("El usuario existe");
        }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Me ha llegado una peticion");
		PrintWriter out = response.getWriter();
		//out.println("Se ha solicitado un post");
		
		BufferedReader br = request.getReader();
		String line = null;
		String texto = "";
		while((line = br.readLine()) != null){
			texto += line;
			out.println("Server read this: "+line);
			
		}
		System.out.println(texto);
		
		
		JSONParser parser = new JSONParser();
	
		try {
            Object obj = parser.parse(texto);

            JSONObject json = (JSONObject) obj;
          
            
            String correo = json.get("correo").toString();
            System.out.println("Server read this USUARIO: "+correo);
            String password = json.get("longitud").toString();
            System.out.println("Server read this PASSWORD: "+password);
            String nombre = json.get("longitud").toString();
            System.out.println("Server read this PASSWORD: "+nombre);
            String apellidos = json.get("longitud").toString();
            System.out.println("Server read this PASSWORD: "+apellidos);
            
            NotarioElectronicoDAO dao = NotarioElectronicoDAOImpl.getInstance();
            //Usuario usuario = new Usuario(correo, nombre, apellidos, password);
            
            dao.registroUsuario(correo, nombre, apellidos, password);
            
            //devolver al movil si se ha registrado bien
            
            
          
            
		} catch (ParseException e) {
            e.printStackTrace();
        } 
		
		
		
	}

}
