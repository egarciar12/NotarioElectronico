package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;


import java.security.*;
import java.security.cert.Certificate;
import java.io.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.LtvTimestamp;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.TSAClient;
import com.itextpdf.text.pdf.TSAClientBouncyCastle;


/*import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;*/
/*import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;*/
//import com.lowagie.text.pdf.*;
//import com.lowagie.text.*;
//import com.itextpdf.text.pdf.OcspClientBouncyCastle;
/*
//import org.bouncycastle.jce.provider.BouncyCastleProvider;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.OcspClientBouncyCastle;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
//import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.security.PdfPKCS7;
//import com.itextpdf.text.pdf.TSAClient;
//import com.itextpdf.text.pdf.TSAClientBouncyCastle;*/


/**
 * Servlet implementation class CrearPDF
 */
@WebServlet("/CrearPDF")
public class CrearPDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearPDF() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Me ha llegado una peticion");
		PrintWriter out = response.getWriter();
		out.println("Se ha solicitado un get");
		
		//mandaPDF(img);
		signPDF();
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	
	public void signPDF(){
		 try {
			 
			 
			KeyStore ks = KeyStore.getInstance("pkcs12");
	         ks.load(new FileInputStream("C:/test/my_keystore.pfx"), "ksjddf8234756dhhd73".toCharArray());
	         
	         String alias = (String)ks.aliases().nextElement();
	         PrivateKey key = (PrivateKey)ks.getKey(alias, "ksjddf8234756dhhd73".toCharArray());
	         Certificate[] chain = ks.getCertificateChain(alias);
	         
	         PdfReader reader = new PdfReader("prueba.pdf"); 
	         FileOutputStream fout = new FileOutputStream("C:/Users/Enrique/Documents/Universidad/prueba3.pdf");
	         
	         PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
	         
	         PdfSignatureAppearance sap = stp.getSignatureAppearance();
	         
	         /*//Añadimos sellado de tiempo
	         
	         TSAClient tsa = new TSAClientBouncyCastle("http://time.certum.pl", "", "");
	         
	         LtvTimestamp.timestamp(sap, tsa, null);*/
	         
	         
	         sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
	         sap.setReason("soy el autor");
	         sap.setLocation("madrid");              
	         
	         
	         sap.setVisibleSignature(new Rectangle(100,100,200,200),1,null);
	        
	         /*//Añadimos sellado de tiempo
	         
	         TSAClient tsa = new TSAClientBouncyCastle("http://time.certum.pl", "", "");
	         
	         LtvTimestamp.timestamp(sap, tsa, null);
	        */
		        
	         stp.close();
		 }
	        catch(Exception e) {
	            e.printStackTrace();
	        }
			
		
		
				 
	}


}
