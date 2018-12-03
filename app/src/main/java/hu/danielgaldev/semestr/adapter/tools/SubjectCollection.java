package hu.danielgaldev.semestr.adapter.tools;

import java.util.HashMap;
import java.util.List;

import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;
import hu.danielgaldev.semestr.model.pojo.Subject;

public class SubjectCollection {

    private HashMap<Subject, List<Requirement>> subReqMap;
    private List<RequirementType> reqTypeList;

    public SubjectCollection(HashMap<Subject, List<Requirement>> subReqMap, List<RequirementType> reqTypeList) {
        this.subReqMap = subReqMap;
        this.reqTypeList = reqTypeList;
    }

    public HashMap<Subject, List<Requirement>> getSubReqMap() {
        return subReqMap;
    }

    public List<RequirementType> getReqTypeList() {
        return reqTypeList;
    }
}
