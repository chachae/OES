package com.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.Teacher;
import com.exam.entity.dto.ChangePassDto;
import com.github.pagehelper.PageInfo;

/**
 * 教师业务接口
 *
 * @author yzn
 * @date 2020/2/5
 */
public interface TeacherService extends IService<Teacher> {

  /**
   * 教师登录
   *
   * @param teacherId 教师ID
   * @param teacherPassword 教师密码
   * @return 教师信息
   */
  Teacher login(String teacherId, String teacherPassword);

  /**
   * 修改密码
   *
   * @param id 教师ID
   * @param dto 密码信息
   */
  void updatePassword(Integer id, ChangePassDto dto);

  /**
   * 分页查询教师列表
   *
   * @param pageNo 当前页
   * @return 教师数据分页结果集
   */
  PageInfo<Teacher> pageForTeacherList(Integer pageNo);
}
