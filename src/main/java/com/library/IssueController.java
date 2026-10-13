package com.library;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/admin/issues")
public class IssueController {
    public record IssueRequest(Long bookId, Long studentId, Integer days) {}

    private final IssueRepository issues;
    private final BookRepository books;
    private final StudentRepository students;
    @Value("${fine.per-day}") private double finePerDay;

    public IssueController(IssueRepository i, BookRepository b, StudentRepository s) {
        this.issues = i; this.books = b; this.students = s;
    }

    @GetMapping public List<IssueRecord> all() { return issues.findAll(); }

    @PostMapping
    @Transactional
    public IssueRecord issue(@RequestBody IssueRequest req) {
        Book b = books.findById(req.bookId()).orElseThrow(() -> bad("Book not found"));
        Student s = students.findById(req.studentId()).orElseThrow(() -> bad("Student not found"));
        if (b.getQuantity() < 1) throw bad("No copies available");
        b.setQuantity(b.getQuantity() - 1);
        books.save(b);
        IssueRecord r = new IssueRecord();
        r.setBookId(b.getId()); r.setBookTitle(b.getTitle());
        r.setStudentId(s.getId()); r.setStudentName(s.getName());
        r.setIssueDate(LocalDate.now());
        r.setDueDate(LocalDate.now().plusDays(req.days() == null ? 14 : req.days()));
        return issues.save(r);
    }

    @PostMapping("/{id}/return")
    @Transactional
    public IssueRecord giveBack(@PathVariable Long id) {
        IssueRecord r = issues.findById(id).orElseThrow(() -> bad("Record not found"));
        if (r.getReturnDate() != null) throw bad("Already returned");
        LocalDate today = LocalDate.now();
        r.setReturnDate(today);
        long late = ChronoUnit.DAYS.between(r.getDueDate(), today);
        r.setFine(late > 0 ? late * finePerDay : 0);
        books.findById(r.getBookId()).ifPresent(b -> { b.setQuantity(b.getQuantity() + 1); books.save(b); });
        return issues.save(r);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { issues.deleteById(id); }

    private ResponseStatusException bad(String m) { return new ResponseStatusException(HttpStatus.BAD_REQUEST, m); }
}
