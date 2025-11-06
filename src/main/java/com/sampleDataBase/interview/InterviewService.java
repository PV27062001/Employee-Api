package com.sampleDataBase.interview;

import com.sampleDataBase.department.Department;
import com.sampleDataBase.department.DepartmentName;
import com.sampleDataBase.department.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final DepartmentService departmentService;

    public Interview addInterviewData(InterviewRequest interviewRequest){
        List<Department> getMatchedDepartment = interviewRequest.getDepartmentName()
                .stream()
                .map(this::getDepartmentByName)
                .toList();

       Interview interview = Interview.builder()
                .userName(interviewRequest.getUserName())
                .isAppliedAny(true)
               .isGotOffer(false)
                .department(getMatchedDepartment)
                .build();
       return interviewRepository.save(interview);
    }

    private Department getDepartmentByName(String departmentName) {
        return departmentService.getDepartmentByName(departmentName);
    }


//    public String jobResponse(ResultUpdate resultUpdate) {
//
//    }
}
