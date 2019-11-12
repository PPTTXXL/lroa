package com.oa.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.html.Result;
import com.oa.pojo.Employee;
import com.oa.service.EmployeeService;
import com.oa.vo.EmployeeVO;

/**
 * @author xxl
 * @category 人力资源控制器
 */

@Controller
@RequestMapping("/renLi")
public class HumanResourcesController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 查询所有员工的人事档案
	 * 
	 * @return List<EmployeeVO> 所有员工的信息
	 */
	@RequestMapping("/showPersonnelFiles")
	public @ResponseBody List<EmployeeVO> showPersonnelFiles(HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<EmployeeVO> employeeVOs = employeeService.findUserList();
		return employeeVOs;
	}

	/**
	 * 查询出当前要修改信息的员工
	 * 
	 * @param eid
	 * @return
	 */
	@RequestMapping("/toUpdateEmployee")
	public @ResponseBody EmployeeVO toUpdateEmployee(Integer eid) {
		EmployeeVO employeeVO = employeeService.findById(eid);
		return employeeVO;
	}

	/**
	 * 更该员工信息
	 * 
	 * @param employee
	 * @return
	 */
	@RequestMapping("/updateEmployee")
	public @ResponseBody Result updateEmployee(Employee employee, HttpServletResponse response) {
		Boolean flag = employeeService.update(employee);
		Result result = new Result();
		if (flag) {
			result.setResult("更新成功！");
		} else {
			result.setResult("更新失败！");
		}
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}

	/**
	 * 添加员工
	 * 
	 * @param employee
	 * @return
	 */
	@RequestMapping("/addEmployee")
	public @ResponseBody Result addEmployee(Employee employee, HttpServletResponse response) {
		Boolean flag = employeeService.add(employee);
		Result result = new Result();
		if (flag) {
			result.setResult("添加成功！");
		} else {
			result.setResult("添加失败！");
		}
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}

	/**
	 * 删除员工
	 * 
	 * @param eid
	 * @return
	 */
	@RequestMapping("/deleteEmployee")
	public @ResponseBody Result deleteEmployee(Integer eid, HttpServletResponse response) {
		Boolean flag = employeeService.delete(eid);
		Result result = new Result();
		if (flag) {
			result.setResult("删除成功！");
		} else {
			result.setResult("删除失败！");
		}
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}

}
