package TaleSmith.mycommands

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import taleSmith.Game
import org.eclipse.core.resources.IProject
import org.eclipse.jface.viewers.TreeSelection
import org.eclipse.ui.IFileEditorInput
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.handlers.HandlerUtil
import taleSmith.presentation.TaleSmithEditor
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IFile
import java.io.InputStream
import java.io.ByteArrayInputStream

class CommandHandler extends AbstractHandler {
	
	override execute(ExecutionEvent event) throws ExecutionException {
		println("Executed Custom Command")

		// Get active part of the model eclipse instance
		var IWorkbenchPart wbp = HandlerUtil.getActivePart(event)

		// Cast to project editor
		var TaleSmithEditor editor = (wbp as TaleSmithEditor)

		// Get active project in the model eclipse instance
		var IProject project = (editor.editorInput as IFileEditorInput).file.project

		// Get selected element
		var TreeSelection selection = editor.selection as TreeSelection

		// Get first element (root element of the model)
		var Game root = selection.firstElement as Game

		generateFiles(project, root.compile)

		return void;
	}
	
	
	def generateFiles(IProject project, String content) {
	    val IFolder srcGen = project.getFolder("src-gen")
	    if (!srcGen.exists) {
	        srcGen.create(true, true, null)
	    }
	
	    // Wichtig: Ordner auch im Dateisystem sichtbar machen
	    srcGen.refreshLocal(1, null)
	
	    val IFile javaFile = srcGen.getFile("GameMain.java")
	
	    if (javaFile.exists) {
	        javaFile.delete(true, null)
	    }
	
	    val stream = new ByteArrayInputStream(content.bytes)
	    javaFile.create(stream, true, null)
	
	    // Datei registrieren & synchronisieren
	    javaFile.refreshLocal(0, null)
	}


	
	def compile(Game game) {
    return '''
        public class GameMain {
            public static void main(String[] args) {
                System.out.println("Willkommen im Spiel: «game.name»");
                // Hier könntest du durch game.rooms iterieren etc.
            }
        }
    '''
}

}
