package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.exceptions.GCPFileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.uzh.ifi.hase.soprafs24.utility.DataBucketUtility;

import java.util.UUID;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GCPStorageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GCPStorageService.class);

  private final DataBucketUtility dataBucketUtility;

  public GCPStorageService(DataBucketUtility dataBucketUtility) {
    this.dataBucketUtility = dataBucketUtility;
  }

  /**
   * Accepts the multipart file received by the REST endpoint.
   *
   * @param file Multipart file containing an image
   * @return String, The uploaded images URI.
   */
  public String uploadImage(MultipartFile file) {

    String imageUrl;

    // extract the original filename to validate the file ending.
    String originalFileName = file.getOriginalFilename();
    if (originalFileName == null) {
      throw new GCPFileUploadException("Original file name is null");
    }

    // Create a new, random, file name.
    Path path = new File(originalFileName).toPath();
    String newFilename = UUID.randomUUID() + "." + FilenameUtils.getExtension(originalFileName);

    try {
      String contentType = Files.probeContentType(path);
      imageUrl = dataBucketUtility.uploadFile(file, newFilename, contentType);

      if (imageUrl != null) {
        LOGGER.info("Uploaded file to: {}", imageUrl);
      }
    }
    catch (Exception e) {
      LOGGER.error("Error occurred while uploading. Error: ", e);
      throw new GCPFileUploadException("Error occurred while uploading");
    }

    return imageUrl;
  }

}

