package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.utility.DataBucketUtility;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class GCPStorageServiceTest {

  @Autowired
  private GCPStorageService gcpStorageService;
  @Mock
  private DataBucketUtility dataBucketUtility;

  //@Test TODO, fix this test --> wanted but not called
  public void test_gcpUpload() throws IOException {
    String url = "my-test-url.com/mock-image.jpg";
    given(
            dataBucketUtility.uploadFile(
                    Mockito.any(),
                    Mockito.anyString(),
                    Mockito.anyString()
            )
    ).willReturn(url);

    FileInputStream imageStream = new FileInputStream("src/test/test-resources/mock-image.jpg");
    MockMultipartFile mockImage = new MockMultipartFile(
            "problematic-name-not-image",
            "mock-image.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            imageStream
    );

    String newUrl = gcpStorageService.uploadImage(mockImage);

    verify(dataBucketUtility, Mockito.times(1))
            .uploadFile(mockImage, "mock-image.jpg", MediaType.IMAGE_JPEG_VALUE);
  }

}
