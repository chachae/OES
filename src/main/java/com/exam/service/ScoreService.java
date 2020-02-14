package com.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.entity.Score;
import com.exam.entity.dto.AnswerEditDto;

import java.util.List;
import java.util.Map;

/**
 * 分数业务接口
 *
 * @author yzn
 * @since 2020-02-06 22:53:25
 */
public interface ScoreService extends IService<Score> {

  /**
   * 通过协商 ID 查询分数 List 集合
   *
   * @param id 学生 ID
   * @return 分数 List 集合
   */
  List<Score> selectByStuId(Integer id);

  /**
   * 根据课程统计成绩信息
   *
   * @param courseId 课程ID
   * @return 课程统计成绩 List 集合
   */
  List<Map<String, Object>> countByCourseId(Integer courseId);

  /**
   * 统计该学生本学期所有课程的平均成绩
   *
   * @param id 学生ID
   * @return 本学期所有课程的平均成绩 List 集合
   */
  List<Map<String, Object>> averageScore(Integer id);

  /**
   * 根据分数段统计该学生的课程门数和具体课程
   *
   * @param studentId 学生ID
   * @return 课程门数和具体课程 List 集合
   */
  List<Map<String, Object>> countByLevel(Integer studentId);

  /**
   * 根据学生ID和试卷ID修改成绩
   *
   * @param dto 修改的信息
   */
  void updateScoreByStuIdAndPaperId(AnswerEditDto dto);

  /**
   * 通过学生ID和试卷ID查询成绩详情
   *
   * @param stuId 学生ID
   * @param paperId 试卷ID
   * @return 成绩详情
   */
  Score selectByStuIdAndPaperId(Integer stuId, Integer paperId);
}
