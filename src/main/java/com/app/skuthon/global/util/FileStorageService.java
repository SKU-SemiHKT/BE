package com.app.skuthon.global.util;

import com.app.skuthon.domain.proof.exception.ProofErrorCode;
import com.app.skuthon.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

  @Value("${file.upload-dir:./uploads}")
  private String uploadDir;

  public String save(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new CustomException(ProofErrorCode.EMPTY_FILE);
    }
    try {
      String original = file.getOriginalFilename();
      String ext = (original != null && original.contains("."))
          ? original.substring(original.lastIndexOf("."))
          : ".jpg";
      String filename = UUID.randomUUID() + ext;

      Path path = Paths.get(uploadDir, filename);
      Files.createDirectories(path.getParent());
      file.transferTo(path.toAbsolutePath().toFile());

      return "/uploads/" + filename;
    } catch (IOException e) {
      throw new CustomException(ProofErrorCode.FILE_UPLOAD_FAILED);
    }
  }
}