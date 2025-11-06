package com.sampleDataBase.interview;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interview")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;

    @PostMapping("/apply-job")
    public ResponseEntity<Interview> applyForInterview(@RequestBody InterviewRequest interviewRequest){
        return ResponseEntity.ok(interviewService.addInterviewData(interviewRequest));
    }

//    @PutMapping("/apply-result")
//    public String isGotJob(@RequestParam ResultUpdate resultUpdate){
//        return interviewService.jobResponse(resultUpdate);
//    }
}
