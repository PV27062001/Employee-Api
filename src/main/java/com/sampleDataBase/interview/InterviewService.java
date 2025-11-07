package com.sampleDataBase.interview;

import com.sampleDataBase.department.Department;
import com.sampleDataBase.department.DepartmentService;
import com.sampleDataBase.interview.interviewdepartmentstatus.InterviewDepartmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final DepartmentService departmentService;

    public Interview addInterviewData(InterviewRequest interviewRequest) {
        Interview interview = Interview.builder()
                .userName(interviewRequest.getUserName())
                .isAppliedAny(true)
                .build();

        List<InterviewDepartmentStatus> statuses = interviewRequest.getDepartmentName()
                .stream()
                .map(deptName -> {
                    Department dept = getDepartmentByName(deptName);
                    return InterviewDepartmentStatus.builder()
                            .interview(interview)
                            .department(dept)
                            .status(Status.PENDING)
                            .build();
                })
                .toList();

        interview.setDepartmentStatuses(statuses);
        return interviewRepository.save(interview);
    }


    private Department getDepartmentByName(String departmentName) {
        return departmentService.getDepartmentByName(departmentName);
    }

    public Optional<Interview> getInterviewByUserName(String userName){
        return Optional.of(interviewRepository.findInterviewByName(userName));
    }

    public String jobResponse(ResultUpdate resultUpdate) {
        Interview interview = getInterviewByUserName(resultUpdate.getUserName()).orElseThrow(() -> new NoSuchElementException("No Interview object found for the userName"));

        for(Map.Entry<String, Boolean> department: resultUpdate.getUpdate().entrySet()){
            String departmentName = department.getKey();
            boolean isSelected = department.getValue();

            interview.getDepartmentStatuses().stream()
                    .filter(status -> status.getDepartment()
                            .getDepartmentName()
                            .name().equalsIgnoreCase(departmentName))

                    .findFirst()
                    .ifPresent(interviewDepartmentStatus -> {
                        if (isSelected) {
                            interviewDepartmentStatus.setStatus(Status.ACCEPTED);
                        } else {
                            interviewDepartmentStatus.setStatus(Status.REJECTED);
                        }
                    });
        }
        interviewRepository.save(interview);
        return "status updated successfully";
    }

    public List<Interview> getAll() {
        return interviewRepository.findAll();
    }
}
