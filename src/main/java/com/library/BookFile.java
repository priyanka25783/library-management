package com.library;

import jakarta.persistence.*;

@Entity
public class BookFile {
    @Id
    private Long bookId;
    private String fileName;
    private String contentType;
    @Lob @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
}
