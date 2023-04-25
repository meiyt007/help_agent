package com.zfsoft.microservice.form.util.poitl;

import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.data.style.PictureStyle;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import com.deepoove.poi.util.BufferedImageUtils;
import com.deepoove.poi.util.SVGConvertor;
import com.deepoove.poi.util.UnitUtils;
import com.deepoove.poi.xwpf.BodyContainer;
import com.deepoove.poi.xwpf.BodyContainerFactory;
import com.deepoove.poi.xwpf.WidthScalePattern;
import com.deepoove.poi.xwpf.XWPFRunWrapper;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Supplier;

//浮动照片 针对签章 签名等  前缀符号 建议使用 `  示例{{`qz_150_0_25_0_0}}  图片属性名_水平偏移量_纵正负位_纵向偏移量_宽_高  宽高不设置时传0 必须 5个_格式 纵正负位：0负数
public class FloatPicRenderPolicy extends AbstractRenderPolicy<Object> {

    @Override
    protected boolean validate(Object data) {
        if (null == data) return false;
        if (data instanceof PictureRenderData) {
            return null != ((PictureRenderData) data).getPictureSupplier();
        }
        return true;
    }

    @Override
    public void doRender(RenderContext<Object> context) throws Exception {
        Helper.renderPicture(context.getRun(), wrapper(context.getData()));
    }

    @Override
    protected void afterRender(RenderContext<Object> context) {
        clearPlaceholder(context, false);
    }

    @Override
    protected void reThrowException(RenderContext<Object> context, Exception e) {
        logger.info("Render picture " + context.getEleTemplate() + " error: {}", e.getMessage());
        String alt = "";
        if (context.getData() instanceof PictureRenderData) {
            alt = ((PictureRenderData) context.getData()).getAltMeta();
        }
        context.getRun().setText(alt, 0);
    }

    private static PictureRenderData wrapper(Object object) {
        if (object instanceof PictureRenderData) return (PictureRenderData) object;
        return Pictures.of(object.toString()).fitSize().create();
    }

    public static class Helper {
        public static void renderPicture(XWPFRun run, PictureRenderData picture) throws Exception {
            Supplier<byte[]> supplier = picture.getPictureSupplier();
            byte[] imageBytes = supplier.get();
            if (null == imageBytes) {
                throw new IllegalStateException("Can't read picture byte arrays!");
            }
            PictureType pictureType = picture.getPictureType();
            if (null == pictureType) {
                pictureType = PictureType.suggestFileType(imageBytes);
            }
            if (null == pictureType) {
                throw new RenderException("PictureRenderData must set picture type!");
            }

            String altMeta = picture.getAltMeta();
            String[] split = altMeta.split("_");
            int leftOffset = Integer.parseInt(split[0]);
            int topOffset = Integer.parseInt(split[1]);

            PictureStyle style = picture.getPictureStyle();
            if (null == style) style = new PictureStyle();
            int width = style.getWidth();
            int height = style.getHeight();

            if (pictureType == PictureType.SVG) {
                imageBytes = SVGConvertor.toPng(imageBytes, (float) width, (float) height);
                pictureType = PictureType.PNG;
            }
            if (!isSetSize(style)) {
                BufferedImage original = BufferedImageUtils.readBufferedImage(imageBytes);
                width = original.getWidth();
                height = original.getHeight();
                if (style.getScalePattern() == WidthScalePattern.FIT) {
                    BodyContainer bodyContainer = BodyContainerFactory
                            .getBodyContainer(((IBodyElement) run.getParent()).getBody());
                    int pageWidth = UnitUtils
                            .twips2Pixel(bodyContainer.elementPageWidth((IBodyElement) run.getParent()));
                    if (width > pageWidth) {
                        double ratio = pageWidth / (double) width;
                        width = pageWidth;
                        height = (int) (height * ratio);
                    }
                }
            }
            try (InputStream stream = new ByteArrayInputStream(imageBytes)) {
                PictureStyle.PictureAlign align = style.getAlign();
                if (null != align && run.getParent() instanceof XWPFParagraph) {
                    ((XWPFParagraph) run.getParent()).setAlignment(ParagraphAlignment.valueOf(align.ordinal() + 1));
                }
                // 添加浮动图片
                XWPFRunWrapper wrapper = new XWPFRunWrapper(run, false);
                wrapper.addPicture(stream, pictureType.type(), "Generated", Units.pixelToEMU(width),
                        Units.pixelToEMU(height));
                stream.close();
                // 获取到图片数据
                CTDrawing drawing = run.getCTR().getDrawingArray(0);
                CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();
                //拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
                CTAnchor anchor = getAnchorWithGraphic(graphicalobject, split[2],
                        Units.toEMU(width), Units.toEMU(height),//图片大小
                        //横向偏移范围0~680(A4)范围 算上图片宽度w 0 ~ 680-w   纵向偏移量随便，建议 是 图片高度h的一半的负值   -h/2
                        Units.toEMU(leftOffset), Units.toEMU(topOffset), false);//相对当前段落位置 需要计算段落已有内容的左偏移
                drawing.setAnchorArray(new CTAnchor[]{anchor});//添加浮动属性
                drawing.removeInline(0);//删除行内属性
            }
        }

        private static boolean isSetSize(PictureStyle style) {
            return (style.getWidth() != 0 || style.getHeight() != 0)
                    && style.getScalePattern() == WidthScalePattern.NONE;
        }

        /**
         * @param ctGraphicalObject 图片数据
         * @param deskFileName      图片描述
         * @param width             宽
         * @param height            高
         * @param leftOffset        水平偏移 left
         * @param topOffset         垂直偏移 top
         * @param behind            文字上方，文字下方
         * @return
         * @throws Exception
         */
        private static CTAnchor getAnchorWithGraphic(CTGraphicalObject ctGraphicalObject,
                                                     String deskFileName, int width, int height,
                                                     int leftOffset, int topOffset, boolean behind) {
            String anchorXML =
                    "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                            + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"" + ((behind) ? 1 : 0) + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                            + "<wp:simplePos x=\"0\" y=\"0\"/>"
                            + "<wp:positionH relativeFrom=\"column\">"
                            + "<wp:posOffset>" + leftOffset + "</wp:posOffset>"
                            + "</wp:positionH>"
                            + "<wp:positionV relativeFrom=\"paragraph\">"
                            + "<wp:posOffset>" + topOffset + "</wp:posOffset>" +
                            "</wp:positionV>"
                            + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
                            + "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>"
                            + "<wp:wrapNone/>"
                            + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + deskFileName + "\"/><wp:cNvGraphicFramePr/>"
                            + "</wp:anchor>";

            CTDrawing drawing = null;
            try {
                drawing = CTDrawing.Factory.parse(anchorXML);
            } catch (XmlException e) {
                e.printStackTrace();
            }
            CTAnchor anchor = drawing.getAnchorArray(0);
            anchor.setGraphic(ctGraphicalObject);
            return anchor;
        }
    }
}
