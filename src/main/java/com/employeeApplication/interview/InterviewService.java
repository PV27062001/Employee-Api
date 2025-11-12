package com.employeeApplication.interview;

import com.employeeApplication.auth.UserDetailService;
import com.employeeApplication.department.Department;
import com.employeeApplication.department.DepartmentService;
import com.employeeApplication.employee.EmployeeService;
import com.employeeApplication.exception.EmployeeException;
import com.employeeApplication.exception.UserNameAuthenticationException;
import com.employeeApplication.interview.interviewdepartmentstatus.InterviewDepartmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final DepartmentService departmentService;
    private final UserDetailService userDetailService;
    private final EmployeeService employeeService;

    public Interview addInterviewData(InterviewRequest interviewRequest) throws UserNameAuthenticationException {

        String name = interviewRequest.getUserName();
        if(employeeService.isEmployeeAlreadyPresent(name)){
            throw new EmployeeException("This user Already have a job");
        }
        String userName = userDetailService.isUserNamePresent(name);
        Interview interview = Interview.builder()
                .userName(userName)
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
        return Optional.ofNullable(interviewRepository.findInterviewByName(userName));
    }

    public InterviewResponse getInterviewResponseByUserName(String userName){

        Interview interview =  getInterviewByUserName(userName)
                .orElseThrow(() -> new NoSuchElementException("No interview found for the given userName"));

        Map<String,String> resultMap = new HashMap<>();
        for(int i = 0;i< interview.getDepartmentStatuses().size();i++){
            resultMap.put(interview.getDepartmentStatuses().get(i).getDepartment().getDepartmentName().name(),interview.getDepartmentStatuses().get(i).getStatus().name());
        }
       return buildInterviewResponse(interview, resultMap);
    }

    private static InterviewResponse buildInterviewResponse(Interview interview, Map<String, String> resultMap) {
        return InterviewResponse.builder()
                .userName(interview.getUserName())
                .appliedDate(interview.getCreatedAt())
                .statusByDepartment(resultMap)
                .build();
    }

    public Map<String,InterviewResponse> getAllInterviewByResponse(){
        List<Interview> findAll = getAll();
        Map<String,InterviewResponse> result = new HashMap<>();

        Map<String,String> resultMap = new HashMap<>();

        for(Interview interview: findAll){
            for(int i = 0;i< interview.getDepartmentStatuses().size();i++){
                resultMap.put(interview.getDepartmentStatuses().get(i).getDepartment().getDepartmentName().name(),interview.getDepartmentStatuses().get(i).getStatus().name());
            }
            result.put(interview.getUserName(),buildInterviewResponse(interview,resultMap));
        }

        return result;
    }

    @Transactional
    public String jobResponse(ResultUpdate resultUpdate) {
        Interview interview = getInterviewByUserName(resultUpdate.getUserName()).orElseThrow(() -> new NoSuchElementException("No Interview object found for the userName"));

        for(Map.Entry<String, String> department: resultUpdate.getStatusByDepartment().entrySet()){
            String departmentName = department.getKey();
            String isSelected = department.getValue();

            interview.getDepartmentStatuses().stream()
                    .filter(status -> status.getDepartment()
                            .getDepartmentName()
                            .name().equalsIgnoreCase(departmentName))
                    .findFirst()
                    .ifPresent(interviewDepartmentStatus -> {
                        if (isSelected.equals(("ACCEPTED"))) {
                            interviewDepartmentStatus.setStatus(Status.ACCEPTED);
                            employeeService.saveEmployee(resultUpdate);
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
