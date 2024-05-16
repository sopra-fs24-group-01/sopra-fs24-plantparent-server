package ch.uzh.ifi.hase.soprafs24.utility;

import ch.uzh.ifi.hase.soprafs24.exceptions.ImageValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DataBucketUtilityTest {
  @Autowired
  DataBucketUtility dataBucketUtility;

  @Test
  public void convertFile_multipartFile_to_file() throws IOException {
    InputStream imageStream = new ByteArrayInputStream("myMock".getBytes(StandardCharsets.UTF_8));
    MockMultipartFile mockFile = new MockMultipartFile(
            "problematic-name-not-image",
            "mock-text.txt",
            MediaType.TEXT_PLAIN_VALUE,
            imageStream
    );

    File convertedFile = dataBucketUtility.convertFile(mockFile);
    Mockito.eq(convertedFile.exists());
    boolean deleted = convertedFile.delete();
  }

  @Test
  public void check_ImageExtension_valid() {
    String fileName = "file.jpg";
    assertEquals(dataBucketUtility.checkFileExtension(fileName), ".jpg");
  }

  @Test
  public void check_ZipExtension_invalid() {
    String fileName = "file.zip";
    assertThrows(
            ImageValidationException.class,
            () -> dataBucketUtility.checkFileExtension(fileName)
    );
  }
}
