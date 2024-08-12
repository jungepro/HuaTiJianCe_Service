package org.jeecg.modules.coderQ.service.impl;

import org.jeecg.modules.coderQ.entity.WbPage;
import org.jeecg.modules.coderQ.entity.WbComment;
import org.jeecg.modules.coderQ.mapper.WbCommentMapper;
import org.jeecg.modules.coderQ.mapper.WbPageMapper;
import org.jeecg.modules.coderQ.service.IWbPageService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: wb_page
 * @Author: jeecg-boot
 * @Date:   2023-05-13
 * @Version: V1.0
 */
@Service
public class WbPageServiceImpl extends ServiceImpl<WbPageMapper, WbPage> implements IWbPageService {

	@Autowired
	private WbPageMapper wbPageMapper;
	@Autowired
	private WbCommentMapper wbCommentMapper;
	
	@Override
	@Transactional
	public void delMain(String id) {
		wbCommentMapper.deleteByMainId(id);
		wbPageMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			wbCommentMapper.deleteByMainId(id.toString());
			wbPageMapper.deleteById(id);
		}
	}
	
}
