package org.jeecg.modules.coderQ.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.coderQ.entity.TsDto;
import org.jeecg.modules.coderQ.mapper.WbPageMapper;
import org.jeecg.modules.coderQ.util.Comment;
import org.jeecg.modules.coderQ.util.SetCookie;
import org.jeecg.modules.coderQ.util.Spider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.coderQ.entity.WbComment;
import org.jeecg.modules.coderQ.entity.WbPage;
import org.jeecg.modules.coderQ.service.IWbPageService;
import org.jeecg.modules.coderQ.service.IWbCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: wb_page
 * @Author: jeecg-boot
 * @Date: 2023-05-13
 * @Version: V1.0
 */
@Api(tags = "wb_page")
@RestController
@RequestMapping("/coderQ/wbPage")
@Slf4j
public class WbPageController extends JeecgController<WbPage, IWbPageService> {

    @Autowired
    private IWbPageService wbPageService;

    @Autowired
    private IWbCommentService wbCommentService;


    /*---------------------------------主表处理-begin-------------------------------------*/

    /**
     * 分页列表查询
     *
     * @param wbPage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wb_page-分页列表查询")
    @ApiOperation(value = "wb_page-分页列表查询", notes = "wb_page-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WbPage wbPage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WbPage> queryWrapper = QueryGenerator.initQueryWrapper(wbPage, req.getParameterMap());
        Page<WbPage> page = new Page<WbPage>(pageNo, pageSize);
        IPage<WbPage> pageList = wbPageService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wbPage
     * @return
     */
    @AutoLog(value = "wb_page-添加")
    @ApiOperation(value = "wb_page-添加", notes = "wb_page-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WbPage wbPage) {
        wbPageService.save(wbPage);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wbPage
     * @return
     */
    @AutoLog(value = "wb_page-编辑")
    @ApiOperation(value = "wb_page-编辑", notes = "wb_page-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WbPage wbPage) {
        wbPageService.updateById(wbPage);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wb_page-通过id删除")
    @ApiOperation(value = "wb_page-通过id删除", notes = "wb_page-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wbPageService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wb_page-批量删除")
    @ApiOperation(value = "wb_page-批量删除", notes = "wb_page-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wbPageService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WbPage wbPage) {
        return super.exportXls(request, wbPage, WbPage.class, "wb_page");
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WbPage.class);
    }
    /*---------------------------------主表处理-end-------------------------------------*/


    /*--------------------------------子表处理-wb_comment-begin----------------------------------------------*/

    /**
     * 通过主表ID查询
     *
     * @return
     */
    @AutoLog(value = "wb_comment-通过主表ID查询")
    @ApiOperation(value = "wb_comment-通过主表ID查询", notes = "wb_comment-通过主表ID查询")
    @GetMapping(value = "/listWbCommentByMainId")
    public Result<?> listWbCommentByMainId(WbComment wbComment,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           HttpServletRequest req) {
        QueryWrapper<WbComment> queryWrapper = QueryGenerator.initQueryWrapper(wbComment, req.getParameterMap());
        Page<WbComment> page = new Page<WbComment>(pageNo, pageSize);
        IPage<WbComment> pageList = wbCommentService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wbComment
     * @return
     */
    @AutoLog(value = "wb_comment-添加")
    @ApiOperation(value = "wb_comment-添加", notes = "wb_comment-添加")
    @PostMapping(value = "/addWbComment")
    public Result<?> addWbComment(@RequestBody WbComment wbComment) {
        wbCommentService.save(wbComment);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wbComment
     * @return
     */
    @AutoLog(value = "wb_comment-编辑")
    @ApiOperation(value = "wb_comment-编辑", notes = "wb_comment-编辑")
    @PutMapping(value = "/editWbComment")
    public Result<?> editWbComment(@RequestBody WbComment wbComment) {
        wbCommentService.updateById(wbComment);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wb_comment-通过id删除")
    @ApiOperation(value = "wb_comment-通过id删除", notes = "wb_comment-通过id删除")
    @DeleteMapping(value = "/deleteWbComment")
    public Result<?> deleteWbComment(@RequestParam(name = "id", required = true) String id) {
        wbCommentService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wb_comment-批量删除")
    @ApiOperation(value = "wb_comment-批量删除", notes = "wb_comment-批量删除")
    @DeleteMapping(value = "/deleteBatchWbComment")
    public Result<?> deleteBatchWbComment(@RequestParam(name = "ids", required = true) String ids) {
        this.wbCommentService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportWbComment")
    public ModelAndView exportWbComment(HttpServletRequest request, WbComment wbComment) {
        // Step.1 组装查询条件
        QueryWrapper<WbComment> queryWrapper = QueryGenerator.initQueryWrapper(wbComment, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<WbComment> pageList = wbCommentService.list(queryWrapper);
        List<WbComment> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "wb_comment"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, WbComment.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("wb_comment报表", "导出人:" + sysUser.getRealname(), "wb_comment"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importWbComment/{mainId}")
    public Result<?> importWbComment(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<WbComment> list = ExcelImportUtil.importExcel(file.getInputStream(), WbComment.class, params);
                for (WbComment temp : list) {
                    temp.setPageid(mainId);
                }
                long start = System.currentTimeMillis();
                wbCommentService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-wb_comment-end----------------------------------------------*/

    @GetMapping("doPaQu")
    public Result<?> doPaQu(String id) {
        WbPage page = wbPageService.getById(id);
        page.setZt(1);
        wbPageService.updateById(page);
        (new Thread(() -> {
            try {
                System.out.println("数据爬取开始--------------------");
                /*-----------------------------------------------------------------------*/
                //     String cookie = "M_WEIBOCN_PARAMS=oid%3D4900264575766131%26luicode%3D10000011%26lfid%3D102803; expires=Sat, 13-May-2023 03:19:45 GMT; Max-Age=600; path=/; domain=.weibo.cn; HttpOnly";
                String cookie = SetCookie.cookie;
                String contentId = page.getPageid();
                List<Comment> list = new Spider(cookie, contentId).process();
                for (Comment comment : list) {
                    WbComment wbCom = new WbComment();
                    wbCom.setPageid(id);
                    wbCom.setContent(comment.getText());
                    wbCom.setLikecount(comment.getLike_count());
                    System.out.println(wbCom);
                    wbCommentService.save(wbCom);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "数据爬取")).start();
        return Result.OK();
    }


    @GetMapping(value = "/getCommentList")
    public Result<?> getCommentList(WbComment wbComment,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LambdaQueryWrapper<WbComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(WbComment::getLikecount);
        Page<WbComment> page = new Page<WbComment>(pageNo, pageSize);
        IPage<WbComment> pageList = wbCommentService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Autowired
    private WbPageMapper pageMapper;

    @GetMapping(value = "/getTsChart")
    public Result<?> getTsChart() {
        List<TsDto> list = pageMapper.getTsChart();
        return Result.OK(list);
    }

}
