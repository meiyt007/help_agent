package com.zfsoft.microservice.form.util.poitl;

import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import com.deepoove.poi.render.WhereDelegate;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName MultiImageRenderPolicy
 * @Description: 多图片的poi组件
 * @Author wuxx
 * @Date 2021/12/9
 **/
public class MultiImageRenderPolicy extends AbstractRenderPolicy<List<PictureRenderData>> {

    @Override
    public void doRender(RenderContext<List<PictureRenderData>> context) throws Exception {
        WhereDelegate where = context.getWhereDelegate();
        Object o = context.getThing();
        if(o instanceof List){
            List<PictureRenderData> picList = context.getThing();
            if(picList != null && picList.size() != 0){
                for(PictureRenderData x: picList){
                    InputStream stream = null;
                    try {
                        byte[] bytes = x.getPictureSupplier().get();
                        stream = new ByteArrayInputStream(bytes);
                        where.addPicture(stream, x.getPictureType().type(), x.getPictureStyle().getWidth(), x.getPictureStyle().getHeight());
                        if(picList.size()>1)
                        where.getRun().setText(" ");
                    } finally {
                        IOUtils.closeQuietly(stream);
                    }
                }
            }
        }
    }

    @Override
    protected void afterRender(RenderContext<List<PictureRenderData>> context) {
        // 清空标签
        clearPlaceholder(context, true);
    }
}
