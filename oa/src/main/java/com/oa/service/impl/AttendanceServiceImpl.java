package com.oa.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oa.dao.AttendanceDao;
import com.oa.dao.DepartmentDao;
import com.oa.dao.EmployeeDao;
import com.oa.pojo.Attendance;
import com.oa.pojo.Employee;
import com.oa.service.AttendanceService;
import com.oa.vo.AttendanceVO;

/**
 * @author wl
 * @category 鎵撳崱涓氬姟閫昏緫瀹炵幇绫?
 *
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {
	@Autowired
	private AttendanceDao attendanceDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private DepartmentDao departmentDao;
	

	/**
	 * 灏哖O瀵硅薄闆嗗悎杞崲鎴怴O瀵硅薄闆嗗悎
	 * 
	 * @param attendanceVOs
	 *            VO瀵硅薄闆嗗悎
	 * @param attendances
	 *            PO瀵硅薄闆嗗悎
	 */
	public void change(List<AttendanceVO> attendanceVOs, List<Attendance> attendances) {
		for (Attendance attendance : attendances) {
			Employee employee = employeeDao.findById(attendance.getEid());
			String ename = employee.getEname();
			String dept = departmentDao.findById(employee.getBid()).getDname();
			AttendanceVO attendanceVO = new AttendanceVO(attendance.getId(), attendance.getEid(), ename, dept,
					attendance.getDate());
			attendanceVOs.add(attendanceVO);
		}
	}

	@Override
	public List<AttendanceVO> findAll() {
		List<AttendanceVO> attendanceVOs = new ArrayList<AttendanceVO>();
		change(attendanceVOs, attendanceDao.findAll());
		return attendanceVOs;
	}

	@Override
	public List<AttendanceVO> findlist(int eid) {
		List<AttendanceVO> attendanceVOs = new ArrayList<AttendanceVO>();
		change(attendanceVOs, attendanceDao.findlist(eid));
		return attendanceVOs;
	}

	@Override
	public boolean add(Attendance attendance) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_str = dateFormat.format(date);
		attendance.setDate(date_str);
		return attendanceDao.add(attendance) > 0 ? true : false;
	}

	@Override
	public boolean update(Attendance attendance) {
		return attendanceDao.update(attendance) > 0 ? true : false;
	}

	@Override
	public boolean delete(int[] ids) {
		return attendanceDao.delete(ids) > 0 ? true : false;
	}

}
