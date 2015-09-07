import com.stratio.notebook.socket.NotebookOperationFactory;
import com.stratio.notebook.socket.notebookOperations.*;
import org.junit.Test;
import static com.stratio.notebook.socket.Message.*;
import static org.junit.Assert.*;
/**
 * Created by idiaz on 4/08/15.
 */
public class NotebookOperationFactoryTest {





    @Test
    public void recoverderListNotesTest() {
         assertTrue("The recovered class must be BroadcastNoteListOperation", NotebookOperationFactory.getOperation(OP.LIST_NOTES) instanceof BroadcastNoteListOperation);
    }



    @Test
    public void getNotesTest() {
        assertTrue("The recovered class must be SendNoteOperation", NotebookOperationFactory.getOperation(OP.GET_NOTE) instanceof SendNoteOperation);
    }


    @Test
    public void importNotesTest() {
        assertTrue("The recovered class must be ImportNoteOperation", NotebookOperationFactory.getOperation(OP.IMPORT_NOTE) instanceof ImportNoteOperation);
    }

    @Test
    public void exportNotesTest() {
        assertTrue("The recovered class must be ExportNoteOperation", NotebookOperationFactory.getOperation(OP.EXPORT_NOTE) instanceof ExportNoteOperation);
    }

    @Test
    public void newNotesTest() {
        assertTrue("The recovered class must be CreateNoteOperation", NotebookOperationFactory.getOperation(OP.NEW_NOTE) instanceof CreateNoteOperation);
    }

    @Test
    public void delNotesTest() {
        assertTrue("The recovered class must be RemoveNoteOperation", NotebookOperationFactory.getOperation(OP.DEL_NOTE) instanceof RemoveNoteOperation);
    }

    @Test
    public void commitParagraphTest() {
        assertTrue("The recovered class must be UpdateParagraphOperation", NotebookOperationFactory.getOperation(OP.COMMIT_PARAGRAPH) instanceof UpdateParagraphOperation);
    }

    @Test
    public void runParagraphTest() {
        assertTrue("The recovered class must be RunParagraphOperation", NotebookOperationFactory.getOperation(OP.RUN_PARAGRAPH) instanceof RunParagraphOperation);
    }

    @Test
    public void cancelParagraphTest() {
        assertTrue("The recovered class must be CancelParagraphOperation", NotebookOperationFactory.getOperation(OP.CANCEL_PARAGRAPH) instanceof CancelParagraphOperation);
    }

    @Test
    public void moveParagraphTest() {
        assertTrue("The recovered class must be MoveParagraphOperation", NotebookOperationFactory.getOperation(OP.MOVE_PARAGRAPH) instanceof MoveParagraphOperation);
    }

    @Test
    public void splitIntoParagraphTest() {
        assertTrue("The recovered class must be SplitIntoParagraphsOperation", NotebookOperationFactory.getOperation(OP.SPLIT_INTO_PARAGRAPHS) instanceof SplitIntoParagraphsOperation);
    }

    @Test
    public void insertIntoParagraphTest() {
        assertTrue("The recovered class must be InsertParagraphOperation", NotebookOperationFactory.getOperation(OP.INSERT_PARAGRAPH) instanceof InsertParagraphOperation);
    }

    @Test
    public void removeParagraphTest() {
        assertTrue("The recovered class must be RemoveParagraphOperation", NotebookOperationFactory.getOperation(OP.PARAGRAPH_REMOVE) instanceof RemoveParagraphOperation);
    }

    @Test
    public void updateOperationTest() {
        assertTrue("The recovered class must be UpdateNoteOperation", NotebookOperationFactory.getOperation(OP.NOTE_UPDATE) instanceof UpdateNoteOperation);
    }

    @Test
    public void saveNoteTest() {
        assertTrue("The recovered class must be SaveNoteOperation", NotebookOperationFactory.getOperation(OP.SAVE_NOTE) instanceof SaveNoteOperation);
    }

    @Test
    public void resetResultTest() {
        assertTrue("The recovered class must be ResetResultsOperation", NotebookOperationFactory.getOperation(OP.RESET_RESULTS) instanceof ResetResultsOperation);
    }

    @Test
    public void completionTest() {
        assertTrue("The recovered class must be CompletionOperation", NotebookOperationFactory.getOperation(OP.COMPLETION) instanceof CompletionOperation);
    }

    @Test
    public void nullTest() {
        assertTrue("The recovered class must be BroadcastNoteListOperation", NotebookOperationFactory.getOperation(null) instanceof BroadcastNoteListOperation);
    }


}
