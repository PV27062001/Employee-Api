package com.employeeApplication.interview;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/interview")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InterviewController {
    private final InterviewService interviewService;

    @PostMapping("/apply")
    @SneakyThrows
    public ResponseEntity<Interview> applyForInterview(@RequestBody InterviewRequest interviewRequest){
        return ResponseEntity.ok(interviewService.addInterviewData(interviewRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/apply-result")
    public String isGotJob(@RequestBody ResultUpdate resultUpdate){
        return interviewService.jobResponse(resultUpdate);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall/interview")
    public Map<String,InterviewResponse> getAll(){
        return interviewService.getAllInterviewByResponse();
    }

    @GetMapping(value = "/my-status",params = {"userName"})
    public InterviewResponse getInterviewResponseByUserName(@RequestParam String userName){
        return interviewService.getInterviewResponseByUserName(userName);
    }
}
