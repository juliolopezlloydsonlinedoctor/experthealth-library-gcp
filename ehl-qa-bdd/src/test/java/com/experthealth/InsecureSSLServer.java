package com.experthealth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Sets up an SSL server.
 * 
 * @author ronan.tobin
 *
 */
public class InsecureSSLServer implements Callable<Boolean> {

    private volatile boolean serverUp = false;
    private volatile boolean runningThread = true;
    
    private final int SSL_PORT = 8111;
    private static final String CERT_PASSWORD = "experthealth";
    
    private String response=null;
    private Thread callableThread;
    
    private SSLSocket sslSocket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    
    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public Boolean call() throws Exception {
        callableThread = new Thread(){
            public void run(){
                setUpServer();
            }
            
        };
        callableThread.start();
        while(!getServerUp()){
        }
        return getServerUp();
    }

    /**
     * Starts the SSL service with default settings.
     */
    public void setUpServer() {
        try {
            KeyStore keyStore = getKeyStore("JKS", "mycert.rtt", CERT_PASSWORD);
            KeyManagerFactory kmf = getKeyManager(keyStore, CERT_PASSWORD);
            SSLServerSocket sslServer = createSSLServer(kmf, "TLS", trustManager, SSL_PORT);
            
            printServerSocketInfo(sslServer);
            startServerSocket(sslServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setUpServer(String type, String cert, String certPassword, String protocol, int port) {
        try {
            KeyStore keyStore = getKeyStore(type, cert, certPassword);
            KeyManagerFactory kmf = getKeyManager(keyStore, certPassword);
            SSLServerSocket sslServer = createSSLServer(kmf, protocol, trustManager, port);
            serverUp = true;
            printServerSocketInfo(sslServer);
            startServerSocket(sslServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Define what key store and keystore type to use.
     * 
     * @param type the keystore type to use (e.g. JKS)
     * @param certificate the certificate to use.
     * @param certPassword the password for the certificate.
     * @return A key storage facility for  cryptographic keys and certificates
     */
    private KeyStore getKeyStore(String type, String certificate, String certPassword) {
        InputStream certInputStream = null;
        KeyStore keyStore = null;
        
        try {
        keyStore = KeyStore.getInstance(type);
        certInputStream = this.getClass().getResourceAsStream(certificate);
        keyStore.load(certInputStream, certPassword.toCharArray());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (certInputStream != null) {
                try {
                    certInputStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return keyStore;
    }

    /**
     * Starts up the SSL server
     * 
     * @param sslServer the SSL server socket
     * @param responseBody the response body to be returned.
     * @throws IOException
     */
    private void startServerSocket(SSLServerSocket sslServer) throws IOException {
        if(response==null) {setResponse("{ \"status\":\"OK\" }");}
        
        while (runningThread) {
            serverUp = true;
            sslSocket = (SSLSocket) sslServer.accept();
            printSocketInfo(sslSocket);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
           
            bufferedReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            
            String m = bufferedReader.readLine();
            if (m != null) {
                bufferedWriter.write("HTTP/1.0 200 OK");
                bufferedWriter.newLine();
                bufferedWriter.write("Content-Type: Application/json");
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                bufferedWriter.write(response);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.close();
            bufferedReader.close();
            sslSocket.close();
        }
    }

    /**
     * Creates a new SSL server.
     * 
     * @param keyManagerFactory a factory for key managers.
     * @param protocol  the standard name of the requested protocol
     * @param trustmanager trustmanager array of certificates.
     * @param port the connection port
     * 
     * @return the SSL server socket
     * 
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     */
    private SSLServerSocket createSSLServer(KeyManagerFactory keyManagerFactory, String protocol, TrustManager[] trustmanager, int port)
            throws NoSuchAlgorithmException, KeyManagementException,
            IOException {
        SSLContext sc = SSLContext.getInstance(protocol);

        sc.init(keyManagerFactory.getKeyManagers(), trustmanager, new java.security.SecureRandom());
        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
        SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(port);
        return s;
    }

    /**
     * Gets a key manager for a given key store.
     * 
     * @param keyStore the given key store
     * @param certPassword the certificate password.
     * @return a factory for managing key stores.
     * 
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     */
    private KeyManagerFactory getKeyManager(KeyStore keyStore, String certPassword) throws NoSuchAlgorithmException, KeyStoreException,
            UnrecoverableKeyException {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, certPassword.toCharArray());
        return kmf;
    }

    public boolean getServerUp() {
        return serverUp;
    }

    /**
     * a TrustManager array of unsecured X509Certificates.
     */
    private TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }
    } };

    private void printSocketInfo(SSLSocket s) {
        System.out.println("Socket class: " + s.getClass());
        System.out.println("   Remote address = "
                + s.getInetAddress().toString());
        System.out.println("   Remote port = " + s.getPort());
        System.out.println("   Local socket address = "
                + s.getLocalSocketAddress().toString());
        System.out.println("   Local address = "
                + s.getLocalAddress().toString());
        System.out.println("   Local port = " + s.getLocalPort());
        System.out.println("   Need client authentication = "
                + s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        System.out.println("   Cipher suite = " + ss.getCipherSuite());
        System.out.println("   Protocol = " + ss.getProtocol());
    }

    private void printServerSocketInfo(SSLServerSocket s) {
        System.out.println("Server socket class: " + s.getClass());
        System.out.println("   Socker address = "
                + s.getInetAddress().toString());
        System.out.println("   Socker port = " + s.getLocalPort());
        System.out.println("   Need client authentication = "
                + s.getNeedClientAuth());
        System.out.println("   Want client authentication = "
                + s.getWantClientAuth());
        System.out.println("   Use client mode = " + s.getUseClientMode());
    }
    
    public static void main(String args[]) {
        new InsecureSSLServer().setUpServer();
    }
}
