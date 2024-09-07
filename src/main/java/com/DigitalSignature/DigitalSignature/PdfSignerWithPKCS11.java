package com.DigitalSignature.DigitalSignature;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PdfSignerWithPKCS11 {
    private static final Logger LOGGER = Logger.getLogger(PdfSignerWithPKCS11.class.getName());

    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());

            String pkcs11Config = "name = ProxKey\n" +
                    "library = /usr/lib/x86_64-linux-gnu/pkcs11/opensc-pkcs11.so\n" +
                    "slot = 0\n";
            Provider []prod = Security.getProviders();
            for(Provider p:prod) {
                System.out.println(p);
            }


            Provider pkcs11Provider = Security.getProvider("SunJGSS");
            pkcs11Provider = pkcs11Provider.configure(pkcs11Config);
            Security.addProvider(pkcs11Provider);

            KeyStore ks = KeyStore.getInstance("PKCS11", pkcs11Provider);
            ks.load(null, "123456789".toCharArray());

            String alias = getFirstAlias(ks);
            PrivateKey pk = (PrivateKey) ks.getKey(alias, null);
            Certificate[] chain = ks.getCertificateChain(alias);

            String src = "sign.pdf";
            String dest = "signed_output.pdf";

            PdfReader reader = new PdfReader(src);
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdfDoc = new PdfDocument(reader, writer);
            FileOutputStream os = new FileOutputStream(dest);
            PdfSigner signer = new PdfSigner(reader, os, new com.itextpdf.kernel.pdf.StampingProperties());

            BouncyCastleDigest digest = new BouncyCastleDigest();
            IExternalSignature signature = new PrivateKeySignature(pk, "SHA-256", "BC");

            signer.signDetached(digest, signature, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);

            System.out.println("Document signed successfully!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred during PDF signing", e);
        }
    }

    private static String getFirstAlias(KeyStore ks) throws Exception {
        Enumeration<String> aliases = ks.aliases();
        if (aliases.hasMoreElements()) {
            return aliases.nextElement();
        } else {
            throw new RuntimeException("No aliases found");
        }
    }
}
