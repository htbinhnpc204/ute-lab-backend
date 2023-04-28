package com.nals.tf7.v1;

import com.nals.tf7.AbstractTest;
import com.nals.tf7.Review360App;
import com.nals.tf7.api.v1.MediaCrudController;
import com.nals.tf7.security.jwt.JWTFilter;
import com.nals.tf7.security.jwt.TokenProvider;
import com.nals.tf7.service.v1.MinioService;
import com.nals.tf7.service.v1.RedisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Review360App.class)
public class MediaCrudControllerIntTest
    extends AbstractTest {
    public static final int ALLOW_HEIGHT = 640;
    public static final int ALLOW_WIDTH = 640;
    public static final int INVALID_WIDTH = 1024;
    public static final int INVALID_HEIGHT = 1024;
    public static final String TXT_EXTENSION = "txt";
    public static final String PLAIN_TEXT_TYPE = "text/plain";
    public static final String PNG_EXTENSION = "png";
    public static final String PNG_CONTENT_TYPE = "image/png";
    public static final String FILE_NAME = "test.png";
    public static final String FILE_FIELD = "file";

    private MockMvc restMvc;
    private String baseUrl;

    @Autowired
    private MediaCrudController mediaCrudController;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private RedisService redisService;

    @MockBean
    private MinioService minioService;

    @Before
    public void setup() {
        this.restMvc = MockMvcBuilders.standaloneSetup(mediaCrudController)
                                      .addFilter(new JWTFilter(tokenProvider, redisService))
                                      .setMessageConverters(getHttpMessageConverters())
                                      .setControllerAdvice(getExceptionTranslator())
                                      .build();
        this.baseUrl = "/api/v1/media";
    }

    @Test
    @Transactional
    public void test_uploadMedia_shouldBeOk()
        throws Exception {

        var multipartFile = createMockImageFile(ALLOW_WIDTH, ALLOW_HEIGHT, PNG_EXTENSION, PNG_CONTENT_TYPE, FILE_NAME);

        Mockito.when(minioService.uploadFile(any(), any(), any())).thenReturn(FILE_NAME);

        restMvc.perform(MockMvcRequestBuilders.multipart(baseUrl)
                                              .file(multipartFile))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data.stored_key").exists());
    }

    @Test
    @Transactional
    public void test_uploadMedia_withInvalidWidth_shouldBeBadRequest()
        throws Exception {
        var multipartFile = createMockImageFile(INVALID_WIDTH, ALLOW_HEIGHT, PNG_EXTENSION, PNG_CONTENT_TYPE, FILE_NAME);

        Mockito.when(minioService.uploadFile(any(), any(), any())).thenReturn(FILE_NAME);

        restMvc.perform(MockMvcRequestBuilders.multipart(baseUrl)
                                              .file(multipartFile))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void test_uploadMedia_withInvalidHeight_shouldBeBadRequest()
        throws Exception {
        var multipartFile = createMockImageFile(ALLOW_WIDTH, INVALID_HEIGHT, PNG_EXTENSION, PNG_CONTENT_TYPE, FILE_NAME);

        Mockito.when(minioService.uploadFile(any(), any(), any())).thenReturn(FILE_NAME);

        restMvc.perform(MockMvcRequestBuilders.multipart(baseUrl)
                                              .file(multipartFile))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void test_uploadMedia_withInvalidExtension_shouldBeBadRequest()
        throws Exception {
        var multipartFile = createMockImageFile(ALLOW_WIDTH, INVALID_HEIGHT, TXT_EXTENSION, PNG_CONTENT_TYPE, FILE_NAME);

        Mockito.when(minioService.uploadFile(any(), any(), any())).thenReturn(FILE_NAME);

        restMvc.perform(MockMvcRequestBuilders.multipart(baseUrl)
                                              .file(multipartFile))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void test_uploadMedia_withInvalidContentType_shouldBeBadRequest()
        throws Exception {
        var multipartFile = createMockImageFile(ALLOW_WIDTH, INVALID_HEIGHT, PNG_EXTENSION, PLAIN_TEXT_TYPE, FILE_NAME);

        Mockito.when(minioService.uploadFile(any(), any(), any())).thenReturn(FILE_NAME);

        restMvc.perform(MockMvcRequestBuilders.multipart(baseUrl)
                                              .file(multipartFile))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void test_uploadMedia_withFileNameIsNull_shouldBeBadRequest()
        throws Exception {
        var multipartFile = createMockImageFile(ALLOW_WIDTH, INVALID_HEIGHT, PNG_EXTENSION, PLAIN_TEXT_TYPE, null);

        Mockito.when(minioService.uploadFile(any(), any(), any())).thenReturn(FILE_NAME);

        restMvc.perform(MockMvcRequestBuilders.multipart(baseUrl)
                                              .file(multipartFile))
               .andExpect(status().isBadRequest());
    }

    private MockMultipartFile createMockImageFile(final int width, final int height, final String extension,
                                                  final String contentType, final String fileName) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, extension, byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();

        return new MockMultipartFile(FILE_FIELD, fileName, contentType, bytes);
    }
}
