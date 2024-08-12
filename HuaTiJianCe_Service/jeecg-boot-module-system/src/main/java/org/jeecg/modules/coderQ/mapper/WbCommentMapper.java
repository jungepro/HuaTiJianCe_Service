package org.jeecg.modules.coderQ.mapper;

import java.util.List;
import org.jeecg.modules.coderQ.entity.WbComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: wb_comment
 * @Author: jeecg-boot
 * @Date:   2023-05-13
 * @Version: V1.0
 */
public interface WbCommentMapper extends BaseMapper<WbComment> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<WbComment> selectByMainId(@Param("mainId") String mainId);

}
