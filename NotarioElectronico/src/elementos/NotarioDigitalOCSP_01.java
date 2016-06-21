package elementos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.OcspClientBouncyCastle;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.TSAClient;
import com.itextpdf.text.pdf.TSAClientBouncyCastle;

public class NotarioDigitalOCSP_01 {

	// *********************************************
	// PRIVATE CLASS VARS
	// *********************************************

	// Document storage

	private static String PRIVATE_FILESYSTEM = "C:/Users/Enrique/";
	private static String src_full_path = "";
	private static String dest_full_path = "";

	// Signature  

	private static String PRIVATE_KEYSTORE = PRIVATE_FILESYSTEM + "notarioelectronico.jks";
	private static String PRIVATE_KEYSTORE_PASSWORD = "ksjddf8234756dhhd73";
	private static String PRIVATE_ROOTCERT = PRIVATE_FILESYSTEM + "NOTARIO_CERT.crt";
	private static String PRIVATE_CERT_ALIAS = "notarioelectronico";
	private static String PRIVATE_CERT_ALIAS_PASSWORD = "lsdgjflsdkjgf3489759348";

	// TSA

	private static String TSA = "http://time.certum.pl";
	private static String TSA_LOGIN = "";
	private static String TSA_PASSWORD = "";
	
	// PDF Document descriptors

	private static String REASON = "Notario electrónico";
	private static String LOCATION = "Madrid, SP";

	// PDF Configuration

	private static int PDF_SIGNATURE_APERANCE = 1; // 0: Do not show signature
	private static int PDF_SIGNATURE_APERANCE_PAGE = 1; // Sets the page number where to show the signature

	/**
	 * Main method.
	 */
	public static void main(String[] args)

			throws IOException, DocumentException, GeneralSecurityException {
		Security.addProvider(new BouncyCastleProvider());

		String pdf_src = "prueba.pdf";
		String pdf_dest = "simple-signed.pdf";
		boolean withTS = true;		// With time stamping
		boolean withOCSP = true;	// With digital signature		

		NotarioDigitalOCSP_01.signPdf(pdf_src, pdf_dest, withTS, withOCSP);

	}
	
	public static void signPdf(String src, String dest, boolean withTS, boolean withOCSP)
			throws IOException, DocumentException, GeneralSecurityException {

		// ************************************************************
		// WORKING FILES
		// ************************************************************

		src_full_path = PRIVATE_FILESYSTEM + src;
		dest_full_path = PRIVATE_FILESYSTEM + dest;

		// ************************************************************
		// Keystore and certificate chain
		// ************************************************************

		// Open the keysore...

		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream(PRIVATE_KEYSTORE), PRIVATE_KEYSTORE_PASSWORD.toCharArray());

		// Open the signature certificate using the cert alias & cert password

		PrivateKey pk = (PrivateKey)ks.getKey(PRIVATE_CERT_ALIAS, PRIVATE_CERT_ALIAS_PASSWORD.toCharArray());
		java.security.cert.Certificate[] chain = ks.getCertificateChain(PRIVATE_CERT_ALIAS);


		// ******************************************************************
		// WORKING WITH THE PDF OBJECT
		// ******************************************************************

		// Reader & stamper

		PdfReader reader = new PdfReader(src_full_path);
		FileOutputStream fout = new FileOutputStream(dest_full_path);
		PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
		PdfSignatureAppearance sap = stp.getSignatureAppearance();

		// Show signature?

		if (PDF_SIGNATURE_APERANCE == 1)
		{
			sap.setVisibleSignature(new Rectangle(350, 85, 500, 150), PDF_SIGNATURE_APERANCE_PAGE, null);
		}

		sap.setCrypto(null, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
		sap.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);

		PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));

		dic.setReason(REASON);
		dic.setLocation(LOCATION);

		dic.setDate(new PdfDate(sap.getSignDate()));
		sap.setCryptoDictionary(dic);

		// preserve some space for the contents

		int contentEstimated = 15000;
		HashMap<PdfName,Integer> exc = new HashMap<PdfName,Integer>();
		exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
		sap.preClose(exc);

		// make the digest
		InputStream data = sap.getRangeStream();
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		byte buf[] = new byte[8192];
		int n;
		while ((n = data.read(buf)) > 0) {
			messageDigest.update(buf, 0, n);
		}
		byte hash[] = messageDigest.digest();
		Calendar cal = Calendar.getInstance();

		
		// If we add a time stamp:
		TSAClient tsc = null;
		if (withTS) {
			tsc = new TSAClientBouncyCastle(TSA, TSA_LOGIN, TSA_PASSWORD);
		}

		// If we use OCSP:
		byte[] ocsp = null;
		if (withOCSP) {
			String url = PdfPKCS7.getOCSPURL((X509Certificate)chain[0]);
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			FileInputStream is = new FileInputStream(PRIVATE_ROOTCERT);
			X509Certificate root = (X509Certificate) cf.generateCertificate(is);
			ocsp = new OcspClientBouncyCastle().getEncoded((X509Certificate)chain[0], root, url);
		}

		// ***************************************
		// Create the signature
		// ***************************************
		
		PdfPKCS7 sgn = new PdfPKCS7(pk, chain, null, "SHA1", null, false);
		byte sh[] = sgn.getAuthenticatedAttributeBytes(hash, cal, ocsp);
		sgn.update(sh, 0, sh.length);
		byte[] encodedSig = sgn.getEncodedPKCS7(hash, cal, tsc, ocsp);

		// System.out.println("hash: " + hash.toString());

		if (contentEstimated + 2 < encodedSig.length)
			throw new DocumentException("Not enough space");

		byte[] paddedSig = new byte[contentEstimated];
		System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

		// Replace the contents

		PdfDictionary dic2 = new PdfDictionary();
		dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
		sap.close(dic2);

	}

}
