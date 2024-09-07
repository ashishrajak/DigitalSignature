

package com.DigitalSignature.DigitalSignature;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignatureUtil;
import com.itextpdf.kernel.pdf.PdfDictionary;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Service
public class PDFSignatureService {
 public void extractSignatureInfo(String filePath) {
     Security.addProvider(new BouncyCastleProvider());  // Add Bouncy Castle as provider
     File file = new File(filePath);

     try (PdfReader reader = new PdfReader(file);
          PdfDocument pdfDoc = new PdfDocument(reader)) {

         SignatureUtil signUtil = new SignatureUtil(pdfDoc);
         List<String> signatureNames = signUtil.getSignatureNames();

         if (signatureNames.isEmpty()) {
             System.out.println("No digital signatures found.");
         } else {
             for (String signatureName : signatureNames) {
                 System.out.println("Signature: " + signatureName);


                 PdfPKCS7 pkcs7 = signUtil.readSignatureData(signatureName);
                 if (pkcs7 == null) {
                     System.out.println("Unable to extract PKCS7 data for signature: " + signatureName);
                     continue;
                 }


                 boolean signatureVerified = pkcs7.verifySignatureIntegrityAndAuthenticity();
                 System.out.println("Is signature verified: " + signatureVerified);


                 X509Certificate cert = pkcs7.getSigningCertificate();
                 if (cert != null) {

                     System.out.println("Subject: " + cert.getSubjectDN());


                     System.out.println("Issuer: " + cert.getIssuerDN());


                     System.out.println("Signature date: " + pkcs7.getSignDate().getTime());


                     System.out.println("Organizational Unit (OU): " + cert.getSubjectX500Principal().getName());


                     Certificate[] certChain = pkcs7.getSignCertificateChain();
                     if (certChain != null) {
                         for (Certificate chainCert : certChain) {
                             System.out.println("Certificate in chain: " + chainCert.toString());
                         }
                     }
                 } else {
                     System.out.println("No signing certificate found for signature: " + signatureName);
                 }
             }
         }
     } catch (IOException e) {
         System.err.println("IOException: " + e.getMessage());
         e.printStackTrace();
     } catch (Exception e) {
         System.err.println("Exception while processing signature: " + e.getMessage());
         e.printStackTrace();
     }
 }



    public static void main(String[] args) {
        PDFSignatureService service = new PDFSignatureService();
        String filePath = "sign.pdf";  // Update this to the path of your PDF file
        service.extractSignatureInfo(filePath);
    }

}
