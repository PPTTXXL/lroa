package com.oa.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oa.dao.DailyDao;
import com.oa.dao.DepartmentDao;
import com.oa.dao.EmployeeDao;
import com.oa.pojo.Daily;
import com.oa.pojo.Employee;
import com.oa.service.DailyService;
import com.oa.vo.DailyVO;

/**
 * @author song
 * @category 涓汉鏃ユ姤涓氬姟閫昏緫瀹炵幇绫?
 *
 */
@Service
public class DailyServiceImpl implements DailyService {
	@Autowired
	private DailyDao idailyDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private DepartmentDao departmentDao;
	

	/**
	 * 鎶婁竴涓狣aily杞寲涓篋ailyVO
	 * 
	 * @param list_vo
	 * @param list
	 */
	public List<DailyVO> infoDaily(List<DailyVO> list_vo, List<Daily> list) {
		for (Daily daily : list) {

			Employee employee = employeeDao.findById(daily.getEid());
			String ename = employee.getEname();
			String bname = departmentDao.findById(employee.getBid()).getDname();
			DailyVO dailyVO = new DailyVO(daily.getDid(), daily.getEid(), ename, bname, daily.getWdef(),
					daily.getWdate());
			list_vo.add(dailyVO);
		}
		return list_vo;
	}

	/**
	 * po瀵硅薄杞崲鎴恦o瀵硅薄
	 * 
	 * @param daily
	 *            po瀵硅薄
	 * @return
	 */
	public DailyVO changePV(Daily daily) {
		String ename = employeeDao.findById(daily.getEid()).getEname();
		String bname = departmentDao.findById(employeeDao.findById(daily.getEid()).getBid()).getDname();
		DailyVO dailyVO = new DailyVO(daily.getDid(), daily.getEid(), ename, bname, daily.getWdef(), daily.getWdate());
		return dailyVO;
	}

	/**
	 * vo瀵硅薄杞崲鎴恜o瀵硅薄
	 * 
	 * @param dailyVO
	 *            vo瀵硅薄
	 * @return
	 */
	public Daily changeVP(DailyVO dailyVO) {
		Daily daily = new Daily(dailyVO.getDid(), dailyVO.getEid(), dailyVO.getWdef(), dailyVO.getWdate());
		return daily;
	}

	@Override
	public boolean add(Daily daily) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String wdate = dateFormat.format(date);
		daily.setWdate(wdate);
		return idailyDao.add(daily) > 0 ? true : false;
	}

	@Override
	public boolean delete(int eid) {
		return idailyDao.delete(eid) > 0 ? true : false;
	}

	public boolean update(DailyVO dailyVO) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String wdate = dateFormat.format(date);
		dailyVO.setWdate(wdate);
		return idailyDao.update(changeVP(dailyVO)) > 0 ? true : false;
	}

	@Override
	public List<DailyVO> findAll() {
		List<DailyVO> list_vo = new ArrayList<DailyVO>();
		infoDaily(list_vo, idailyDao.findAll());
		return list_vo;
	}

	@Override
	public List<DailyVO> findById(int eid) {
		List<DailyVO> list_vo = new ArrayList<DailyVO>();
		infoDaily(list_vo, idailyDao.findAll());
		return list_vo;
	}

	@Override
	public DailyVO findByDailyId(int did) {
		return changePV(idailyDao.findByDailyId(did));
	}

	@Override
	public List<DailyVO> findByDepId(int id) {
		List<DailyVO> list_vo = new ArrayList<DailyVO>();
		infoDaily(list_vo, idailyDao.findByDepId(id));
		return list_vo;
	}

	@Override
	public List<DailyVO> findByListDate(String date) {
		List<DailyVO> list_vo = new ArrayList<DailyVO>();
		infoDaily(list_vo, idailyDao.findByListDate(date));
		return list_vo;
	}

	@Override
	public List<DailyVO> findDateOne(String wdate, int eid) {
		List<DailyVO> list_vo = new ArrayList<DailyVO>();
		infoDaily(list_vo, idailyDao.findDateOne(wdate, eid));
		return list_vo;
	}

	@Override
	public boolean deleteById(int did) {
		return idailyDao.deleteById(did) > 0 ? true : false;
	}

}
