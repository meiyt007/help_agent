package com.zfsoft.microservice.form.util.poitl;

import com.deepoove.poi.data.BookmarkTextRenderData;
import com.deepoove.poi.data.HyperlinkTextRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import com.deepoove.poi.util.StyleUtils;
import com.deepoove.poi.util.TableTools;
import com.deepoove.poi.xwpf.XWPFParagraphWrapper;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;

/**
 * checkbox render policy
 *
 * @author zje
 *
 */
public class CheckBoxRenderPolicy extends AbstractRenderPolicy<Object> {

    @Override
    protected boolean validate(Object data) {
        return true;//返回false标识验证不通过，不进行数据附加了
    }

    @Override
    public void doRender(RenderContext<Object> context) throws Exception {
        String data = (String) context.getData();
        if (data == null){
            data = "□";
        }
        Helper.renderTextRun(context.getRun(), data);
    }

    public static class Helper {

        public static final String REGEX_LINE_CHARACTOR = "\\n";

        public static void renderTextRun(XWPFRun run, Object data) {
            XWPFRun textRun = run;
            if (data instanceof HyperlinkTextRenderData) {
                textRun = createHyperlink(run, ((HyperlinkTextRenderData) data).getUrl());
            }

            TextRenderData wrapper = wrapper(data);
            StyleUtils.styleRun(textRun, wrapper.getStyle());

            String text = wrapper.getText();
            String[] fragment = text.split(REGEX_LINE_CHARACTOR, -1);
            if (fragment.length > 0) {
                textRun.setText(fragment[0], 0);
                boolean lineAtTable = fragment.length > 1 && !(data instanceof HyperlinkTextRenderData)
                        && TableTools.isInsideTable(run);
                for (int i = 1; i < fragment.length; i++) {
                    if (lineAtTable) {
                        textRun.addBreak(BreakType.TEXT_WRAPPING);
                    } else {
                        textRun.addCarriageReturn();
                    }
                    textRun.setText(fragment[i]);
                }
            }
            if (data instanceof BookmarkTextRenderData) {
                createBookmark(textRun, ((BookmarkTextRenderData) data).getBookmark());
            }
        }

        private static TextRenderData wrapper(Object obj) {
            TextRenderData text = obj instanceof TextRenderData ? (TextRenderData) obj
                    : new TextRenderData(obj.toString());
            return null == text.getText() ? new TextRenderData("") : text;
        }

        private static XWPFRun createHyperlink(XWPFRun run, String url) {
            XWPFParagraphWrapper paragraph = new XWPFParagraphWrapper((XWPFParagraph) run.getParent());
            XWPFRun hyperlink = paragraph.insertNewHyperLinkRun(run, url);
            StyleUtils.styleRun(hyperlink, run);
            run.setText("", 0);
            return hyperlink;
        }

        private static void createBookmark(XWPFRun textRun, String name) {
            XWPFParagraphWrapper wapper = new XWPFParagraphWrapper((XWPFParagraph) textRun.getParent());
            CTBookmark bookmarkStart = wapper.insertNewBookmark(textRun);
            bookmarkStart.setName(name);
        }
    }
}
