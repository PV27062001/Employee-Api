package com.employeeApplication.interview;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interview")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InterviewController {
    private final InterviewService interviewService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/apply-job")
    public ResponseEntity<Interview> applyForInterview(@RequestBody InterviewRequest interviewRequest){
        return ResponseEntity.ok(interviewService.addInterviewData(interviewRequest));
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @PatchMapping("/update-result")
    public String isGotJob(@RequestBody ResultUpdate resultUpdate){
        return interviewService.jobResponse(resultUpdate);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/getall/interview")
    public List<Interview> getAll(){
        return interviewService.getAll();
    }
}
