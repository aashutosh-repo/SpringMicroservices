package com.spring.batch.controller;


import com.spring.batch.constants.FileConstants;
import com.spring.batch.dto.ResponseDTO;
import com.spring.batch.paymentEntity.FileMetadata;
import com.spring.batch.repositories.payment.FileMetadataRepository;
import com.spring.batch.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileUploadController {

    private final StorageService storage;
    private final FileMetadataRepository uploadFileRepository;

    @PostMapping(path="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> uploadFile(@RequestParam("file") MultipartFile file, Principal p) throws IOException {
        String path = storage.store(file);
        FileMetadata meta = new FileMetadata();
        meta.setFileName(file.getOriginalFilename());
        meta.setFileSizeBytes(file.getSize());
        meta.setStoragePath(path);
        if(p == null || p.getName().isEmpty()){
            meta.setUploadedBy("ANONYMOUS");
        } else{
            meta.setUploadedBy(p.getName());
        }
        meta.setFileType(FileConstants.FILE_TYPE_TRANSACTION);
        meta.setStatus("SUBMITTED");
        uploadFileRepository.save(meta);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("File uploaded successfully");
        responseDTO.setUploadedFileId("12345");
        responseDTO.setJobId("job-67890");
        responseDTO.setStatus(FileConstants.STATUS_COMPLETED);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/uploads")
    public ResponseEntity<List<FileMetadata>> getFileMetadata(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size
    ){
//        Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").descending());

//        Page<FileMetadata> metadata = uploadFileRepository.findByFileType(FileConstants.FILE_TYPE_TRANSACTION, pageable);
        List<FileMetadata> allMetadata = uploadFileRepository.findAll(Sort.by("uploadedAt").descending());
        return ResponseEntity.ok(allMetadata);
    }
}
