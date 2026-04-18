package org.vagabond.common.api.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.reactive.server.core.multipart.FormData.FileItemImpl;
import org.jboss.resteasy.reactive.server.multipart.FileItem;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.vagabond.common.file.service.FileService;
import org.vagabond.engine.exeption.MetierException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class FileServiceTest {

    @Inject
    FileService fileService;

    @AfterEach
    void cleanup() throws IOException {
        Path testDir = Path.of(System.getProperty("java.io.tmpdir"), "tmp");
        if (Files.exists(testDir)) {
            Files.walk(testDir).sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        }
    }

    @Test
    void uploadFile_retourne_le_chemin() {
        var path = fileService.uploadFile("image.jpg",
                new ByteArrayInputStream(new byte[] { 1, 2, 3 }), "test");
        assertEquals("/test/image.jpg", path);
    }

    @Test
    void uploadFile_avec_id_retourne_chemin_avec_id() {
        var path = fileService.uploadFile(buildFormValue("image.jpg"), "test", 42L);
        assertEquals("/test/42/image.jpg", path);
    }

    @Test
    void uploadFile_avec_id_delegue_au_bon_repertoire() {
        fileService.uploadFile(buildFormValue("image.jpg"), "test", 42L);
        assertTrue(fileService.downloadFile("/test/42/image.jpg").exists());
    }

    @Test
    void uploadFile_formValue_ioexception_leve_MetierException() throws IOException {
        FormValue broken = buildBrokenFormValue();
        assertThrows(MetierException.class, () -> fileService.uploadFile(broken, "/test"));
    }

    @Test
    void uploadFile_inputStream_ioexception_leve_MetierException() {
        InputStream broken = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("disk full");
            }
        };
        assertThrows(MetierException.class,
                () -> fileService.uploadFile("image.jpg", broken, "test"));
    }

    @Test
    void downloadFile_fichier_inexistant_retourne_file_sans_exception() {
        var file = fileService.downloadFile("/inexistant/image.jpg");
        assertNotNull(file);
        assertFalse(file.exists());
    }

    private FormValue buildFormValue(String fileName) {
        return new FormValue() {
            @Override
            public String getValue() {
                return "value";
            }

            @Override
            public String getCharset() {
                return "UTF-8";
            }

            @Override
            public boolean isFileItem() {
                return true;
            }

            @Override
            public String getFileName() {
                return fileName;
            }

            @Override
            public MultivaluedMap<String, String> getHeaders() {
                return null;
            }

            @Override
            public FileItem getFileItem() {
                return new FileItemImpl(new byte[] { 1, 2, 3 });
            }
        };
    }

    private FormValue buildBrokenFormValue() throws IOException {
        FileItem brokenFileItem = mock(FileItem.class);
        when(brokenFileItem.getInputStream()).thenThrow(new IOException("stream error"));

        return new FormValue() {
            @Override
            public String getValue() {
                return "value";
            }

            @Override
            public String getCharset() {
                return "UTF-8";
            }

            @Override
            public boolean isFileItem() {
                return true;
            }

            @Override
            public String getFileName() {
                return "broken.jpg";
            }

            @Override
            public MultivaluedMap<String, String> getHeaders() {
                return null;
            }

            @Override
            public FileItem getFileItem() {
                return brokenFileItem;
            }
        };
    }
}