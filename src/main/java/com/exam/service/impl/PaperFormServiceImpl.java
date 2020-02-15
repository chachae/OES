package com.exam.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.entity.Paper;
import com.exam.entity.PaperForm;
import com.exam.exception.ServiceException;
import com.exam.mapper.PaperFormMapper;
import com.exam.mapper.PaperMapper;
import com.exam.service.PaperFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 试卷模板服务实现类
 *
 * @author yzn
 * @since 2020-02-14 17:51:18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PaperFormServiceImpl extends ServiceImpl<PaperFormMapper, PaperForm>
    implements PaperFormService {

  @Resource private PaperMapper paperMapper;
  @Resource private PaperFormMapper paperFormMapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean removeById(Serializable id) {
    PaperForm form = this.paperFormMapper.selectById(id);
    if (form == null) {
      throw new ServiceException("试卷模版不存在!");
    }
    // 查找是否有正在使用该模版的试卷，如果有，则不允许删除模版
    QueryWrapper<Paper> qw = new QueryWrapper<>();
    qw.lambda().eq(Paper::getPaperFormId, id);
    List<Paper> papers = this.paperMapper.selectList(qw);
    if (CollUtil.isNotEmpty(papers)) {
      throw new ServiceException("试卷模版正在使用，不能删除该模版！");
    }
    return super.removeById(id);
  }
}