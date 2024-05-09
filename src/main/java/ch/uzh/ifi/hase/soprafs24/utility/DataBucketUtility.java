package ch.uzh.ifi.hase.soprafs24.utility;

import ch.uzh.ifi.hase.soprafs24.exceptions.GCPFileUploadException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
public class DataBucketUtility {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataBucketUtility.class);

  @Value("${gcp.project.id}")
  private String gcpProjectId;

  @Value("${gcp.bucket.name}")
  private String gcpBucketName;

  /**
   * Accepts a multipart file that contains an image and renames it to then upload it to a GCP Storage Bucket.
   * Only a subset of file extensions are allowed.
   *
   * @param multipartFile The image to be uploaded
   * @param fileName      The filename
   * @param contentType   MediaType of file.
   * @return String: The storage path containing the newly generated URI.
   */
  public String uploadFile(MultipartFile multipartFile, String fileName, String contentType) {
    try {
      // Convert the Multipart file into a byte array
      File convertedFile = convertFile(multipartFile);
      byte[] fileData = FileUtils.readFileToByteArray(convertedFile);

      // Get the contexts default credentials
      // On GCP this is the current user
      // Locally, this is the user authenticated with gcloud SDK
      GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

      StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
              .setCredentials(credentials).build();

      Storage storage = options.getService();
      Bucket bucket = storage.get(gcpBucketName, Storage.BucketGetOption.fields());


      // Create the blob on Google cloud storage
      Blob blob = bucket.create(fileName + checkFileExtension(fileName), fileData, contentType);

      // Remove the temporary file
      convertedFile.delete();

      if (blob != null) {
        LOGGER.debug("File successfully uploaded to GCS");

        // Construct the uri to show the image
        return "https://storage.googleapis.com/" + blob.getBucket() + "/" + blob.getName();
      }

    }
    catch (Exception e) {
      LOGGER.error("An error occurred while uploading data. Exception: ", e);
      throw new RuntimeException("An error occurred while storing data to GCS");
    }
    throw new GCPFileUploadException("An error occurred while storing data to GCS");
  }

  /**
   * Convert a MultipartFile to File
   * Checks that the file name is not null and names the File accordingly.
   *
   * @param file Multipart file of an image
   * @return image as File type
   */
  public File convertFile(MultipartFile file) {

    try {
      if (file.getOriginalFilename() == null) {
        throw new GCPFileUploadException("Original file name is null");
      }

      // make sure the tmp-upload directory exists
      String dirPath = "/tmp/upload/";
      File directory = new File(dirPath);
      if (!directory.exists()) {
        directory.mkdirs();
      }

      File convertedFile = new File(dirPath + file.getOriginalFilename());
      FileOutputStream outputStream = new FileOutputStream(convertedFile);
      outputStream.write(file.getBytes());
      outputStream.close();

      LOGGER.debug("Converting multipart file : {}", convertedFile);
      return convertedFile;

    }
    catch (Exception e) {
      throw new GCPFileUploadException("An error has occurred while converting the file");
    }
  }

  /**
   * Validates that the ending of the file is in the extensionList provided.
   *
   * @param fileName A file name as String.
   * @return the matched extension
   */
  public String checkFileExtension(String fileName) {
    if (fileName != null && fileName.contains(".")) {
      String[] extensionList = {".png", ".jpeg", ".jpg"};

      for (String extension : extensionList) {
        if (fileName.endsWith(extension)) {
          LOGGER.debug("Accepted file type : {}", extension);
          return extension;
        }
      }
    }
    LOGGER.error("Not a permitted file type");
    throw new GCPFileUploadException("Not a permitted file type");
  }
}