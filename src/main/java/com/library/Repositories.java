package com.library;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByPublishedTrueAndTitleContainingIgnoreCaseOrPublishedTrueAndAuthorContainingIgnoreCase(String t, String a);
}
interface BookFileRepository extends JpaRepository<BookFile, Long> {}
interface StudentRepository extends JpaRepository<Student, Long> {}
interface IssueRepository extends JpaRepository<IssueRecord, Long> {}
