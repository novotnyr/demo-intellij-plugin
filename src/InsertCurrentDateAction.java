import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class InsertCurrentDateAction extends EditorAction {


    protected InsertCurrentDateAction() {
        super(new InsertCurrentDateActionHandler());
    }

    public static class InsertCurrentDateActionHandler extends EditorActionHandler  {
        @Override
        protected void doExecute(Editor editor, @Nullable Caret caret, DataContext dataContext) {
            Project project = dataContext.getData(CommonDataKeys.PROJECT);
            Document document = editor.getDocument();
            int offset = editor.getCaretModel().getOffset();
            String currentDate = Instant.now().toString();
            Runnable runnable = () -> {
                document.insertString(offset, currentDate);
            };
            WriteCommandAction.runWriteCommandAction(project, runnable);
        }
    }
}
