package com.stratio.notebook.socket;

import com.stratio.notebook.socket.notebookOperations.*;

/**
 * This Factory must create the correct Operation class.
 * Created by jmgomez on 3/09/15.
 */
public class NotebookOperationFactory {

    /**
     *
     * The factory method to recover an operation.
     * @param operation the operation id.
     * @return  The Operation.
     */
    public static INotebookOperation getOperation(Message.OP operation){

        INotebookOperation notebookOperation;
        if (operation!=null) {
            switch (operation) {
                case LIST_NOTES:
                    notebookOperation = new BroadcastNoteListOperation();
                    break;
                case GET_NOTE:
                    notebookOperation = new SendNoteOperation();
                    break;
                case IMPORT_NOTE:
                    notebookOperation = new ImportNoteOperation();
                    break;
                case EXPORT_NOTE:
                    notebookOperation = new ExportNoteOperation();
                    break;
                case NEW_NOTE:
                    notebookOperation = new CreateNoteOperation();
                    break;
                case DEL_NOTE:
                    notebookOperation = new RemoveNoteOperation();
                    break;
                case COMMIT_PARAGRAPH:
                    notebookOperation = new UpdateParagraphOperation();
                    break;
                case RUN_PARAGRAPH:
                    notebookOperation = new RunParagraphOperation();
                    break;
                case CANCEL_PARAGRAPH:
                    notebookOperation = new CancelParagraphOperation();
                    break;
                case MOVE_PARAGRAPH:
                    notebookOperation = new MoveParagraphOperation();
                    break;
                case SPLIT_INTO_PARAGRAPHS:
                    notebookOperation = new SplitIntoParagraphsOperation();
                    break;
                case INSERT_PARAGRAPH:
                    notebookOperation = new InsertParagraphOperation();
                    break;
                case PARAGRAPH_REMOVE:
                    notebookOperation = new RemoveParagraphOperation();
                    break;
                case NOTE_UPDATE:
                    notebookOperation = new UpdateNoteOperation();
                    break;
                case SAVE_NOTE:
                    notebookOperation = new SaveNoteOperation();
                    break;
                case RESET_RESULTS:
                    notebookOperation = new ResetResultsOperation();
                    break;
                case COMPLETION:
                    notebookOperation = new CompletionOperation();
                    break;
                default:
                    notebookOperation = new BroadcastNoteListOperation();
                    break;
            }
        }else{
            notebookOperation= new BroadcastNoteListOperation();
        }
        return  notebookOperation;
    }
}
