import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class InsertCurrentDateClassAsCommentAction extends EditorAction {
    protected InsertCurrentDateClassAsCommentAction() {
        super(new InsertCurrentDateClassAsCommentActionHandler());
    }

    public static class InsertCurrentDateClassAsCommentActionHandler extends EditorWriteActionHandler {
        /**
         * Inserts a new Java comment that contains the current date just before the current Java class.
         * <p>
         *     Note that this method is running in the UI thread.
         * </p>
         */
        @Override
        public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
            int offset = editor.getCaretModel().getOffset();

            Project project = dataContext.getData(CommonDataKeys.PROJECT);
            PsiFile psiFile = dataContext.getData(LangDataKeys.PSI_FILE);

            PsiElement elementUnderCaret = psiFile.findElementAt(offset);

            // let's build a Java PSI tree
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);

            // create a new Java single-line comment
            PsiComment commentElement = elementFactory.createCommentFromText("// " + Instant.now().toString(), null);

            // we will find the nearest Java class that wraps the current caret position
            PsiClass enclosingClassElement = PsiTreeUtil.getParentOfType(elementUnderCaret, PsiClass.class);
            // let's find the Java class parent PSI element
            PsiElement enclosingClassParentElement = enclosingClassElement.getParent();

            // under the PSI element, let's add a new Comment just before the Java class element
            enclosingClassParentElement.addBefore(commentElement, enclosingClassElement);
        }
    }
}
