package com.zfsoft.superwindow.controller.zxhd;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.zsgl.Notepad;
import com.zfsoft.superwindow.manager.zsgl.NotepadManager;
import com.zfsoft.superwindow.service.zsgl.NotepadService;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: liangss
 * @create: 2020-10-26 20:12:56
 * @description: 记事本控制层
 */
@Slf4j
@RestController
@RequestMapping(value = "/notepad")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotepadController implements NotepadService {

    private final NotepadManager notepadManager;

    /**
     * 分页查询记事本
     * @param notepad
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    @PostMapping(value = "/queryNotepadWithPage")
    public ApiResultSet<PageResult<Notepad>> queryNotepadWithPage(Notepad notepad, Integer pageNum, Integer pageSize) {
        PageResult<Notepad> pageResult=this.notepadManager.queryNotepadWithPage(notepad,pageNum,pageSize);
        log.info("获取记事本列表调用成功结果为：{}", JSON.toJSONString(pageResult));
       // return new ApiResultSet<>(pageResult);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        if(null!=CurrentLoginUserHolder.getCurrentLoginUser()) {
          String  loginUserOid= CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
          apiResultSet.setMessage(loginUserOid);
        }
        return apiResultSet;
    }

    /**
     * 分页查询个人记事本
     * @param notepad
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    @PostMapping(value = "/queryPersonalNotepadWithPage")
    public ApiResultSet<PageResult<Notepad>> queryPersonalNotepadWithPage(Notepad notepad, Integer pageNum, Integer pageSize) {
        log.info("获取当前登录信息为：{}", JSON.toJSONString(CurrentLoginUserHolder.getCurrentLoginUser()));
        if(null!=CurrentLoginUserHolder.getCurrentLoginUser()) {
             notepad.setCreateUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
        }
        PageResult<Notepad> pageResult=this.notepadManager.queryNotepadWithPage(notepad,pageNum,pageSize);
        log.info("获取个人记事本列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet<>(pageResult);
    }


    /**
     * 保存/更新记事本
     * @param notepad
     * @return
     */
    @Override
    @PostMapping(value = "/saveNotepad")
    public ApiResultSet<Notepad> saveNotepad(@RequestBody Notepad notepad) {
        notepadManager.saveOrUpdate(notepad);
        ApiResultSet<Notepad> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(notepad);
        return apiResultSet;
    }

    /**
     * 根据主键查询记事本信息
     * @param id
     * @return
     */
    @Override
    @PostMapping(value = "/getNotepadById")
    public ApiResultSet<Notepad> getNotepadById(Long id) {
        Notepad notepad=this.notepadManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(notepad));
        return new ApiResultSet(notepad);
    }

    /**
     * 根据主键删除记事本信息
     * @param id
     * @return
     */
    @Override
    @PostMapping(value = "/deleteNotepadById")
    public ApiResultSet<Integer> deleteNotepadById(Long id) {
        this.notepadManager.delNotepad(id);
        log.info("删除成功：{}", id);
        return null;
    }

    /**
     * 根据主键修改分享状态
     * @param id
     * @return
     */
    @Override
    @PostMapping(value = "/shareFlagNotepadById")
    public ApiResultSet<Integer> shareFlagNotepadById(Long id) {
        this.notepadManager.shareFlagNotepad(id);
        log.info("删除成功：{}", id);
        return null;
    }

    /**
     * 根据某年某月查询当月记事的所有日期及内容
     * @param time
     * @return
     */
    @Override
    @PostMapping(value = "/getOneMouthNotepad")
    public JSONArray getOneMouthNotepad(@Param("time") String time) {
        JSONArray list = this.notepadManager.getOneMouthNotepad(time);
        return list;
    }

    /**
     * 根据日期保存/更新记事本
     * @param notepad
     * @return
     */
    @Override
    @PostMapping(value = "/dateTosaveNotepad")
    public ApiResultSet<Notepad> dateTosaveNotepad(@RequestBody Notepad notepad) {
        notepadManager.dateTosaveNotepad(notepad);
        ApiResultSet<Notepad> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(notepad);
        return apiResultSet;
    }


}
