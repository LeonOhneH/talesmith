package TaleSmith.mycommands;

import java.io.ByteArrayInputStream;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class CommandHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    throw new Error("Unresolved compilation problems:"
      + "\nTaleSmithEditor cannot be resolved to a type."
      + "\nTaleSmithEditor cannot be resolved to a type."
      + "\nGame cannot be resolved to a type."
      + "\nGame cannot be resolved to a type."
      + "\neditorInput cannot be resolved"
      + "\nselection cannot be resolved"
      + "\ncompile cannot be resolved");
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

  public String compile(final /* Game */Object game) {
    throw new Error("Unresolved compilation problems:"
      + "\nname cannot be resolved");
  }
}
