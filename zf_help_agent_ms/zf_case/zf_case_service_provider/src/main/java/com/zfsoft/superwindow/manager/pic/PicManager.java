package com.zfsoft.superwindow.manager.pic;

import com.zfsoft.superwindow.dbaccess.dao.TDataSetMaterialImgRecMapper;
import com.zfsoft.superwindow.dbaccess.data.TDataSetMaterialImgRec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/21 14:03
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PicManager {
    private final TDataSetMaterialImgRecMapper tDataSetMaterialImgRecMapper;

    public int deletePicRecord(Long id) {
        //直接物理删除
        return tDataSetMaterialImgRecMapper.deleteById(id);
    }

    public int  savePicRecord(TDataSetMaterialImgRec tDataSetMaterialImgRec){
        return tDataSetMaterialImgRecMapper.insert(tDataSetMaterialImgRec);
    }

    public int  updatePicRecord(Long picRecordId, Integer isEmpty){
/*        if(null!=tDataSetMaterialImgRec.getId()){
            TDataSetMaterialImgRec  old = tDataSetMaterialImgRecMapper.queryById(tDataSetMaterialImgRec.getId());
            BeanUtils.copyProperties(tDataSetMaterialImgRec, old);
            //
            return tDataSetMaterialImgRecMapper.update(old);
        }else{
            return -1;
        }*/
        TDataSetMaterialImgRec  old = tDataSetMaterialImgRecMapper.queryById(picRecordId);
        if(old!=null){
            old.setIsEmpty(isEmpty);
            return  tDataSetMaterialImgRecMapper.update(old);
        }
        return -1;
    }

}
