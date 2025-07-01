package TaleSmith.mycommands;

import java.io.ByteArrayInputStream;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import taleSmith.Game;
import taleSmith.presentation.TaleSmithEditor;

@SuppressWarnings("all")
public class CommandHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    InputOutput.<String>println("Executed Custom Command");
    IWorkbenchPart wbp = HandlerUtil.getActivePart(event);
    TaleSmithEditor editor = ((TaleSmithEditor) wbp);
    IEditorInput _editorInput = editor.getEditorInput();
    IProject project = ((IFileEditorInput) _editorInput).getFile().getProject();
    ISelection _selection = editor.getSelection();
    TreeSelection selection = ((TreeSelection) _selection);
    Object _firstElement = selection.getFirstElement();
    Game root = ((Game) _firstElement);
    this.generateFiles(project, this.compile(root));
    return void.class;
  }

  public void generateFiles(final IProject project, final String content) {
    try {
      final IFolder srcGen = project.getFolder("src-gen");
      boolean _exists = srcGen.exists();
      boolean _not = (!_exists);
      if (_not) {
        srcGen.create(true, true, null);
      }
      srcGen.refreshLocal(1, null);
      final IFile javaFile = srcGen.getFile("GameMain.java");
      boolean _exists_1 = javaFile.exists();
      if (_exists_1) {
        javaFile.delete(true, null);
      }
      byte[] _bytes = content.getBytes();
      final ByteArrayInputStream stream = new ByteArrayInputStream(_bytes);
      javaFile.create(stream, true, null);
      javaFile.refreshLocal(0, null);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  public String compile(final Game game) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class GameMain {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("public static void main(String[] args) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("System.out.println(\"Willkommen im Spiel: ");
    String _name = game.getName();
    _builder.append(_name, "        ");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("// Hier könntest du durch game.rooms iterieren etc.");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
