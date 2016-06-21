package servlets;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;











import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import es.TFG.dao.NotarioElectronicoDAO;
import es.TFG.dao.NotarioElectronicoDAOImpl;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
            
            NotarioElectronicoDAO dao = NotarioElectronicoDAOImpl.getInstance();
            boolean existeUsuario = dao.existeUsuario(correo, password);
            
            //devolver al movil existeUsuario
            
           
            
            
            
          
            
		} catch (ParseException e) {
            e.printStackTrace();
        } 
		
		
	}	
	
	public void generaPDF() throws /*DocumentException,*/ FileNotFoundException{
		
		// Se crea el documento
		/*Document documento = new Document();
		
		System.out.println("LLega1" );

		// Se crea el OutputStream para el fichero donde queremos dejar el pdf.
		FileOutputStream ficheroPdf = new FileOutputStream("prueba.pdf");

		// Se asocia el documento al OutputStream y se indica que el espaciado entre
		// lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
		
		PdfWriter.getInstance(documento,ficheroPdf).setInitialLeading(20);
		
		
		documento.open();
		
		
		documento.add(new Paragraph("Esto es el primer párrafo, normalito"));
	
		documento.add(new Paragraph("Este es el segundo y tiene una fuente rara",
						FontFactory.getFont("arial",   // fuente
						22,                            // tamaño
						Font.ITALIC,                   // estilo
						BaseColor.CYAN)));	          // color


		
		documento.close();*/

	}

}
