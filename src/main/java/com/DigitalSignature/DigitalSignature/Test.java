package com.DigitalSignature.DigitalSignature;


import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.PdfSignatureAppearance;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;


import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;

public class Test {


    static PrivateKey privateKey;
    static PublicKey publicKey;
    static X509Certificate x509Certificate;
    static Certificate cert;
    static String inputFileName = "testPdf.pdf";
    static String outputFile = "signedPdf.pdf";

    public static void main(String[] args) {
        try {
            /*String pkcs11ConfigPath = "config.cfg";
            Provider providerPKCS11 = Security.getProvider("SunPKCS11");

            // Create the provider using the configuration file
            providerPKCS11 = providerPKCS11.configure(pkcs11ConfigPath);
            Security.addProvider(providerPKCS11);

            // Create the KeyStore for accessing certificates in the USB device by supplying the PIN.
            KeyStore keyStore = KeyStore.getInstance("PKCS11", providerPKCS11);
            keyStore.load(null, "123456789".toCharArray()); // Replace "your-pin" with the actual PIN

            // Enumerate items (certificates and private keys) in the KeyStore
            Enumeration<String> aliases = keyStore.aliases();
            String alias = null;

            while (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
                cert = keyStore.getCertificate(alias);
                x509Certificate = (X509Certificate) cert;

                // Check whether the certificate has digitalSignature key usage
                if (x509Certificate.getKeyUsage()[0]) {
                    Key key = keyStore.getKey(alias, null); // Access the private key
                    privateKey = (PrivateKey) key;
                    publicKey = x509Certificate.getPublicKey();
                    break;
                }
            }

            // Reader and stamper
            PdfReader pdf = new PdfReader(inputFileName);
            FileOutputStream fos = new FileOutputStream(outputFile);
            PdfStamper stp = PdfStamper.createSignature(pdf, fos, '\0');
            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setReason("Author Abdullah AlHussein");

            // Appearance
            PdfSignatureAppearance appearance = stp.getSignatureAppearance();
            appearance.setReason("Agreeing to the contract");
            appearance.setLocation("Riyadh, Saudi Arabia");
            appearance.setVisibleSignature(new Rectangle(72, 732, 250, 850), 1, "primeira assinatura");

            // Digital signature
            ExternalSignature es = new PrivateKeySignature(privateKey, "SHA-1", providerPKCS11.getName());
            ExternalDigest digest = new BouncyCastleDigest();
            Certificate[] certs = new Certificate[1];
            certs[0] = cert;

            // Sign the document using detached mode, CMS or CAdES equivalent
            MakeSignature.signDetached(sap, digest, es, certs, null, null, null, 0, MakeSignature.CryptoStandard.CMS);

            System.out.println("The PDF file has been signed successfully");*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);

        // Using BouncyCastle X509v3CertificateGenerator to generate a certificate
        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                new X500Name("CN=Test"),
                new BigInteger(Long.toString(now)),
                startDate,
                new Date(now + 365 * 24 * 60 * 60 * 1000L), // Valid for 1 year
                new X500Name("CN=Test"),
                keyPair.getPublic());

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
        X509CertificateHolder certHolder = certBuilder.build(contentSigner);

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
    }
}
