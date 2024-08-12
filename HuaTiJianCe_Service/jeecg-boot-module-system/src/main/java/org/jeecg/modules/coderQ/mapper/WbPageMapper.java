package org.jeecg.modules.coderQ.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.coderQ.entity.TsDto;
import org.jeecg.modules.coderQ.entity.WbPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: wb_page
 * @Author: jeecg-boot
 * @Date:   2023-05-13
 * @Version: V1.0
 */
public interface WbPageMapper extends BaseMapper<WbPage> {

    List<TsDto> getTsChart();

}
