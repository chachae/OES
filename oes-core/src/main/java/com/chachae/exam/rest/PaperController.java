package com.chachae.exam.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chachae.exam.common.base.R;
import com.chachae.exam.common.constant.SysConsts.Session;
import com.chachae.exam.common.model.Paper;
import com.chachae.exam.common.model.StuAnswerRecord;
import com.chachae.exam.common.model.Teacher;
import com.chachae.exam.common.model.dto.AnswerEditDto;
import com.chachae.exam.common.model.dto.ImportPaperDto;
import com.chachae.exam.common.model.dto.ImportPaperRandomQuestionDto;
import com.chachae.exam.common.model.dto.PaperQuestionUpdateDto;
import com.chachae.exam.common.model.dto.QueryPaperDto;
import com.chachae.exam.common.util.HttpUtil;
import com.chachae.exam.core.annotation.Limit;
import com.chachae.exam.core.annotation.Permissions;
import com.chachae.exam.service.PaperService;
import com.chachae.exam.service.QuestionService;
import com.chachae.exam.service.ScoreService;
import com.chachae.exam.service.StuAnswerRecordService;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chachae
 * @since 2020/2/29 12:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/paper")
public class PaperController {

  private final ScoreService scoreService;
  private final StuAnswerRecordService stuAnswerRecordService;
  private final QuestionService questionService;
  private final PaperService paperService;

  @PostMapping("/update")
  @Permissions("paper:update")
  public R updateTime(Paper paper) {
    this.paperService.updateById(paper);
    return R.success();
  }

  @PostMapping("/update/gradeIds")
  public R updateGradeIds(Paper paper) {
    this.paperService.updateGradeIds(paper);
    return R.success();
  }

  @GetMapping("/{id}")
  @Permissions("paper:list")
  public Paper getOne(@PathVariable Integer id) {
    return this.paperService.getById(id);
  }

  @GetMapping("/list")
  @Permissions("paper:list")
  @Limit(key = "paperList", period = 5, count = 15, name = "试卷查询接口", prefix = "limit")
  public Map<String, Object> pagePaper(Page<Paper> page, QueryPaperDto entity) {
    return this.paperService.pagePaper(page, entity);
  }

  @PostMapping("/import/excel")
  @Permissions("paper:import")
  public R excel(@RequestParam("file") MultipartFile multipartFile) {
    // 导入试卷
    ImportPaperDto dto = this.questionService.importPaper(multipartFile);
    return R.successWithData(dto);
  }

  @PostMapping("/save/import")
  @Permissions("paper:save")
  public R newPaperByExcel(Paper paper, ImportPaperRandomQuestionDto entity) {
    // 获取教师 ID
    Teacher teacher = (Teacher) HttpUtil.getAttribute(Session.TEACHER);
    // 设置出卷老师
    paper.setTeacherId(teacher.getId());
    // 设置试卷学院
    paper.setAcademyId(teacher.getAcademyId());
    // 局部随机参数判断，没有局部随机参数则调用普通的插入接口
    boolean[] res = {entity.getA() == 0, entity.getB() == 0, entity.getC() == 0, entity.getD() == 0,
        entity.getE() == 0, entity.getF() == 0};
    for (boolean e : res) {
      if (!e) {
        this.paperService.saveWithImportPaper(paper, entity);
      }
    }
    this.paperService.save(paper);
    return R.successWithData(paper.getId());
  }

  /**
   * 提交组卷信息
   *
   * @param paper       试卷信息
   * @param paperFormId 试卷模板ID
   * @return 组卷页面
   */
  @PostMapping("/save/random")
  @Permissions("paper:save")
  public R add(@Valid Paper paper, Integer paperFormId, String difficulty) {
    // 设置试卷模板 ID
    paper.setPaperFormId(paperFormId);
    // 获取教师 ID
    Teacher teacher = (Teacher) HttpUtil.getAttribute(Session.TEACHER);
    // 设置出卷老师
    paper.setTeacherId(teacher.getId());
    // 设置试卷学院
    paper.setAcademyId(teacher.getAcademyId());
    // 带指定难度的接口
    paperService.randomNewPaper(paper, difficulty);
    return R.successWithData(paper.getId());
  }

  /**
   * 修改主观题成绩
   *
   * @param dto 信息
   */
  @PostMapping("/update/score")
  @Permissions("paper:update")
  public R editScore(@Valid AnswerEditDto dto) {
    // 修改该题得分
    StuAnswerRecord record = new StuAnswerRecord();
    record.setId(dto.getId()).setScore(dto.getNewScore());
    this.stuAnswerRecordService.updateById(record);
    // 修改总分
    StuAnswerRecord stuRec = this.stuAnswerRecordService.getById(dto.getId());
    // 封装参数
    dto.setStuId(stuRec.getStuId());
    dto.setPaperId(stuRec.getPaperId());
    this.scoreService.updateScoreByStuIdAndPaperId(dto);
    return R.success();
  }

  /**
   * 级联删除试卷、分数、答案记录
   *
   * @param id 试卷ID
   * @return 试卷页面
   */
  @PostMapping("/delete/{id}")
  @Permissions("paper:delete")
  public R delPaper(@PathVariable Integer id) {
    // 级联删除试卷（详见接口实现类）
    paperService.deletePaperById(id);
    return R.success();
  }

  /**
   * 修改試卷题目
   *
   * @param dto 修改的信息
   * @return 回调信息
   */
  @PostMapping("/update/question")
  @Permissions("paper:update")
  public R updateQuestionId(PaperQuestionUpdateDto dto) {
    this.paperService.updateQuestionId(dto);
    return R.success();
  }

  @GetMapping("/chart/analysis")
  public void outputAnalysis(Integer paperId, Integer gradeId, HttpServletResponse response) {
    this.scoreService.outputPaperChartExcel(paperId, gradeId, response);
  }

  @GetMapping("/export/{paperId}")
  public void outputPaper(@PathVariable Integer paperId, HttpServletResponse response) {
    this.paperService.outputPaperExcel(paperId, response);
  }
}
