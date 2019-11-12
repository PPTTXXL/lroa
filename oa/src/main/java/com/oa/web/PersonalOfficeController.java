package com.oa.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.html.Result;
import com.oa.pojo.Daily;
import com.oa.pojo.Task;
import com.oa.service.DailyService;
import com.oa.service.EmployeeService;
import com.oa.service.TaskService;
import com.oa.vo.DailyVO;
import com.oa.vo.EmployeeVO;
import com.oa.vo.TaskVO;

/**
 * @author xxl
 * @category 个人办公控制器
 */

@Controller
@RequestMapping("/banGong")
public class PersonalOfficeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private DailyService dailyService;

	/**
	 * 个人日报：展示个人日报，如果当前登录员工为普通员工，则能看到自己个人日报，若为经理则能看到本部门所有员工的个人日报
	 * 
	 * @return List<Daily> 个人日报
	 */
	@RequestMapping("/showPersonalJournal")
	public @ResponseBody List<DailyVO> showPersonalJournal(HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		int bid = employee.getBid();
		if (employee.getZid() == 1) {
			List<DailyVO> dailys = dailyService.findByDepId(bid);
			return dailys;
		} else {
			List<DailyVO> dailys = dailyService.findById(employee.getEid());
			return dailys;
		}
	}

	/**
	 * 按时间展示个人日报，如果当前登录员工为普通员工，则能看到自己某天的个人日报，若为经理则能看到本部门所有员工某天个人日报
	 * 
	 * @param date
	 *            日期（格式为“yyyy-MM-dd”）
	 * @return 根据日期查询当日所有日报信息
	 */
	@RequestMapping("/listDailyVO")
	public @ResponseBody List<DailyVO> listDailyVO(String date, HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		if (employee.getZid() == 1) {
			List<DailyVO> dailys = dailyService.findByListDate(date);
			return dailys;
		} else {
			List<DailyVO> dailys = dailyService.findDateOne(date, employee.getEid());
			return dailys;
		}

	}

	/**
	 * 个人日报：创建个人日报
	 * 
	 * @return 创建结果（是否创建成功）
	 */
	@RequestMapping("/createPersonalJournal")
	public @ResponseBody Result createPersonalJournal(Daily daily, HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		// 从session中获取当前登录员工的信息
		EmployeeVO employeeVO = (EmployeeVO) session.getAttribute("employeeVO");
		daily.setEid(employeeVO.getEid());
		Boolean flag = dailyService.add(daily);
		if (flag) {
			result.setResult("创建成功！");
		} else {
			result.setResult("创建失败！");
		}
		return result;
	}

	/**
	 * 个人日报：删除个人日报
	 * 
	 * @param did
	 *            日志编号
	 * @param session
	 * @param response
	 * @return 删除结果（是否删除成功）
	 */
	@RequestMapping("/deletePersonalJournal")
	public @ResponseBody Result deletePersonalJournal(Integer did, HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		// 从session中获取当前登录员工的信息
		EmployeeVO employeeVO = (EmployeeVO) session.getAttribute("employeeVO");
		DailyVO dailyVO = dailyService.findByDailyId(did);
		if (employeeVO.getEid() == dailyVO.getEid()) {
			Boolean flag = dailyService.deleteById(did);
			if (flag) {
				result.setResult("删除成功！");
			} else {
				result.setResult("创建失败！");
			}
		}
		return result;
	}

	/**
	 * 个人日报：更新个人日报
	 * 
	 * @param did
	 *            日志编号
	 * @param session
	 * @param response
	 * @return 更新结果（是否更新成功）
	 */
	@RequestMapping("/updatePersonalJournal")
	public @ResponseBody Result updatePersonalJournal(Integer did, String wdef, HttpSession session,
			HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		// 从session中获取当前登录员工的信息
		EmployeeVO employeeVO = (EmployeeVO) session.getAttribute("employeeVO");
		DailyVO dailyVO = dailyService.findByDailyId(did);
		if (employeeVO.getEid() == dailyVO.getEid()) {
			dailyVO.setWdef(wdef);
			Boolean flag = dailyService.update(dailyVO);
			if (flag) {
				result.setResult("更新成功！");
			} else {
				result.setResult("更新失败！");
			}
		}
		return result;
	}

	/**
	 * 任务管理：展示个人任务，如果当前登录员工为普通员工，则能看到自己个人任务，若为经理则能看到本部门所有员工的个人任务
	 * 
	 * @return
	 */
	@RequestMapping("/showTask")
	public @ResponseBody List<TaskVO> showTaskByEid(Integer eid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<TaskVO> taskVOs = null;
		EmployeeVO employee = employeeService.findById(eid);
		int bid = employee.getBid();
		if (employee.getZid() == 1) {
			taskVOs = taskService.findByBid(bid);
		} else {
			taskVOs = taskService.findByEid(employee.getEid());
		}
		return taskVOs;
	}

	/**
	 * 创建个人任务
	 * 
	 * @param task
	 * @return
	 */
	@RequestMapping("/addTask")
	public @ResponseBody Result addTask(Task task, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		EmployeeVO employee = employeeService.findById(task.getEid());
		if (employee.getZid() == 1) {
			task.setEid(employee.getEid());
			Boolean flag = taskService.add(task);
			if (flag) {
				result.setResult("创建成功！");
			} else {
				result.setResult("创建失败！");
			}
		}
		return result;
	}

	/**
	 * 删除个人任务
	 * 
	 * @param tid
	 * @return
	 */
	@RequestMapping("/delTask")
	public @ResponseBody Result delTask(Integer tid, Integer eid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		EmployeeVO employee = employeeService.findById(eid);
		if (employee.getZid() == 1) {
			Boolean flag = taskService.delete(tid);
			if (flag) {
				result.setResult("删除成功！");
			} else {
				result.setResult("删除失败！");
			}
		}
		return result;
	}

	/**
	 * 更新个人任务
	 * 
	 * @param task
	 * @return
	 */
	@RequestMapping("/upTask")
	public @ResponseBody Result upTask(Integer tid, String tdef, Integer eid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		EmployeeVO employee = employeeService.findById(eid);
		TaskVO taskVO = taskService.findByTid(tid);
		if (employee.getZid() == 1) {
			taskVO.setTdef(tdef);
			Boolean flag = taskService.update(taskVO);
			if (flag) {
				result.setResult("更新成功！");
			} else {
				result.setResult("更新失败！");
			}
		}
		return result;
	}

	/**
	 * 通讯录
	 * 
	 * @return List<EmployeeVO> 所有员工
	 */
	@RequestMapping("/addressBook")
	public @ResponseBody List<EmployeeVO> addressBook(HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<EmployeeVO> list = employeeService.findUserList();
		return list;
	}

}
