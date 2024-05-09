package ch.uzh.ifi.hase.soprafs24.config;

import com.google.cloud.secretmanager.v1.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

@Component
public class CredentialsLoader {

  private String MjPublicKey;
  private String MjPrivateKey;

  private final String GCPProjectId = "sopra-fs24-group-01-server";
  private final String GCPMjSecretIdPublic = "mailjet-public-key";
  private final String GCPMjSecretIdPrivate = "mailjet-private-key";
  private final String GCPSecretVersion = "1";

  private final String LocalSecretIdPublic = "MJ_APIKEY_PUBLIC";
  private final String LocalSecretIdPrivate = "MJ_APIKEY_PRIVATE";

  @PostConstruct
  public void initCredentials() throws Exception {
    String appEngineVersion = System.getProperty("com.google.appengine.runtime.version");

    if (appEngineVersion == null) {
      System.out.println("Running locally");
      setLocalCredentials();
    }
    else {
      System.out.println("Running on GAE");
      setGoogleAppEngineCredentials();
    }
  }

  public void setLocalCredentials() {
    setMjPublicKey(System.getenv(LocalSecretIdPublic));
    setMjPrivateKey(System.getenv(LocalSecretIdPrivate));


    if (getMjPrivateKey() == null || getMjPublicKey() == null) {
      System.out.println("One of the environment variables is not set! " +
              "Trying to fall back on the google cloud cli... if installed.");
      try {
        setGoogleAppEngineCredentials();
      }
      catch (Exception e) {
        System.out.println("Unrecoverable problem. You can not access MailJet.");
      }
    }
  }

  public void setGoogleAppEngineCredentials() throws Exception {
    setMjPublicKey(getCredentialsManagerSecret(GCPProjectId, GCPMjSecretIdPublic, GCPSecretVersion));
    setMjPrivateKey(getCredentialsManagerSecret(GCPProjectId, GCPMjSecretIdPrivate, GCPSecretVersion));
  }

  public String getCredentialsManagerSecret(String projectId, String secretId, String secretVersion) throws Exception {
    // https://github.com/GoogleCloudPlatform/java-docs-samples/blob/main/secretmanager/src/main/java/secretmanager/GetSecret.java
    try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
      SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, secretVersion);

      // Access the secret version.
      AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);

      // Verify checksum.
      byte[] data = response.getPayload().getData().toByteArray();
      Checksum checksum = new CRC32C();
      checksum.update(data, 0, data.length);
      if (response.getPayload().getDataCrc32C() != checksum.getValue()) {
        System.out.printf("Data corruption detected.");
      }

      // Convert the response back to a string.
      String payload = response.getPayload().getData().toStringUtf8();

      return payload;
    }
  }

  public String getMjPublicKey() {
    return MjPublicKey;
  }

  public void setMjPublicKey(String mjPublicKey) {
    MjPublicKey = mjPublicKey;
  }

  public String getMjPrivateKey() {
    return MjPrivateKey;
  }

  public void setMjPrivateKey(String mjPrivateKey) {
    MjPrivateKey = mjPrivateKey;
  }
}