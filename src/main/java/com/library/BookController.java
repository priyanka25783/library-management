package com.library;

import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;

@RestController
public class BookController {
    private final BookRepository books;
    private final BookFileRepository files;
    public BookController(BookRepository books, BookFileRepository files) { this.books = books; this.files = files; }

    // ---------- PUBLIC ----------
    @GetMapping("/api/books")
    public List<Book> publicBooks(@RequestParam(defaultValue = "") String q) {
        return books.findByPublishedTrueAndTitleContainingIgnoreCaseOrPublishedTrueAndAuthorContainingIgnoreCase(q, q);
    }

    @GetMapping("/api/books/{id}/file")
    public ResponseEntity<byte[]> publicFile(@PathVariable Long id) {
        Book b = books.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!b.isPublished()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return file(id);
    }

    // ---------- ADMIN ----------
    @GetMapping("/api/admin/books")
    public List<Book> all() { return books.findAll(); }

    @PostMapping("/api/admin/books")
    public Book create(@RequestBody Book b) { b.setId(null); b.setHasFile(false); return books.save(b); }

    @PutMapping("/api/admin/books/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book in) {
        Book b = books.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        b.setTitle(in.getTitle()); b.setAuthor(in.getAuthor()); b.setIsbn(in.getIsbn());
        b.setCategory(in.getCategory()); b.setDescription(in.getDescription()); b.setQuantity(in.getQuantity());
        return books.save(b);
    }

    @PostMapping("/api/admin/books/{id}/publish")
    public Book publish(@PathVariable Long id, @RequestParam boolean value) {
        Book b = books.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        b.setPublished(value);
        return books.save(b);
    }

    @DeleteMapping("/api/admin/books/{id}")
    @Transactional
    public void delete(@PathVariable Long id) {
        files.deleteById(id);
        books.deleteById(id);
    }

    @PostMapping("/api/admin/books/{id}/file")
    public Book upload(@PathVariable Long id, @RequestParam("file") MultipartFile f) throws IOException {
        Book b = books.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        BookFile bf = new BookFile();
        bf.setBookId(id);
        bf.setFileName(f.getOriginalFilename());
        bf.setContentType(f.getContentType());
        bf.setData(f.getBytes());
        files.save(bf);
        b.setHasFile(true);
        return books.save(b);
    }

    @GetMapping("/api/admin/books/{id}/file")
    public ResponseEntity<byte[]> adminFile(@PathVariable Long id) { return file(id); }

    private ResponseEntity<byte[]> file(Long id) {
        BookFile bf = files.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String ct = bf.getContentType() == null ? "application/octet-stream" : bf.getContentType();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(ct))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + bf.getFileName() + "\"")
                .body(bf.getData());
    }
}
