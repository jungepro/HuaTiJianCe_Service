package org.jeecg.modules.coderQ.service.impl;

import org.jeecg.modules.coderQ.entity.WbComment;
import org.jeecg.modules.coderQ.mapper.WbCommentMapper;
import org.jeecg.modules.coderQ.service.IWbCommentService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: wb_comment
 * @Author: jeecg-boot
 * @Date:   2023-05-13
 * @Version: V1.0
 */
@Service
public class WbCommentServiceImpl extends ServiceImpl<WbCommentMapper, WbComment> implements IWbCommentService {
	
	@Autowired
	private WbCommentMapper wbCommentMapper;
	
	@Override
	public List<WbComment> selectByMainId(String mainId) {
		return wbCommentMapper.selectByMainId(mainId);
	}
}
