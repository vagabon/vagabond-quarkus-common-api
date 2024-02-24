package org.vagabond.engine.crud.service;

import org.jboss.resteasy.reactive.server.core.multipart.FormData.FileItemImpl;
import org.jboss.resteasy.reactive.server.multipart.FileItem;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;

@QuarkusTest
class UploadServiceTest {

    @Inject
    UploadService uploadService;

    @Test
    void testDownload() {
        var tested = uploadService.downloadFile("filename");
        Assertions.assertEquals("uploadfilename", tested.getName());
    }

    @Test
    void testUpload() {
        var tested = uploadService.uploadFile(new FormValue() {

            @Override
            public String getValue() {
                return "value";
            }

            @Override
            public String getCharset() {
                return "charset";
            }

            @Override
            public FileItem getFileItem() {
                byte[] content = {};
                return new FileItemImpl(content);
            }

            @Override
            public boolean isFileItem() {
                return false;
            }

            @Override
            public String getFileName() {
                return "filename";
            }

            @Override
            public MultivaluedMap<String, String> getHeaders() {
                return null;
            }

        }, "test", 1L);
        Assertions.assertEquals("test/1/filename", tested);
    }
}
