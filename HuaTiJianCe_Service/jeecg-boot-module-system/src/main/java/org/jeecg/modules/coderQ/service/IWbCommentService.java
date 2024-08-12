package org.jeecg.modules.coderQ.service;

import org.jeecg.modules.coderQ.entity.WbComment;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: wb_comment
 * @Author: jeecg-boot
 * @Date:   2023-05-13
 * @Version: V1.0
 */
public interface IWbCommentService extends IService<WbComment> {

	public List<WbComment> selectByMainId(String mainId);
}
