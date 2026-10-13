package com.library;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/students")
public class StudentController {
    private final StudentRepository repo;
    public StudentController(StudentRepository repo) { this.repo = repo; }

    @GetMapping public List<Student> all() { return repo.findAll(); }

    @PostMapping public Student create(@RequestBody Student s) { s.setId(null); return repo.save(s); }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student in) {
        Student s = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        s.setName(in.getName()); s.setRollNo(in.getRollNo()); s.setEmail(in.getEmail()); s.setPhone(in.getPhone());
        return repo.save(s);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
