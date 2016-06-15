package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylveryte on 14/6/16.
 */
public class EmployeeLab implements LabObeservable {

    private static EmployeeLab sEmployeeLab;

    private List<Employee> mEmployees;
    private List<LabObserver> mLabObservers;

    private EmployeeLab()
    {

        mEmployees=new ArrayList<>();
        mLabObservers=new ArrayList<>();

        //// TODO: 14/6/16 clean up
        for (int i=0; i<4;i++)
        {
            Employee employee=new Employee();
            employee.setName("Rext "+i*234);
            employee.setAge(i+12*i);
            employee.setActive(i % 2 == 0);
            Designation designation=new Designation();
            designation.setTitle(i*324+"slave");
            employee.addDesignation(designation);
            employee.addSite(SitesLab.getInstanceOf().getSites().get(i));
            addEmployee(employee);
        }

    }

    public static EmployeeLab getInstanceOf()
    {
        if (sEmployeeLab==null)
        {
            sEmployeeLab=new EmployeeLab();
        }
        return sEmployeeLab;
    }

    public void addEmployee(Employee employee)
    {
        mEmployees.add(employee);
    }

    public List<Employee> getEmployees()
    {
        return mEmployees;
    }

    public void addListener(LabObserver labObserver)
    {
        mLabObservers.add(labObserver);
    }

    public void alertAllObservers()
    {
        for (LabObserver labObserver :mLabObservers)
            labObserver.update();
    }

}
