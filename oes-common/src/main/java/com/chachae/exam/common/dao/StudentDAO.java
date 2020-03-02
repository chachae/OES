package com.chachae.exam.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chachae.exam.common.model.Student;
import com.chachae.exam.common.model.dto.StudentQueryDto;
import com.chachae.exam.common.model.vo.StudentVo;

import java.util.List;

/**
 * 学生 Mapper 接口
 *
 * @author chachae
 * @date 2020/1/6
 */
public interface StudentDAO extends BaseMapper<Student> {

  /**
   * 通过条件查询学生 List 集合
   *
   * @param dto 学生查询条件
   * @return 学生 List 集合
   */
  List<StudentVo> listVo(StudentQueryDto dto);

  /**
   * 通过 ID 查询学生详细信息
   *
   * @param id 学生 ID
   * @return 学生信息
   */
  StudentVo selectVoById(Integer id);
}