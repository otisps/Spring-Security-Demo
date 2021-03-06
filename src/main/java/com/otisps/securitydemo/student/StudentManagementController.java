package com.otisps.securitydemo.student;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path="/mangement/api/v1/students")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Crewe"),
            new Student(2, "John Doe"),
            new Student(3, "Jack Erwin"),
            new Student(4, "Joshua Golf")
    );

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public static List<Student> getAllStudents() {
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student){
        System.out.println(student.toString());
    }

    @DeleteMapping(path="{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable(name = "studentId", required = true) Integer studentId){
        System.out.println(STUDENTS.get(studentId));
    }

    @PutMapping(path="{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable(name = "studentId", required = true) Integer studentId, @RequestBody Student student){
        System.out.println(studentId.toString() + student.toString());
    }
}
