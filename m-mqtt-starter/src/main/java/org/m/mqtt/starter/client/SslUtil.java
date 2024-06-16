package org.m.mqtt.starter.client;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SslUtil {

    public static SSLSocketFactory getSocketFactory(final String caCrtFile) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        X509Certificate caCert = null;
        InputStream bis = null;
        if (StrUtil.isEmpty(caCrtFile)|| caCrtFile.contains("classpath")) {
            ClassPathResource certResource = new ClassPathResource(caCrtFile.split(":")[1]);
            bis = certResource.getInputStream();
        } else {
            bis = FileUtil.getInputStream(caCrtFile);
        }
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        while (bis.available() > 0) {
            caCert = (X509Certificate) cf.generateCertificate(bis);
        }
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext.getSocketFactory();
    }
}

