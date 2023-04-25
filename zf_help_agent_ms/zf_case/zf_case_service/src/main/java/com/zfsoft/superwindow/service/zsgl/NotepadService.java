package com.zfsoft.superwindow.service.zsgl;

import com.alibaba.fastjson.JSONArray;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.zsgl.Notepad;


/**
 * @ClassName NotepadService
 * @Description 记事本接口
 * @Author liangss
 * @Date 2020-10-26 19:33
 * @Version V1.0
 **/
public interface NotepadService {



    /**
     * 分页查询记事本
     * @param notepad
     * @param pageNumber
     * @param pageSize
     * @return
     */
    ApiResultSet<PageResult<Notepad>>   queryNotepadWithPage
    (Notepad notepad, Integer pageNum, Integer pageSize);

    /**
     * 分页查询个人记事本
     * @param notepad
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ApiResultSet<PageResult<Notepad>> queryPersonalNotepadWithPage(Notepad notepad, Integer pageNum, Integer pageSize);

    /**
     * 保存/更新记事本
     * @param notepad
     * @return
     */
    ApiResultSet<Notepad> saveNotepad(Notepad notepad);


    /**
     * 根据主键查询记事本信息
     * @param id
     * @return
     */
    ApiResultSet<Notepad>  getNotepadById(Long id);

    /**
     * 根据主键删除记事本信息
     * @param id
     * @return
     */
    ApiResultSet<Integer>  deleteNotepadById(Long id);


    /**
     * 根据主键修改分享状态
     * @param id
     * @return
     */
    ApiResultSet<Integer>  shareFlagNotepadById(Long id);


    /**
     * 根据某年某月查询当月记事的所有日期及内容
     * @param time
     * @return
     */
    JSONArray getOneMouthNotepad(String time);

    /**
     * 根据日期保存/更新记事本
     * @param notepad
     * @return
     */
    ApiResultSet<Notepad> dateTosaveNotepad(Notepad notepad);


}
